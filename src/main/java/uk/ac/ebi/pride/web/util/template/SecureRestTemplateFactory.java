package uk.ac.ebi.pride.web.util.template;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Rui Wang
 * @version $Id$
 */
public final class SecureRestTemplateFactory {

    public static RestTemplate getTemplate(String userName, String password) {
        // setup authentication
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);

        // update user summary
        DefaultHttpClient client = new DefaultHttpClient();
        client.setCredentialsProvider(credentialsProvider);

        HttpComponentsClientHttpRequestFactory contextAwareHttpRequestFactory = new PreEmptiveAuthHttpRequestFactory(client);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(contextAwareHttpRequestFactory);
        return restTemplate;
    }
}
