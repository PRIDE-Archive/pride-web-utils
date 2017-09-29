package uk.ac.ebi.pride.web.util.twitter;

import twitter4j.Status;

/**
 * {@code HyperLinkTweetTextFormatter} scans the tweet text and wrap any hyperlinks with
 * html 'a' tags
 *
 * @author Rui Wang
 * @version $Id$
 */
public class HyperLinkTweetTextFormatter implements TweetTextFormatter {

  public static final String HYPERLINK_PATTERN = "((http|https|ftp|mailto):\\S+)";

  public static String format(Status tweets) {
    return tweets.getText().replaceAll(HYPERLINK_PATTERN, "<a href=\"$1\">link</a>");
  }

}
