package uk.ac.ebi.pride.web.util.frontier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.ebi.pride.web.util.callback.filter.GenericResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Jose A Dianes
 * @author Antonio Fabregat
 *
 */
@SuppressWarnings("UnusedDeclaration")
public class FrontierTemplateFilter implements Filter {

    private static Log log = LogFactory.getLog(FrontierTemplateFilter.class);

    private File jsonConfig;

    public FrontierTemplateFilter(File jsonConfig) {
        this.jsonConfig = jsonConfig;
    }

    public void init(FilterConfig fConfig) throws ServletException {

    }

    @SuppressWarnings("RedundantStringConstructorCall")
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        GenericResponseWrapper wrapper = new GenericResponseWrapper(httpResponse);
        String content = wrapper.getContentType();
        String html = getFrontierTemplate().replace("##contentHTML##", content);
        OutputStream out = httpResponse.getOutputStream();
        out.write(html.getBytes());

        //chain.doFilter(request, response);
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

        BufferedReader reader = new BufferedReader(new FileReader(this.jsonConfig));
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