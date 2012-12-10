package uk.ac.ebi.pride.web.util.twitter;

import org.springframework.social.twitter.api.Tweet;

import java.util.Collection;

/**
 * {@code HyperLinkTweetTextFormatter} scans the tweet text and wrap any hyperlinks with
 * html 'a' tags
 *
 * @author Rui Wang
 * @version $Id$
 */
public class HyperLinkTweetTextFormatter implements TweetTextFormatter {

    public static final String HYPERLINK_PATTERN = "((http|https|ftp|mailto):\\S+)";

    @Override
    public Collection<Tweet> format(Collection<Tweet> tweets) {
        for (Tweet tweet : tweets) {
            formatTweetText(tweet);
        }
        return tweets;
    }

    private void formatTweetText(Tweet tweet) {
        String tweetText = tweet.getText();
        tweetText = tweetText.replaceAll(HYPERLINK_PATTERN, "<a href=\"$1\">$1</a>");
        tweet.setText(tweetText);
    }
}
