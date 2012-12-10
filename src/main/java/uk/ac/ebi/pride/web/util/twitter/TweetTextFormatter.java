package uk.ac.ebi.pride.web.util.twitter;

import org.springframework.social.twitter.api.Tweet;

import java.util.Collection;

/**
 * {@code TweetTextFormatter} is an interface that format the tweet text of a collection of tweets
 * the original tweets will be modified, and the input collection will be also returns
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface TweetTextFormatter {

    public Collection<Tweet> format(Collection<Tweet> tweets);
}
