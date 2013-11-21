package uk.ac.ebi.pride.web.util.exception;

import org.springframework.core.convert.converter.Converter;

/**
 * A {@code RestErrorConverter} is an intermediate 'bridge' component, it converts a {@link RestError} object
 * into another object that is better suited for HTTP response rendering
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface RestErrorConverter<T> extends Converter<RestError, T> {

    /**
     * Converts the {@link RestError} instance into an object that will be used by an
     * {@link org.springframework.http.converter.HttpMessageConverter} to render the response body
     *
     * @param restError rest error
     * @return  an object for HTTP response rendering
     */
    T convert(RestError restError);

}
