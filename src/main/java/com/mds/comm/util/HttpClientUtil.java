package com.mds.comm.util;

import com.mds.comm.interfaces.ExecutableVoidGeneral;
import com.mds.error.handler.exception.GeneralException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.json.JsonMapper;

/**
 * Static helpers shared across the HTTP client library — collection
 * converters, breakable stream iteration, validation executor, and
 * JSON serialisation via Jackson 3.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpClientUtil {

  private static final JsonMapper jsonMapper = JsonMapper.builder().build();
  private static final ObjectWriter objectWriter = jsonMapper.writerWithDefaultPrettyPrinter();

  /**
   * Wraps the given elements into an immutable collection.
   *
   * @param elements the elements
   * @param <T>      the element type
   * @return an immutable collection
   */
  @SafeVarargs
  public static <T> Collection<T> convertToCollection(T... elements) {
    return List.of(elements);
  }

  /**
   * Wraps the given elements into an immutable list.
   *
   * @param elements the elements
   * @param <T>      the element type
   * @return an immutable list
   */
  @SafeVarargs
  public static <T> List<T> convertToList(T... elements) {
    return (List<T>) convertToCollection(elements);
  }

  /**
   * Iterates a stream with an early-exit {@link Breaker} mechanism.
   *
   * @param stream   the source stream
   * @param consumer a bi-consumer receiving each element and a breaker
   * @param <T>      the element type
   */
  public static <T> void forEach(Stream<T> stream, BiConsumer<T, Breaker> consumer) {
    Spliterator<T> spliterator = stream.spliterator();
    boolean hadNext = true;
    Breaker breaker = new Breaker();

    while (hadNext && !breaker.get()) {
      hadNext = spliterator.tryAdvance(elem -> consumer.accept(elem, breaker));
    }
  }

  /**
   * Flag object used by {@link #forEach} to signal early termination.
   */
  public static class Breaker {

    private boolean shouldBreak = false;

    public void stop() {
      shouldBreak = true;
    }

    boolean get() {
      return shouldBreak;
    }
  }

  /**
   * Executes a void operation on every entry of the set, stopping at the
   * first {@link GeneralException} and re-throwing it.
   *
   * @param entries        the entries to process
   * @param executableVoid the operation to apply
   * @param <T>            the entry type
   * @throws GeneralException if the operation fails on any entry
   */
  public static <T> void executableGeneralValidate(Set<T> entries, ExecutableVoidGeneral<T> executableVoid) throws GeneralException {
    AtomicReference<GeneralException> atomicException = new AtomicReference<>(null);

    forEach(entries.stream(), (entry, breaker) -> {
      try {
        executableVoid.execute(entry);
      } catch (Throwable e) {
        atomicException.set((GeneralException) e);
        breaker.stop();
      }
    });

    GeneralException exception = atomicException.get();

    if (exception != null) {
      throw exception;
    }
  }

  /**
   * Serialises an object to pretty-printed JSON. Returns an empty string
   * when the value is {@code null} or serialisation fails.
   *
   * @param value the object to serialise
   * @return the JSON string, or {@code ""} on failure
   */
  public static String convertObjectWriteToJson(Object value) {
    String json = "";
    try {
      if (value != null) {
        json = objectWriter.writeValueAsString(value);
      }
    } catch (RuntimeException jx) {
      // No content
    }
    return json;
  }
}
