package com.mds.comm;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration entry point for the MDS HTTP Client library.
 *
 * <p>Registers all beans under the {@code com.mds.comm} package,
 * including Feign interceptors, OkHttp transport, and default session
 * configuration.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@ComponentScan
public class HttpClientAutoConfiguration {

}
