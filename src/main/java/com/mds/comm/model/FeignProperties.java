package com.mds.comm.model;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Holds the unencrypted and encrypted {@link Session} lists that drive
 * the Feign interceptor pipeline.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public class FeignProperties {

  /**
   * The list of unencrypted sessions.
   */
  private final List<Session> unencryptedSession;

  /**
   * The list of encrypted sessions.
   */
  private final List<Session> encryptedSession;

  private List<Session> sessions = null;

  // *************************** Builder Method ***************************

  /**
   * Creates an empty instance with no sessions.
   */
  public FeignProperties() {
    this.unencryptedSession = new ArrayList<>();
    this.encryptedSession = new ArrayList<>();
  }

  /**
   * Creates an instance with the given session lists (stored as unmodifiable copies).
   *
   * @param unencryptedSession the plain sessions
   * @param encryptedSession   the encrypted sessions
   */
  public FeignProperties(final List<Session> unencryptedSession, final List<Session> encryptedSession) {
    this.unencryptedSession = unmodifiableList(unencryptedSession);
    this.encryptedSession = unmodifiableList(encryptedSession);
  }

  // *************************** Get & Set Method ***************************

  /**
   * Returns an unmodifiable view of the unencrypted sessions.
   *
   * @return the unencrypted session list
   */
  public List<Session> getUnencryptedSession() {
    return unmodifiableList(unencryptedSession);
  }

  /**
   * Returns {@code true} when at least one unencrypted session exists.
   *
   * @return {@code true} if the list is non-empty
   */
  public boolean existsUnencryptedSession() {
    return (unencryptedSession != null && !unencryptedSession.isEmpty());
  }

  /**
   * Returns an unmodifiable view of the encrypted sessions.
   *
   * @return the encrypted session list
   */
  public List<Session> getEncryptedSession() {
    return unmodifiableList(encryptedSession);
  }

  /**
   * Returns a lazily-built, unmodifiable view combining both session lists.
   *
   * @return all sessions
   */
  public List<Session> getSessions() {
    if (sessions == null) {
      Stream<Session> combinedStream = Stream.of(unencryptedSession, encryptedSession).flatMap(Collection::stream);
      sessions = combinedStream.collect(Collectors.toList());
    }
    return unmodifiableList(sessions);
  }

  /**
   * Replaces the combined session list.
   *
   * @param sessions the new session list
   */
  public void setSessions(final List<Session> sessions) {
    this.sessions = unmodifiableList(sessions);
  }

  /**
   * Returns {@code true} when at least one session (encrypted or not) exists.
   *
   * @return {@code true} if any session is present
   */
  public boolean existsSessions() {
    return (unencryptedSession != null && !unencryptedSession.isEmpty()) || (encryptedSession != null && !encryptedSession.isEmpty());
  }
}
