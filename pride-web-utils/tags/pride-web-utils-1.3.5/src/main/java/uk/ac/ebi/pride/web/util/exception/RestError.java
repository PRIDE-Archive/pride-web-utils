package uk.ac.ebi.pride.web.util.exception;

import org.springframework.http.HttpStatus;

/**
 * {@code RestError} represents an exception which can directly be parsed as error output of a RESTful API
 *
 * Please note, you should try to use {@code RestError.Builder} to create an instance of {@code RestError}
 *
 * @author Rui Wang
 * @version $Id$
 */
public class RestError {

    /** HTTP status code, such as: 500, 401 */
    private final HttpStatus status;
    /** Internal error code */
    private final int code;
    /** Internal error message */
    private final String message;
    /** Error message intended for debugging the error */
    private final String developerMessage;
    /** Url for additional information about the error */
    private final String moreInfoUrl;
    /** Original exception */
    private final Throwable throwable;


    public RestError(HttpStatus status, int code, String message, String moreInfoUrl) {
        this(status, code, message, null, moreInfoUrl, null);
    }

    public RestError(HttpStatus status, int code,
                     String message, String developerMessage,
                     String moreInfoUrl, Throwable throwable) {
        if (status == null) {
            throw new IllegalArgumentException("HttpStatus cannot be null");
        }

        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
        this.moreInfoUrl = moreInfoUrl;
        this.throwable = throwable;
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

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public String getMoreInfoUrl() {
        return moreInfoUrl;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestError)) return false;

        RestError restError = (RestError) o;

        if (code != restError.code) return false;
        if (developerMessage != null ? !developerMessage.equals(restError.developerMessage) : restError.developerMessage != null)
            return false;
        if (message != null ? !message.equals(restError.message) : restError.message != null) return false;
        if (moreInfoUrl != null ? !moreInfoUrl.equals(restError.moreInfoUrl) : restError.moreInfoUrl != null)
            return false;
        if (status != restError.status) return false;
        if (throwable != null ? !throwable.equals(restError.throwable) : restError.throwable != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + code;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (developerMessage != null ? developerMessage.hashCode() : 0);
        result = 31 * result + (moreInfoUrl != null ? moreInfoUrl.hashCode() : 0);
        result = 31 * result + (throwable != null ? throwable.hashCode() : 0);
        return result;
    }

    /**
     * Use of Builder design pattern
     */
    public static class Builder {
        private HttpStatus status;
        private int code;
        private String message;
        private String developerMessage;
        private String moreInfoUrl;
        private Throwable throwable;


        public Builder() {
        }

        public Builder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder setCode(int code) {
            this.code = code;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setDeveloperMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }

        public Builder setMoreInfoUrl(String moreInfoUrl) {
            this.moreInfoUrl = moreInfoUrl;
            return this;
        }

        public Builder setThrowable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public RestError build() {
            if (this.status == null) {
                this.status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            return new RestError(this.status, this.code, this.message, this.developerMessage, this.moreInfoUrl, this.throwable);
        }
    }
}
