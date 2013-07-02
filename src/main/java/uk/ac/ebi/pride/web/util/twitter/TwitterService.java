package uk.ac.ebi.pride.web.util.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

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

    private final Set<TweetFilter> tweetFilters = new HashSet<TweetFilter>();

    private final Set<TweetTextFormatter> tweetTextFormatters = new HashSet<TweetTextFormatter>();

    public TwitterService(String consumerKey, String consumerSecret,
                          String accessToken, String accessTokenSecret) {
        this(consumerKey, consumerSecret, accessToken, accessTokenSecret, null, null);
    }

    public TwitterService(String consumerKey, String consumerSecret,
                          String accessToken, String accessTokenSecret,
                          Collection<TweetTextFormatter> tweetTextFormatters) {
        this(consumerKey, consumerSecret, accessToken, accessTokenSecret, null, tweetTextFormatters);
    }

    public TwitterService(String consumerKey, String consumerSecret,
                          String accessToken, String accessTokenSecret,
                          Collection<TweetFilter> tweetFilters,
                          Collection<TweetTextFormatter> tweetTextFormatters) {

        this.twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);

        if (tweetFilters != null && !tweetFilters.isEmpty()) {
            this.tweetFilters.addAll(tweetFilters);
        }

        if (tweetTextFormatters != null && !tweetTextFormatters.isEmpty()) {
            this.tweetTextFormatters.addAll(tweetTextFormatters);
        }
    }

    public List<Tweet> queryForTweets() {
        // get all the tweets of all the twitter account
        List<Tweet> tweets = getTweetsByTimeLine();

        // filter out unwanted tweets
        filterTweets(tweets);

        // sort tweets according to time
        sortTweets(tweets);

        // format tweet messages
        formatTweetText(tweets);

        return tweets;
    }

    private List<Tweet> getTweetsByTimeLine() {
        List<Tweet> tweets = new ArrayList<Tweet>();

        TimelineOperations timelineOperations = twitter.timelineOperations();
        tweets.addAll(timelineOperations.getHomeTimeline());

        return tweets;
    }

    private void sortTweets(List<Tweet> tweets) {
        Collections.sort(tweets, new TweetComparator());
    }

    private void filterTweets(List<Tweet> tweets) {
        for (TweetFilter tweetFilter : tweetFilters) {
            tweetFilter.filter(tweets);
        }
    }

    private void formatTweetText(List<Tweet> tweets) {
        for (TweetTextFormatter tweetTextFormatter : tweetTextFormatters) {
            tweetTextFormatter.format(tweets);
        }
    }

    private static class TweetComparator implements Comparator<Tweet> {

        @Override
        public int compare(Tweet t1, Tweet t2) {
            return t2.getCreatedAt().compareTo(t1.getCreatedAt());
        }
    }
}
