import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import uk.ac.ebi.pride.web.util.twitter.TwitterService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by tobias on 01/03/2017.
 */
public class TwitterTest {
  private static final Logger logger = LoggerFactory.getLogger(TwitterTest.class);

  @Test
  public void testGetTimeline() throws Exception {
    TwitterService twitterService = new TwitterService("nhYZqNgFHmy1Ock5vV0xg",
        "GBRq77hm3g4Tju7n77tM9ayEozogL0Z48gzPh3nIQaI",
        "135937416-PAQMoGMGIoBPWs21jrzEJCFiMf67UEkHHFocYg8s",
        "hSkClsh7wmYWfwUxyjGV0oB3lbyY01uvqeIvlA4BhqI");
    twitterService.queryForTweets(5).forEach(status -> System.out.println(status.getText()));
    twitterService.queryForTweets(5).forEach(status -> logger.info(status.getText()));
  }
/*    Twitter twitter = TwitterFactory.getSingleton();
    twitter.setOAuthConsumer("nhYZqNgFHmy1Ock5vV0xg", "GBRq77hm3g4Tju7n77tM9ayEozogL0Z48gzPh3nIQaI");
    RequestToken requestToken = twitter.getOAuthRequestToken();
    AccessToken accessToken = null;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while (null == accessToken) {
      System.out.println("Open the following URL and grant access to your account:");
      System.out.println(requestToken.getAuthorizationURL());
      System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
      String pin = br.readLine();
      try{
        if(pin.length() > 0){
          accessToken = twitter.getOAuthAccessToken(requestToken, pin);
        }else{
          accessToken = twitter.getOAuthAccessToken();
        }
      } catch (TwitterException te) {
        if(401 == te.getStatusCode()){
          System.out.println("Unable to get the access token.");
        }else{
          te.printStackTrace();
        }
      }
    }
    //persist to the accessToken for future reference.
    storeAccessToken(Integer.parseInt("" + twitter.verifyCredentials().getId()) , accessToken);
    System.exit(0);
  }
  private static void storeAccessToken(int useId, AccessToken accessToken){
    logger.info("ACCESSION TOKEN, getToken: " + accessToken.getToken());
    logger.info("ACCESSION TOKEN, getTokenSecret: " + accessToken.getTokenSecret());
  }*/
}
