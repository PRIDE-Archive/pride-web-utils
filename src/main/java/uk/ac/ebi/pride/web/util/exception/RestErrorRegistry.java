package uk.ac.ebi.pride.web.util.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.social.InternalServerErrorException;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Predefined RESTful error registry
 *
 * @author Rui Wang
 * @version $Id$
 */
public enum RestErrorRegistry {

    HTTP_MESSAGE_NOT_READABLE(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST, 10000, "error.http.message", null),
    MISSING_SERVLET_REQUEST_PARAMETER(MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST, 10001, "error.missing.parameter", null),
    MISMATCH_TYPE(TypeMismatchException.class, HttpStatus.BAD_REQUEST, 10002, "error.type.mismatch", null),
    NO_HANDLER_FOUND_METHOD(NoHandlerFoundException.class, HttpStatus.NOT_FOUND, 10003, "error.no.such.method", null),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED(HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED, 10004, "error.unsupported.method", null),
    HTTP_MEDIA_TYPE_NOT_ACCEPTABLE(HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE, 10005, "error.media.type.unacceptable", null),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED(HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE, 10006, "error.unsupported.media.type", null),
    ACCESS_DENY_EXCEPTION(AccessDeniedException.class, HttpStatus.UNAUTHORIZED, 10007, "error.access.denied", null),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST, 10008, "error.request.argument.validation", null),
    RESOURCE_NOT_FOUND_EXCEPTION(ResourceNotFoundException.class, HttpStatus.NOT_FOUND, 10009, "error.resource.not.found", null),
    INTERNAL_SERVER_ERROR(InternalServerErrorException.class, HttpStatus.INTERNAL_SERVER_ERROR, 10010, "error.internal.server.error", null);

    private Class<? extends Exception> exceptionClass;
    private HttpStatus status;
    private int code;
    private String message;
    private String moreInfoUrl;

    private RestErrorRegistry(Class<? extends Exception> exceptionClass,
                              HttpStatus status,
                              int code,
                              String message,
                              String moreInfoUrl) {
        this.exceptionClass = exceptionClass;
        this.status = status;
        this.code = code;
        this.message = message;
        this.moreInfoUrl = moreInfoUrl;
    }

    public Class<? extends Exception> getExceptionClass() {
        return exceptionClass;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMoreInfoUrl() {
        return moreInfoUrl;
    }

    public static Map<Class<? extends Throwable>, RestError> getRestErrorMappings() {
        Map<Class<? extends Throwable>, RestError> restErrorMappings = new LinkedHashMap<Class<? extends  Throwable>, RestError>();

        for (RestErrorRegistry restErrorRegistry : RestErrorRegistry.values()) {
            restErrorMappings.put(restErrorRegistry.getExceptionClass(),
                    new RestError(restErrorRegistry.getStatus(), restErrorRegistry.getCode(),
                                  restErrorRegistry.getMessage(), restErrorRegistry.getMoreInfoUrl()));
        }

        return restErrorMappings;
    }

    public static RestError getRestErrorByClass(Class<? extends Throwable> clazz) {
        for (RestErrorRegistry restErrorRegistry : RestErrorRegistry.values()) {
            if (restErrorRegistry.exceptionClass.isAssignableFrom(clazz)) {
                return new RestError(restErrorRegistry.getStatus(), restErrorRegistry.getCode(),
                        restErrorRegistry.getMessage(), restErrorRegistry.getMoreInfoUrl());
            }
        }

        return null;
    }
}
