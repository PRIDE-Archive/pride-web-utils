package uk.ac.ebi.pride.web.util.twitter;

import org.springframework.social.twitter.api.Tweet;

import java.util.Collection;
import java.util.Iterator;

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
        Iterator<Tweet> tweetIter = tweets.iterator();

        while(tweetIter.hasNext()) {
            Tweet tweet = tweetIter.next();
            String tweetText = tweet.getText();
            if (tweetText == null || tweetText.toLowerCase().contains(filterStr)) {
                tweetIter.remove();
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
