package gov.va.aes.vear.gal.utils;

/**
 * Results of all the completed futures.
 * Created by brian on 4/26/14.
 */
public interface ConsolidatedResult<T> {

    void addResult(T result);
}