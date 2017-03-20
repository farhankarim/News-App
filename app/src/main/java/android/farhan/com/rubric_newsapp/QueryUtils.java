package android.farhan.com.rubric_newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving Articles data from USGS.
 */

/**
 * Helper methods related to requesting and receiving Articles data from USGS.
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link Articles} objects.
     */
    public static List<Articles> fetchArticles(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Article}s
        List<Articles> articlesList = extractArticless(jsonResponse);

        // Return the list of {@link Earthquake}s
        return articlesList;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Articles> extractArticless(String articlesJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(articlesJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding articles to
        ArrayList<Articles> articlesArrayList = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the SAMPLE_JSON_RESPONSE string
            JSONObject baseJsonResponse = new JSONObject(articlesJSON);

            // For a given articles, extract the JSONObject associated with the
            // key called "response",
            JSONObject response = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of articles.
            JSONArray articlesArray = response.getJSONArray("results");

            // For each articles in the resilts, create an {@link articles} object
            for (int i = 0; i < articlesArray.length(); i++) {

                // For a given articles, extract the JSONObject at the current location and iterate through the end of response
                JSONObject properties = articlesArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String webTitle = properties.getString("webTitle");

                // Extract the value for the key called "webPublicationDate"
                String webPublicationDate = properties.getString("webPublicationDate");


                // Extract the value for the key called "sectionid"
                String sectionId = properties.getString("sectionId");

                // Extract the value for the key called "webUrl"
                String webUrl = properties.getString("webUrl");

                // Extract the JSONArray associated with the key called "tags",
                // which contains author name
                JSONArray fieldsArray = properties.getJSONArray("tags");

                // For a given articles, extract the JSONObject at the
                // current location and iterate through the end of response
                //which in this case is only 0 so no for loop required
                JSONObject authorDetail = fieldsArray.getJSONObject(0);

                // Extract the value for the key called "webTitle"
                String webTitleAuthor = authorDetail.getString("webTitle");


                // Create a new {@link Articles} object with the magnitude, location, and time
                // from the JSON response.
                //public Articles(String date,String author, String title,String sectionId) {
                Articles articles = new Articles(webPublicationDate, webTitleAuthor, webTitle, sectionId, webUrl);

                // Add the new {@link Articles} to the list of Articless.
                articlesArrayList.add(articles);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Articles JSON results", e);
        }

        // Return the list of Articless
        return articlesArrayList;
    }


}