package uk.ac.ebi.pride.web.util.frontier;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dani
 * Date: 19/11/12
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 *
 * UPDATE: jadianes - added 'exceptions'
 */
public class CustomizedFrontierTemplateFilter extends FrontierTemplateFilter {

    private List<String> exceptions = new LinkedList();

    //will overwrite it to do not inject header/footer to all pages
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            String url = ((HttpServletRequest)request).getRequestURL().toString();
            //only do injection if does not contain exceptions
            if (matchesExceptions(url))
                chain.doFilter(request, response);
            else
                super.doFilter(request, response, chain);
        }
    }

    public List getExceptions() {
        return exceptions;
    }

    public void setExceptions(List exceptions) {
        this.exceptions = exceptions;
    }

    private boolean matchesExceptions(String url) {

        boolean matches = false;

        Iterator<String> it = exceptions.iterator();

        while (!matches && it.hasNext()) {
            String exception = it.next();
            matches = url.contains(exception);
        }

        return matches;
    }
}
