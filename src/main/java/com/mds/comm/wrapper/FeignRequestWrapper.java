package com.mds.comm.wrapper;

import static java.lang.String.join;
import static java.util.Collections.unmodifiableSet;
import static java.util.regex.Pattern.quote;

import com.mds.comm.keys.HttpClientKeys;
import com.mds.comm.model.FeignProperties;
import com.mds.comm.model.Session;
import feign.RequestTemplate;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps a Feign {@link RequestTemplate} and pre-processes it by resolving
 * the active {@link Session}, extracting path / query parameters, and
 * preparing the URL for encryption when needed.
 *
 * <p>Subclasses (e.g.
 * {@link com.mds.comm.base.AbstractFeignClientBase}) use the
 * resolved state to apply encryption and headers.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public class FeignRequestWrapper {

  /**
   * The logger.
   */
  private static final Logger logger = LoggerFactory.getLogger(FeignRequestWrapper.class);

  /**
   * The Feign properties.
   */
  private FeignProperties properties;

  /**
   * The Feign template.
   */
  private RequestTemplate template;

  /**
   * The body of the request.
   */
  private String body;

  /**
   * The new URL.
   */
  private String newUrl;

  /**
   * The current prefix.
   */
  private String currentPrefix;

  /**
   * Indicates if the session is encrypted.
   */
  private boolean encryptedSession;

  /**
   * The current session.
   */
  private Session currentSession;

  /**
   * The current path context.
   */
  private String currentPathContext;

  /**
   * The path parameters.
   */
  private Map<String, String> pathParams;

  /**
   * The query parameters.
   */
  private Set<Entry<String, Collection<String>>> queryParams;

  /**
   * The current prefix with interceptor signal.
   */
  private String currentPrefixWithInterceptorSignal;

  // *************************** Builder Method ***************************

  /**
   * Sets the Feign properties for subsequent request processing.
   *
   * @param properties the Feign properties
   */
  protected void addFeignProperties(final FeignProperties properties) {
    this.properties = properties;
  }

  /**
   * Analyses the given template — resolves session, prefix, parameters,
   * and body.
   *
   * @param template the Feign request template
   */
  protected void performValidationForRequest(final RequestTemplate template) {
    this.template = template;
    cleanAttributeValues();
    buildCurrentPathContext();
    buildCurrentPrefix();
    buildCurrentUrl();
    buildCurrentSession();
    sessionValidate();
    buildParameter();
    buildBodyTemplate();
  }

  // *************************** Get Methods ***************************

  /**
   * Returns the current Feign request template.
   *
   * @return the template
   */
  public RequestTemplate getTemplate() {
    return template;
  }

  /**
   * Returns the Feign properties.
   *
   * @return the properties
   */
  public FeignProperties getProperties() {
    return properties;
  }

  /**
   * Returns {@code true} when properties have been set.
   *
   * @return {@code true} if non-null
   */
  public boolean existsProperties() {
    return (properties != null);
  }

  /**
   * Returns the decoded request body, or {@code null} if absent.
   *
   * @return the body string
   */
  public String getBody() {
    return body;
  }

  /**
   * Returns the manipulated URL.
   *
   * @return the new URL
   */
  public String getNewUrl() {
    return newUrl;
  }

  /**
   * Stores the target URL for later application.
   *
   * @param targetUrl the URL to set
   */
  public void addNewUrl(final String targetUrl) {
    this.newUrl = targetUrl;
  }

  /**
   * Strips the interceptor-signal prefix from the given URL.
   *
   * @param newUrl the URL to clean
   * @return the cleaned URL
   */
  public String validateNewUrl(String newUrl) {
    return newUrl.replace(getCurrentPrefixWithInterceptorSignal(), HttpClientKeys.EMPTY);
  }

  /**
   * Replaces the template's URI with the manipulated URL.
   *
   * @param target the request template to update
   */
  public void updateUrlWithNewContext(RequestTemplate target) {
    String oldUrl = target.url();
    String currentUrl = validateNewUrl(newUrl);
    target.uri(currentUrl);
    logger.info("[FeignClientConfig] - (updateUrlWithNewContext): Updating url({}) with new context({})", oldUrl, newUrl);
  }

  /**
   * Returns the resolved URL prefix.
   *
   * @return the prefix, or an empty string
   */
  public String getCurrentPrefix() {
    return currentPrefix;
  }

  /**
   * Returns {@code true} if a non-blank prefix was detected.
   *
   * @return {@code true} when a prefix is present
   */
  public boolean existsPrefixInUrl() {
    return (currentPrefix != null && !currentPrefix.isBlank());
  }

  /**
   * Returns {@code true} if the resolved session requires encryption.
   *
   * @return {@code true} for encrypted sessions
   */
  public boolean isEncryptedSession() {
    return encryptedSession;
  }

  /**
   * Returns the resolved session for this request.
   *
   * @return the current session
   */
  public Session getCurrentSession() {
    return currentSession;
  }

  /**
   * Delegates to the template's path.
   *
   * @return the raw request path
   */
  public String getCurrentPath() {
    return getTemplate().path();
  }

  /**
   * Returns the path context (path without query string and leading slash).
   *
   * @return the path context
   */
  public String getCurrentPathContext() {
    return currentPathContext;
  }

  /**
   * Returns an unmodifiable set of extracted path parameters.
   *
   * @return path parameter entries
   */
  public Set<Entry<String, String>> getPathParams() {
    if (pathParams == null) {
      pathParams = new HashMap<>();
    }
    return unmodifiableSet(pathParams.entrySet());
  }

  /**
   * Returns {@code true} when path parameters were extracted.
   *
   * @return {@code true} if non-empty
   */
  public boolean existsPathParamsInUrl() {
    return (pathParams != null && !pathParams.isEmpty());
  }

  /**
   * Adds or replaces a path parameter entry.
   *
   * @param key   the parameter key
   * @param value the parameter value
   */
  public void addPathParam(String key, String value) {
    pathParams.put(key, value);
  }

  /**
   * Replaces the URL with a version containing encrypted path parameters.
   *
   * @param currentUrl the URL with encrypted segments
   */
  public void replacePathParamInCurrentUrlByEncrypted(String currentUrl) {
    addNewUrl(currentUrl);
  }

  /**
   * Returns an unmodifiable set of extracted query parameters.
   *
   * @return query parameter entries
   */
  public Set<Entry<String, Collection<String>>> getQueryParams() {
    if (queryParams == null) {
      queryParams = new HashSet<>();
    }
    return unmodifiableSet(queryParams);
  }

  /**
   * Returns {@code true} when query parameters were extracted.
   *
   * @return {@code true} if non-empty
   */
  public boolean existsQueryParamInUrl() {
    Set<Entry<String, Collection<String>>> queryParamsWrapper = getQueryParams();
    return queryParamsWrapper != null && !queryParamsWrapper.isEmpty();
  }

  /**
   * Rebuilds the URL with encrypted query parameters.
   *
   * @param encryptedQueryParams the encrypted query string
   */
  public void replaceQueryParamInCurrentPathByEncrypted(String encryptedQueryParams) {
    String joinUrl = join(HttpClientKeys.QUERY_PARAM_INTERCEPTOR_SIGNAL, currentPathContext, encryptedQueryParams);
    addNewUrl(joinUrl);
  }

  // *************************** Private Methods ***************************

  /**
   * Resets all mutable state before processing a new request.
   */
  private void cleanAttributeValues() {
    this.body = null;
    this.newUrl = null;
    this.currentPrefix = null;
    this.encryptedSession = false;
    this.currentSession = null;
    this.currentPathContext = null;
    this.pathParams = null;
    this.queryParams = null;
  }

  /**
   * Derives the working URL from the template, stripping the leading slash
   * when a prefix is present.
   */
  private void buildCurrentUrl() {
    String url = getTemplate().url();
    if (existsPrefixInUrl()) {
      url = url.substring(1);
    }
    addNewUrl(url);
  }

  /**
   * Extracts the path context — everything before the query string,
   * without the leading slash.
   */
  private void buildCurrentPathContext() {
    currentPathContext = getCurrentPath();
    int interceptorIndex = currentPathContext.indexOf(HttpClientKeys.QUERY_PARAM_INTERCEPTOR_SIGNAL_CHARACTER);
    if (interceptorIndex > -1) {
      currentPathContext = currentPathContext.substring(0, interceptorIndex).trim();
    }
    if (currentPathContext.startsWith(HttpClientKeys.RIGHT_SLASH)) {
      currentPathContext = currentPathContext.substring(1);
    }
  }

  /**
   * Resolves the interceptor prefix from the path context.
   */
  private void buildCurrentPrefix() {
    if (currentPathContext.contains(HttpClientKeys.PREFIX_INTERCEPTOR_SIGNAL)) {
      currentPrefix = currentPathContext.substring(0, currentPathContext.indexOf(HttpClientKeys.PREFIX_INTERCEPTOR_SIGNAL));
      addCurrentPrefixWithInterceptorSignal(currentPrefix);
    } else {
      currentPrefix = HttpClientKeys.EMPTY;
    }
  }

  /**
   * Matches the current prefix against the registered sessions.
   */
  private void buildCurrentSession() {
    currentSession = getProperties().getSessions().stream().filter(s -> s.hasProperties() && s.getProperties().startsWith(getCurrentPrefixWithInterceptorSignal())).findFirst().orElseGet(Session::new);
  }

  /**
   * Determines whether the resolved session requires encryption.
   */
  private void sessionValidate() {
    encryptedSession = !(getProperties().existsUnencryptedSession() && getProperties().getUnencryptedSession().stream().map(Session::getProperties).anyMatch(getCurrentSession()::containsProperties));
  }

  /**
   * Builds both path and query parameter maps.
   */
  private void buildParameter() {
    buildPathParams();
    buildQueryParams();
  }

  /**
   * Builds the path parameters.
   */
  private void buildPathParams() {
    pathParams = new HashMap<>();
    if (existsPrefixInUrl()) {
      String[] splitPath = currentPathContext.split(quote(HttpClientKeys.RIGHT_SLASH));
      for (String s : splitPath) {
        if (!currentSession.getProperties().contains(s)) {
          pathParams.putIfAbsent(s, s);
        }
      }
    }
  }

  /**
   * Builds the query parameters.
   */
  private void buildQueryParams() {
    queryParams = template.queries().entrySet();
  }

  /**
   * Decodes the request body from the template.
   */
  private void buildBodyTemplate() {
    if (template.body() != null) {
      body = new String(template.body(), StandardCharsets.UTF_8);
    }
  }

  /**
   * Returns the prefix concatenated with the interceptor signal.
   *
   * @return the prefixed signal string
   */
  private String getCurrentPrefixWithInterceptorSignal() {
    return currentPrefixWithInterceptorSignal;
  }

  /**
   * Stores the prefix concatenated with the interceptor signal.
   *
   * @param value the raw prefix
   */
  private void addCurrentPrefixWithInterceptorSignal(final String value) {
    assert value != null && !value.isBlank();
    this.currentPrefixWithInterceptorSignal = value.concat(HttpClientKeys.PREFIX_INTERCEPTOR_SIGNAL);
  }
}
