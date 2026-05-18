package com.mds.comm.enumerator;

import com.mds.comm.enumerator.pattern.EnumerationPattern;

import java.util.Arrays;
import java.util.List;

/**
 * Encryption strategies applicable to path and query parameters.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 * @see EnumerationPattern
 */
public enum EncryptionTypeParameterEnum implements EnumerationPattern {
  /** The parameter is not encrypted. */
  NONE(1, "None"),
  /** The parameter is encrypted using the default encryption algorithm. */
  DEFAULT(2, "Default"),
  /** The parameter is encrypted using the path parameter encryption algorithm. */
  PATH_PARAM(3, "Path Param"),
  /** The parameter is encrypted using the query parameter encryption algorithm. */
  QUERY_PARAM(4, "Query Param");

  /** The order of the encryption type. */
  private final int order;

  /** The description of the encryption type. */
  private final String description;

  /**
   * Creates a new entry with the given order and description.
   *
   * @param order       the display order
   * @param description the human-readable description
   */
  EncryptionTypeParameterEnum(int order, String description) {
    this.order = order;
    this.description = description;
  }

  /**
   * Returns {@code true} if any of the given types requires path-parameter encryption.
   *
   * @param encryptionTypeParameter the types to check
   * @return {@code true} when path encryption applies
   */
  public static boolean validatePathParam(EncryptionTypeParameterEnum... encryptionTypeParameter) {
    final List<EncryptionTypeParameterEnum> pathParamList = List.of(DEFAULT, PATH_PARAM);
    return pathParamList.stream().anyMatch(t -> Arrays.asList(encryptionTypeParameter).contains(t));
  }

  /**
   * Returns {@code true} if any of the given types requires query-parameter encryption.
   *
   * @param encryptionTypeParameter the types to check
   * @return {@code true} when query encryption applies
   */
  public static boolean validateQueryParam(EncryptionTypeParameterEnum... encryptionTypeParameter) {
    final List<EncryptionTypeParameterEnum> queryParamList = List.of(DEFAULT, QUERY_PARAM);
    return queryParamList.stream()
        .anyMatch(t -> Arrays.asList(encryptionTypeParameter).contains(t));
  }

  /** {@inheritDoc} */
  @Override
  public int getOrder() {
    return order;
  }

  /** {@inheritDoc} */
  @Override
  public String getDescription() {
    return description;
  }
}
