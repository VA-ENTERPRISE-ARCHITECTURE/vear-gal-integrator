package gov.va.aes.vear.gal.utils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Generified future running and completion
 *
 * @param <T>
 *            the result type
 * @param <S>
 *            the task input
 */
public class WaitingFuturesRunner<T, S> {
    private transient static final Logger log = Logger.getLogger(WaitingFuturesRunner.class.getName());
    private final Collection<Task<T, S>> tasks;
    private final long timeOut;
    private final TimeUnit timeUnit;
    private final ExecutorService executor;

    /**
     * Constructor, used to initialise with the required tasks
     *
     * @param tasks
     *            the list of tasks to execute
     * @param timeOut
     *            max length of time to wait
     * @param timeUnit
     *            time out timeUnit
     */
    public WaitingFuturesRunner(final Collection<Task<T, S>> tasks, final int threadCount, final long timeOut,
	    final TimeUnit timeUnit) {
	this.tasks = tasks;
	this.timeOut = timeOut;
	this.timeUnit = timeUnit;
	this.executor = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * Go!
     *
     * @param taskInput
     *            The input to the task
     * @param consolidatedResult
     *            a container of all the completed results
     */
    public void go(final S taskInput, final ConsolidatedResult<T> consolidatedResult) {
	final CountDownLatch latch = new CountDownLatch(tasks.size());
	final List<CompletableFuture<T>> theFutures = tasks.stream()
		.map(aSearch -> CompletableFuture.supplyAsync(() -> processTask(aSearch, taskInput, latch), executor))
		.collect(Collectors.<CompletableFuture<T>>toList());

	final CompletableFuture<List<T>> allDone = collectTasks(theFutures);
	try {
	    latch.await(timeOut, timeUnit);
	    log.log(Level.FINE, "complete... adding results");
	    allDone.get().forEach(consolidatedResult::addResult);
	    this.executor.shutdown();
	} catch (final InterruptedException | ExecutionException e) {
	    log.log(Level.SEVERE, "go Error", e);
	    throw new RuntimeException("go Error, could not complete processing", e);
	}
    }

    private <E> CompletableFuture<List<E>> collectTasks(final List<CompletableFuture<E>> futures) {
	final CompletableFuture<Void> allDoneFuture = CompletableFuture
		.allOf(futures.toArray(new CompletableFuture[futures.size()]));
	return allDoneFuture
		.thenApply(v -> futures.stream().map(CompletableFuture<E>::join).collect(Collectors.<E>toList()));
    }

    private T processTask(final Task<T, S> task, final S searchTerm, final CountDownLatch latch) {
	log.log(Level.FINE, "Starting: " + task);
	T searchResults = null;
	try {
	    searchResults = task.process(searchTerm, latch);
	} catch (final Exception e) {
	    throw new RuntimeException("processTask Error, could not complete processing", e);
	}
	return searchResults;
    }

}
