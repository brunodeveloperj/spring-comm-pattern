package com.mds.comm.interfaces;

/**
 * Functional interface for a void operation that may throw any
 * {@link Throwable}.
 *
 * @param <T> the type of the input value
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@FunctionalInterface
public interface ExecutableVoidGeneral<T> {

  /**
   * Executes the operation on the given value.
   *
   * @param value the input value
   * @throws Throwable if an error occurs during execution
   */
  void execute(T value) throws Throwable;
}
