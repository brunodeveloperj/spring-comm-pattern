package com.mds.comm.interfaces;

import com.mds.comm.model.FeignProperties;
import feign.RequestInterceptor;
import feign.ResponseInterceptor;

/**
 * Contract for a Feign interceptor that participates in both request and
 * response pipelines.
 *
 * <p>Extends {@link RequestInterceptor} and {@link ResponseInterceptor} so
 * that a single bean can apply headers, encrypt parameters on the way out,
 * and inspect responses on the way back.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public interface FeignClientApi extends RequestInterceptor, ResponseInterceptor {

  /**
   * Builds the {@link FeignProperties} that describe which sessions are
   * encrypted and which are not.
   *
   * @return the Feign properties for this client
   */
  FeignProperties generateFeignProperties();
}
