package uk.ac.ebi.pride.web.util.twitter;

import org.springframework.social.twitter.api.Tweet;

import java.util.Collection;

/**
 * {@code DataSetTweetFilter} filters out any tweet which doesn't contain a list of string
 *
 * @author Rui Wang
 * @version $Id$
 */
public class DataSetTweetFilter implements TweetFilter {
    public final static String DEFAULT_DATASET_FILTER = "dataset";

    private String filterStr;

    public DataSetTweetFilter() {
        this.filterStr = DEFAULT_DATASET_FILTER;
    }

    @Override
    public Collection<Tweet> filter(Collection<Tweet> tweets) {
        for (Tweet tweet : tweets) {
            String tweetText = tweet.getText();
            if (tweetText == null || tweetText.toLowerCase().contains(filterStr)) {
                tweets.remove(tweet);
            }
        }

        return tweets;
    }

    public String getFilterStr() {
        return filterStr;
    }

    public void setFilterStr(String filterStr) {
        this.filterStr = filterStr;
    }
}
