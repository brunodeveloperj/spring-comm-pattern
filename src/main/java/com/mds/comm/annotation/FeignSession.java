package com.mds.comm.annotation;

import com.mds.comm.enumerator.EncryptionTypeParameterEnum;
import com.mds.comm.enumerator.EncryptionTypeRequestEnum;
import com.mds.comm.keys.HttpClientKeys;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * Declares a Feign session with its encryption strategy and exclusion rules.
 *
 * <p>Applied at type or field level to configure how
 * {@link com.mds.comm.base.AbstractFeignClientBase} intercepts
 * requests — which parameters to encrypt and which to exclude.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface FeignSession {

  /** The properties of the session. */
  @AliasFor(value = "name")
  String properties();

  @AliasFor(value = "properties")
  String name() default HttpClientKeys.EMPTY;

  /** The encryption type of the request. */
  EncryptionTypeRequestEnum encryptionTypeRequest() default EncryptionTypeRequestEnum.NONE;

  /** The encryption type parameter of the request. */
  EncryptionTypeParameterEnum encryptionTypeParameter() default EncryptionTypeParameterEnum.NONE;

  /** The list of parameters to be excluded from the encryption. */
  String[] paramExcludes() default {};
}
