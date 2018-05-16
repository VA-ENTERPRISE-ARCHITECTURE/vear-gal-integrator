package gov.va.aes.vear.gal.utils;

import java.util.concurrent.CountDownLatch;

/**
 * Generic Task
 * Created by brian on 4/26/14.
 *
 * @param <S> S
 * @param <T> T
 */
public interface Task<T, S> {

    T process(S input, CountDownLatch latch);
}