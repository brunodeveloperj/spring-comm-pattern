package com.mds.comm.interfaces;

import com.mds.comm.model.FeignProperties;
import com.mds.comm.model.Session;

import java.util.List;

/**
 * Configuration contract for supplying encrypted and unencrypted
 * {@link Session} lists to the Feign interceptor pipeline.
 *
 * <p>Implementations declare which sessions require encryption and which
 * are plain. The default {@link #generateFeignConfig()} merges both lists
 * into a single {@link FeignProperties} instance.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public interface FeignConfigApi {

  /**
   * Generates a new Feign configuration if the current one is null, otherwise returns the current
   * configuration.
   *
   * @return the Feign configuration
   */
  default FeignProperties generateFeignConfig() {
    return new FeignProperties(joinUnencryptedSession(), joinEncryptedSession());
  }

  /**
   * Retrieves a list of unencrypted sessions.
   *
   * @return a list of unencrypted sessions
   */
  List<Session> joinUnencryptedSession();

  /**
   * Retrieves a list of encrypted sessions.
   *
   * @return a list of encrypted sessions
   */
  List<Session> joinEncryptedSession();
}
