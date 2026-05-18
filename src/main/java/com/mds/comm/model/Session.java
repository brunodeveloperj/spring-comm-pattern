package com.mds.comm.model;

import static java.util.Collections.unmodifiableList;

import com.mds.comm.enumerator.EncryptionTypeParameterEnum;
import com.mds.comm.enumerator.EncryptionTypeRequestEnum;
import com.mds.comm.keys.HttpClientKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a Feign session with its encryption strategy and the list
 * of parameters excluded from encryption.
 *
 * <p>Use the static {@link #join} factory methods to build instances.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public class Session {

  /**
   * The properties of the session.
   */
  private String properties;

  /**
   * The encryption type of the request.
   */
  private EncryptionTypeRequestEnum encryptionTypeRequest;

  /**
   * The encryption type parameter of the request.
   */
  private EncryptionTypeParameterEnum encryptionTypeParameter;

  /**
   * The list of parameters to be excluded from the encryption.
   */
  private List<String> paramExcludes;

  // *************************** Builder Method ***************************

  /**
   * Creates a session with default (no encryption) settings.
   */
  public Session() {
    this.properties = HttpClientKeys.EMPTY;
    this.encryptionTypeRequest = EncryptionTypeRequestEnum.NONE;
    this.encryptionTypeParameter = EncryptionTypeParameterEnum.NONE;
    this.paramExcludes = new ArrayList<>();
  }

  /**
   * Creates a fully configured session.
   *
   * @param properties              the session identifier / URL prefix
   * @param encryptionTypeRequest   the request-level encryption strategy
   * @param encryptionTypeParameter the parameter-level encryption strategy
   * @param paramExcludes           parameters excluded from encryption
   */
  public Session(final String properties, final EncryptionTypeRequestEnum encryptionTypeRequest, final EncryptionTypeParameterEnum encryptionTypeParameter, final List<String> paramExcludes) {
    this.properties = properties;
    this.encryptionTypeRequest = encryptionTypeRequest;
    this.encryptionTypeParameter = encryptionTypeParameter;
    this.paramExcludes = unmodifiableList(paramExcludes);
  }

  /**
   * Factory method — full configuration.
   *
   * @param properties              the session identifier / URL prefix
   * @param encryptType             the request-level encryption strategy
   * @param encryptionTypeParameter the parameter-level encryption strategy
   * @param paramExcludes           parameters excluded from encryption
   * @return a new session
   */
  public static Session join(final String properties, final EncryptionTypeRequestEnum encryptType, final EncryptionTypeParameterEnum encryptionTypeParameter, final List<String> paramExcludes) {
    return new Session(properties, encryptType, encryptionTypeParameter, paramExcludes);
  }

  /**
   * Factory method — without exclusion list.
   *
   * @param properties              the session identifier / URL prefix
   * @param encryptType             the request-level encryption strategy
   * @param encryptionTypeParameter the parameter-level encryption strategy
   * @return a new session
   */
  public static Session join(final String properties, final EncryptionTypeRequestEnum encryptType, final EncryptionTypeParameterEnum encryptionTypeParameter) {
    return join(properties, encryptType, encryptionTypeParameter, new ArrayList<>());
  }

  /**
   * Factory method — request encryption only.
   *
   * @param properties  the session identifier / URL prefix
   * @param encryptType the request-level encryption strategy
   * @return a new session
   */
  public static Session join(final String properties, final EncryptionTypeRequestEnum encryptType) {
    return join(properties, encryptType, EncryptionTypeParameterEnum.NONE, new ArrayList<>());
  }

  /**
   * Factory method — identifier only, no encryption.
   *
   * @param properties the session identifier / URL prefix
   * @return a new session
   */
  public static Session join(final String properties) {
    return join(properties, EncryptionTypeRequestEnum.NONE, EncryptionTypeParameterEnum.NONE, new ArrayList<>());
  }

  /**
   * Returns the session identifier / URL prefix.
   *
   * @return the properties string
   */
  public String getProperties() {
    return properties;
  }

  /**
   * Returns {@code true} when the properties string is non-null and non-blank.
   *
   * @return {@code true} if present
   */
  public boolean hasProperties() {
    return (properties != null && !properties.isBlank());
  }

  /**
   * Sets the session identifier / URL prefix.
   *
   * @param properties the properties string
   */
  public void setProperties(final String properties) {
    this.properties = properties;
  }

  /**
   * Returns {@code true} if the properties string contains the given target.
   *
   * @param target the substring to look for
   * @return {@code true} when present
   */
  public boolean containsProperties(final String target) {
    if (properties == null || properties.isBlank()) {
      properties = HttpClientKeys.EMPTY;
    }
    return properties.contains(target);
  }

  /**
   * Returns the request-level encryption strategy.
   *
   * @return the encryption type
   */
  public EncryptionTypeRequestEnum getEncryptionTypeRequest() {
    return encryptionTypeRequest;
  }

  /**
   * Sets the request-level encryption strategy.
   *
   * @param encryptionTypeRequest the encryption type
   */
  public void setEncryptionTypeRequest(final EncryptionTypeRequestEnum encryptionTypeRequest) {
    this.encryptionTypeRequest = encryptionTypeRequest;
  }

  /**
   * Returns the parameter-level encryption strategy.
   *
   * @return the parameter encryption type
   */
  public EncryptionTypeParameterEnum getEncryptionTypeParameter() {
    return encryptionTypeParameter;
  }

  /**
   * Sets the parameter-level encryption strategy.
   *
   * @param encryptionTypeParameter the parameter encryption type
   */
  public void setEncryptionTypeParameter(final EncryptionTypeParameterEnum encryptionTypeParameter) {
    this.encryptionTypeParameter = encryptionTypeParameter;
  }

  /**
   * Returns the unmodifiable list of parameters excluded from encryption.
   *
   * @return the exclusion list
   */
  public List<String> getParamExcludes() {
    return unmodifiableList(paramExcludes);
  }

  /**
   * Replaces the exclusion list.
   *
   * @param paramExcludes the parameters to exclude
   */
  public void setParamExcludes(final List<String> paramExcludes) {
    this.paramExcludes = unmodifiableList(paramExcludes);
  }

  /**
   * Returns {@code true} if any of the given parameter names appears in the
   * exclusion list (case-insensitive).
   *
   * @param param the parameter name(s) to check
   * @return {@code true} when at least one match is found
   */
  public boolean existsParamInExcludes(String... param) {
    final List<String> paramList = List.of(param);
    return Optional.ofNullable(paramExcludes).orElse(new ArrayList<>()).stream().anyMatch(p -> paramList.stream().anyMatch(p::equalsIgnoreCase));
  }
}
