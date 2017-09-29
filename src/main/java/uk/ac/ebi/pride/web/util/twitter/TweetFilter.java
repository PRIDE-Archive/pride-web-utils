package uk.ac.ebi.pride.web.util.twitter;

import twitter4j.Status;

import java.util.Collection;

/**
 * {@code TweetFilter} filters a collections of tweets, this filter should operate on the input collection
 * and return the input collection
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface TweetFilter {

    public Collection<Status> filter(Collection<Status> tweets);
}
