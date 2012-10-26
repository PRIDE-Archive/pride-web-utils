package uk.ac.ebi.pride.web.util.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Default {@code RestErrorResolver} implementation that converts discovered exceptions to {@link RestError} instances
 *
 * @author Rui Wang
 * @version $Id$
 * @see RestErrorResolver
 * @see RestError
 */
public class DefaultRestErrorResolver implements RestErrorResolver {
    private static final Logger logger = LoggerFactory.getLogger(DefaultRestErrorResolver.class);

    /** REST error mappings, this mapping acts as dictionary */
    private Map<Class<? extends  Throwable>, RestError> restErrorMappings;

    /** default more info url, used only when {@code RestError} doesn't contain a MoreInfoUrl*/
    private String defaultMoreInfoUrl;

    /**
     * Resolve an exception into a {@code RestError} instance
     *
     * @param ex    the exception that was thrown during handler exception
     * @return  an instance of {@code RestError}
     */
    @Override
    public RestError resolveError(Exception ex) {
        logger.debug("Resolving error: {} with {}", ex.getClass(), ex.getMessage());
        RestError restErrorTemplate = restErrorMappings.get(ex.getClass());
        if (restErrorTemplate == null) {
            return null;
        }

        return copyRestErrorTemplate(restErrorTemplate, ex);
    }

    /**
     * Set a map of mappings between exception classes and rest error instances
     * Please note: these rest error instances are only going to be used as template,
     * They will not be modified.
     *
     * @param restErrorMappings input maps of rest error mappings
     */
    public void setRestErrorMappings(Map<Class<? extends  Throwable>, RestError> restErrorMappings) {
        this.restErrorMappings = restErrorMappings;
    }

    /**
     * Set the default moreInfoUrl, this will be used if the moreInfoUrl is not set
     *
     * @param defaultMoreInfoUrl    default more information URL
     */
    public void setDefaultMoreInfoUrl(String defaultMoreInfoUrl) {
        this.defaultMoreInfoUrl = defaultMoreInfoUrl;
    }

    /**
     * Make a copy of rest error
     * @param restErrorTemplate rest error to be copied
     * @param ex exception will be used as throwable parameter,
     *           and also if the developer message is null, exception message will be used instead
     * @return  a new instance of the rest error
     */
    private RestError copyRestErrorTemplate(RestError restErrorTemplate, Exception ex) {
        RestError.Builder builder = new RestError.Builder();
        return builder.setStatus(restErrorTemplate.getStatus())
                .setCode(restErrorTemplate.getCode())
                .setMessage(restErrorTemplate.getMessage())
                .setDeveloperMessage(restErrorTemplate.getDeveloperMessage() == null ? ex.getMessage() : restErrorTemplate.getDeveloperMessage())
                .setMoreInfoUrl(restErrorTemplate.getMoreInfoUrl() == null ? defaultMoreInfoUrl : restErrorTemplate.getMoreInfoUrl())
                .setThrowable(ex).build();
    }
}
