package uk.ac.ebi.pride.web.util.frontier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Jose A Dianes
 * @author Antonio Fabregat
 *
 * Have a look here: http://www.oracle.com/technetwork/java/filters-137243.html
 * at the section 'Programming Customized Requests and Responses'
 *
 */
@SuppressWarnings("UnusedDeclaration")
public class FrontierTemplateFilter implements Filter {

    private static Log log = LogFactory.getLog(FrontierTemplateFilter.class);

    private Resource jsonConfig;

    public void setJsonConfig(Resource jsonConfig) {
        this.jsonConfig = jsonConfig;
    }

    public void init(FilterConfig fConfig) throws ServletException {

    }

    @SuppressWarnings("RedundantStringConstructorCall")
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // get a writer to write into the response output
        PrintWriter out = response.getWriter();

        // create a char wrapper to the response
        CharResponseWrapper wrapper = new CharResponseWrapper((HttpServletResponse) response);

        // do the filter in order to get the response into the wrapper
        chain.doFilter(request, wrapper);

        // get the frontier template and write it together with the response into a char writer
        CharArrayWriter caw = new CharArrayWriter();
        String frontierTemplate = getFrontierTemplate();
        caw.write(frontierTemplate.substring(0, frontierTemplate.indexOf("##contentHTML##")-1));
        caw.write(wrapper.toString()); // insert the response (i.e. the JSP)
        caw.write(frontierTemplate.substring(frontierTemplate.indexOf("##contentHTML##")+15, frontierTemplate.length()));

        // put the char writer content into the response/wrapper output
        response.setContentLength(caw.toString().length());
        out.write(caw.toString());

        out.close();

    }

    public void destroy() {}

    private String getFrontierTemplate() throws IOException {
        URL url = new URL("http://wwwdev.ebi.ac.uk/frontier/template-service/templates/services/web.html");
        // make post mode connection
        URLConnection urlc = url.openConnection();
        urlc.setDoOutput(true);
        urlc.setAllowUserInteraction(false);
        OutputStreamWriter wr = new OutputStreamWriter(urlc.getOutputStream());
        wr.write(getWebConfigurationJSON());
        wr.flush();

        // retrieve result
        BufferedReader br1 = new BufferedReader(new InputStreamReader(urlc.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br1.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br1.close();
        return sb.toString();
    }

    private String getWebConfigurationJSON() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(this.jsonConfig.getFile()));
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        return stringBuilder.toString();
    }

}