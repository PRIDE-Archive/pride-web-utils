package uk.ac.ebi.pride.web.util.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Renders a response with a RESTful representation based on the error format discussed in
 *
 * <a href="http://www.stormpath.com/blog/spring-mvc-rest-exception-handling-best-practices-part-1">
 *     Spring MVC Rest Exception Handling Best Practices.</a>
 * <p/>
 *
 * <h2>JSON Rendering</h2>
 *
 * This implementation comes pre-configured with Spring's typical default
 * {@link org.springframework.http.converter.HttpMessageConverter} instances; JSON will be enabled automatically if Jackson is in the classpath.  If you
 * want to match the JSON representation shown in the article above (recommended) but do not want to use Jackson
 * (or the Spring's default Jackson config), you will need to
 * {@link #setMessageConverters(org.springframework.http.converter.HttpMessageConverter[]) configure} a different
 * JSON-capable {@link org.springframework.http.converter.HttpMessageConverter}.
 *
 * @author Rui Wang
 * @version $Id$
 */
public class RestExceptionHandler extends AbstractHandlerExceptionResolver implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    private HttpMessageConverter<?>[] messageConverters = null;

    private RestErrorResolver errorResolver;

    private RestErrorConverter errorConverter;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        RestError restError = errorResolver.resolveError(ex);
        if (restError == null) {
            return null;
        }

        ModelAndView mav = null;

        try {
            mav = getModelAndView(new ServletWebRequest(request, response), restError);
        } catch (Exception e) {
            logger.error("Acquiring ModelAndView for Exception [" + ex + "] resulted in an exception.", e);
        }

        return mav;
    }

    private ModelAndView getModelAndView(ServletWebRequest webRequest, RestError restError) throws Exception {
        // set response http status code
        addResponseStatus(webRequest, restError);

        Object body = restError;

        if (errorConverter != null) {
            body = errorConverter.convert(restError);
        }

        // build response body
        return buildResponseBody(body, webRequest);
    }

    @SuppressWarnings("unchecked")
    private ModelAndView buildResponseBody(Object body, ServletWebRequest webRequest) throws IOException {
        HttpInputMessage httpInputMessage = new ServletServerHttpRequest(webRequest.getRequest());

        // get accepted media type from request
        List<MediaType> acceptedMediaTypes = httpInputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }

        MediaType.sortByQualityValue(acceptedMediaTypes);

        HttpOutputMessage httpOutputMessage = new ServletServerHttpResponse(webRequest.getResponse());

        Class<?> bodyType = body.getClass();

        for (MediaType acceptedMediaType : acceptedMediaTypes) {
            for (HttpMessageConverter messageConverter : messageConverters) {
                if (messageConverter.canWrite(bodyType, acceptedMediaType)) {
                    messageConverter.write(body, acceptedMediaType, httpOutputMessage);
                    // This is to let Spring know the view has already been rendered
                    return new ModelAndView();
                }
            }
        }

        return null;
    }

    /**
     * Add response HTTP status code
     * @param webRequest   request represent both incoming request and outgoing response
     * @param restError rest error
     */
    private void addResponseStatus(ServletWebRequest webRequest, RestError restError) {
        if (!WebUtils.isIncludeRequest(webRequest.getRequest())) {
            webRequest.getResponse().setStatus(restError.getStatus().value());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(messageConverters, "HttpMessageConverters cannot be null");
        Assert.isTrue(messageConverters.length > 0, "HttpMessageConverters cannot be empty");
        Assert.notNull(errorResolver, "RestErrorResolver cannot be null");
    }

    public void setMessageConverters(HttpMessageConverter<?>[] messageConverters) {
        this.messageConverters = messageConverters;
    }

    public void setErrorResolver(RestErrorResolver errorResolver) {
        this.errorResolver = errorResolver;
    }

    public void setErrorConverter(RestErrorConverter errorConverter) {
        this.errorConverter = errorConverter;
    }
}
