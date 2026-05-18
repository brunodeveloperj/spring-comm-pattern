package com.mds.comm.exception;

/**
 * Unchecked exception thrown when a Feign request interception fails.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public class PatternRequestException extends RuntimeException {

  public PatternRequestException(Throwable tx) {
    super(tx);
  }
}
