package gov.va.aes.vear.gal.utils;

import java.util.concurrent.CountDownLatch;

/**
 * 
 * @param <S>
 *            S
 * @param <T>
 *            T
 */
public interface Task<T, S> {

    T process(S input, CountDownLatch latch) throws Exception;
}