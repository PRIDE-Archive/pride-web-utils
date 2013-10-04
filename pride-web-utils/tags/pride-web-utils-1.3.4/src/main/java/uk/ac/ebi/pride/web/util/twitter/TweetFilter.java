package uk.ac.ebi.pride.web.util.twitter;

import org.springframework.social.twitter.api.Tweet;

import java.util.Collection;

/**
 * {@code TweetFilter} filters a collections of tweets, this filter should operate on the input collection
 * and return the input collection
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface TweetFilter {

    public Collection<Tweet> filter(Collection<Tweet> tweets);
}
