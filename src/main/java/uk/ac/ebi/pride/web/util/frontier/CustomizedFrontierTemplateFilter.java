package uk.ac.ebi.pride.web.util.frontier;

import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CustomizedFrontierTemplateFilter extends FrontierTemplateFilter {

  private List<String> exceptions = new LinkedList<>();

  private Resource jsonConfigAuthenticated;

  public synchronized void setJsonConfigAuthenticated(Resource jsonConfigAuthenticated) {
    this.jsonConfigAuthenticated = jsonConfigAuthenticated;
  }

  // will overwrite it to do not inject header/footer to all pages
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {

      String url = ((HttpServletRequest) request).getRequestURL().toString();

      // only do injection if does not contain exceptions
      if (matchesExceptions(url)) {
        chain.doFilter(request, response);
      } else {
        super.doFilter(request, response, chain);
      }
    }
  }

  @Override
  public synchronized Resource getJsonConfig() {
    if (jsonConfigAuthenticated != null) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
        return jsonConfigAuthenticated;
      }
    }

    return super.getJsonConfig();
  }

  public List getExceptions() {
    return exceptions;
  }

  public void setExceptions(List<String> exceptions) {
    this.exceptions = exceptions;
  }

  private boolean matchesExceptions(String url) {

    boolean matches = false;

    Iterator<String> it = exceptions.iterator();

    while (!matches && it.hasNext()) {
      String exception = it.next();
      // ToDo: perhaps allow for wild cards and whole directories to be recognised as exceptions
      matches = url.contains(exception);
    }

    return matches;
  }
}
