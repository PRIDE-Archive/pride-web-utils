package uk.ac.ebi.pride.web.util.twitter;

import twitter4j.Status;

import java.util.Collection;
import java.util.stream.Collectors;

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
    public Collection<Status> filter(Collection<Status> tweets) {
        return tweets.stream()
                .filter(status -> status.getText()!=null && status.getText().toLowerCase().contains(filterStr))
                .collect(Collectors.toList());
    }

    public String getFilterStr() {
        return filterStr;
    }

    public void setFilterStr(String filterStr) {
        this.filterStr = filterStr;
    }
}
