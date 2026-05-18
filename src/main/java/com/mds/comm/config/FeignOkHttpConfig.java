package com.mds.comm.config;

import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Provides the default {@link OkHttpClient} bean used as the Feign HTTP
 * transport.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
public class FeignOkHttpConfig {

  /**
   * Creates the primary OkHttp-backed Feign client.
   *
   * @return the OkHttpClient bean
   */
  @Bean
  @Primary
  public OkHttpClient client() {
    return new OkHttpClient();
  }

}
