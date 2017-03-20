package android.farhan.com.rubric_newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of articles by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ArticlesLoader extends AsyncTaskLoader<List<Articles>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = ArticlesLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link ArticlesLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public ArticlesLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread to perform the network request
     * parse the response, and extract a list of articles
     */
    @Override
    public List<Articles> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // P earthquakes.
        List<Articles> articlesList = QueryUtils.fetchArticles(mUrl);
        return articlesList;
    }
}