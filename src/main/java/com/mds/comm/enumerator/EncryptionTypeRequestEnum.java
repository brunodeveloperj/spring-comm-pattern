package com.mds.comm.enumerator;

import com.mds.comm.enumerator.pattern.EnumerationPattern;

import java.util.Arrays;
import java.util.List;

/**
 * Encryption strategies applicable to request headers (authorization,
 * encrypted object).
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 * @see EnumerationPattern
 */
public enum EncryptionTypeRequestEnum implements EnumerationPattern {
  /**
   * The request is not encrypted.
   */
  NONE(1, "None"),

  /**
   * The request is encrypted using the default encryption algorithm.
   */
  DEFAULT(2, "Default"),

  /**
   * The request is encrypted using the authorization header encryption algorithm.
   */
  AUTHORIZATION(3, "Authorization"),

  /**
   * The request is encrypted using the encrypted object encryption algorithm.
   */
  ENCRYPTED_OBJECT(4, "EncryptedObject");

  /**
   * The order of the encryption type.
   */
  private final int order;

  /**
   * The description of the encryption type.
   */
  private final String description;

  /**
   * Creates a new entry with the given order and description.
   *
   * @param order       the display order
   * @param description the human-readable description
   */
  EncryptionTypeRequestEnum(int order, String description) {
    this.order = order;
    this.description = description;
  }

  /**
   * Returns {@code true} if any of the given types requires an authorization header.
   *
   * @param encryptionTypeRequest the types to check
   * @return {@code true} when authorization applies
   */
  public static boolean validateAuthorization(EncryptionTypeRequestEnum... encryptionTypeRequest) {
    final List<EncryptionTypeRequestEnum> authDomainList = List.of(DEFAULT, AUTHORIZATION);
    return authDomainList.stream().anyMatch(a -> Arrays.asList(encryptionTypeRequest).contains(a));
  }

  /**
   * Returns {@code true} if any of the given types requires an encrypted object header.
   *
   * @param encryptionTypeRequest the types to check
   * @return {@code true} when encrypted-object applies
   */
  public static boolean validateEncryptedObject(EncryptionTypeRequestEnum... encryptionTypeRequest) {
    final List<EncryptionTypeRequestEnum> encryptedObjectDomainList = List.of(DEFAULT, ENCRYPTED_OBJECT);
    return encryptedObjectDomainList.stream().anyMatch(a -> Arrays.asList(encryptionTypeRequest).contains(a));
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
