package com.mds.comm.config;

import com.mds.comm.interfaces.FeignConfigApi;
import com.mds.comm.model.Session;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Configuration;

/**
 * Default {@link FeignConfigApi} that returns empty session lists.
 *
 * <p>Acts as a no-op baseline; downstream projects override by declaring
 * their own {@code @Configuration} bean qualified as
 * {@code "defaultFeignConfig"}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Configuration(value = "defaultFeignConfig")
public class DefaultFeignConfig implements FeignConfigApi {

  /** {@inheritDoc} */
  @Override
  public List<Session> joinUnencryptedSession() {
    return new ArrayList<>();
  }

  /** {@inheritDoc} */
  @Override
  public List<Session> joinEncryptedSession() {
    return new ArrayList<>();
  }
}
