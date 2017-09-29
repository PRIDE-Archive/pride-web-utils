package uk.ac.ebi.pride.web.util.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.*;

/**
 * {@code TwitterService} provides a service implementation for retrieving tweeter feeds
 *
 * @author Rui Wang
 * @version $Id$
 */
public class TwitterService {

    private static final Logger logger = LoggerFactory.getLogger(TwitterService.class);

    private final Twitter twitter;

    private final Set<TweetFilter> tweetFilters = new HashSet();

    public TwitterService(String consumerKey, String consumerSecret,
                          String accessToken, String accessTokenSecret) {
        this(consumerKey, consumerSecret, accessToken, accessTokenSecret, null);
    }

    public TwitterService(String consumerKey, String consumerSecret,
                          String accessToken, String accessTokenSecret,
                          Collection<TweetFilter> tweetFilters) {
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        AccessToken accessTokenTwitter = new AccessToken(accessToken, accessTokenSecret);
        twitter.setOAuthAccessToken(accessTokenTwitter);
        if (tweetFilters != null && !tweetFilters.isEmpty()) {
            this.tweetFilters.addAll(tweetFilters);
        }
    }

    public List<Status> queryForTweets(int numberOfTweets) {
        List<Status> tweets = getTweetsByTimeLine(numberOfTweets);
        filterTweets(tweets);
        sortTweets(tweets);
        return tweets;
    }

    private List<Status> getTweetsByTimeLine(int numberOfTweets) {
        List<Status> result = new ArrayList<>(numberOfTweets);
        ResponseList<Status> statuses = null;
        try {
            statuses = twitter.getUserTimeline();
        } catch (TwitterException e) {
            logger.error("Problem getting Twitter user timeline: ", e);
        }
        if (statuses!=null) {
            this.sortTweets(statuses);
            Iterator<Status> iterator = statuses.iterator();
            for (int i=0; iterator.hasNext() && i<statuses.size() && i <= numberOfTweets; i++) {
                result.add(iterator.next());
            }
        }
        return result;
    }

    private void sortTweets(List<Status> statuses) {
        statuses.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
    }

    private void sortTweets(ResponseList<Status> statuses) {
        statuses.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
    }

    private void filterTweets(List<Status> tweets) {
        for (TweetFilter tweetFilter : tweetFilters) {
            tweetFilter.filter(tweets);
        }
    }

    public String formatTweetText(Status tweet) {
        return HyperLinkTweetTextFormatter.format(tweet);
    }
}
