package uk.ac.ebi.pride.web.util.frontier;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: dani
 * Date: 19/11/12
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public class CustomizedFrontierTemplateFilter extends FrontierTemplateFilter {

    //will overwrite it to do not inject header/footer to all pages
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            String url = ((HttpServletRequest)request).getRequestURL().toString();
            //only do injection if does not contain the jnlp
            if (url.contains("jnlp")) chain.doFilter(request, response);
            else super.doFilter(request, response, chain);
        }
    }
}
