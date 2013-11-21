package uk.ac.ebi.pride.web.util.exception;

/**
 * A {@link RestErrorResolver} resolves an exception and produces a {@link RestError} instance that
 * can be used to render a REST error representation to the response body
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface RestErrorResolver {

    /**
     * Returns a {@code RestError} instance to render as the response body based on the given exception
     *
     * @param ex    the exception that was thrown during handler exception
     * @return a {@code RestError} instance
     */
    RestError resolveError(Exception ex);
}
