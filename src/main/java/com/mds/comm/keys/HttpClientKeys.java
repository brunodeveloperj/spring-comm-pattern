package com.mds.comm.keys;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * String constants shared across the HTTP client library — URL delimiters,
 * interceptor signals, and telemetry messages.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpClientKeys {

  /** The empty string. */
  public static final String EMPTY = "";
  /** The right slash character. */
  public static final String RIGHT_SLASH = "/";
  /** The prefix interceptor signal. */
  public static final String PREFIX_INTERCEPTOR_SIGNAL = "::";

  /** The query parameter interceptor signal in character type. */
  public static final char QUERY_PARAM_INTERCEPTOR_SIGNAL_CHARACTER = '?';

  /** The query parameter interceptor signal. */
  public static final String QUERY_PARAM_INTERCEPTOR_SIGNAL = "?";

  public static final String TELEMETRY_MESSAGE = "Validate feign client telemetry.";

}
