package android.farhan.com.rubric_newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ArticleAdapter extends ArrayAdapter<Articles> {

    /**
     * Constructs a new {@link ArticleAdapter}.
     *
     * @param context  of the app
     * @param articles is the list of articles, which is the data source of the adapter
     */
    public ArticleAdapter(Context context, List<Articles> articles) {
        super(context, 0, articles);
    }

    /**
     * Returns a list item view that displays information about the article at the given position
     * in the list of articles.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);
        }


        // Find the article at the given position in the list of articles
        Articles currentArticles = getItem(position);

        // Find and set the TextView with view ID tvTitle
        TextView titleView = (TextView) listItemView.findViewById(R.id.tvTitle);
        titleView.setText(currentArticles.getTitle());

        // Find and set the TextView with view ID tvAuthor
        TextView authorView = (TextView) listItemView.findViewById(R.id.tvAuthor);
        authorView.setText(currentArticles.getAuthor());

        // Find and set the TextView with view ID tvDate
        TextView dateView = (TextView) listItemView.findViewById(R.id.tvDate);
        dateView.setText(DateFormatter(currentArticles.getDate()));

        // Find and set the TextView with view ID tvSectionID
        TextView sectionIdView = (TextView) listItemView.findViewById(R.id.tvSectionID);
        sectionIdView.setText("Section: " + currentArticles.getSectionId());

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /*
    * Created a seperate method to format the date according to my needs to dd/MMM/yyyy
    * */
    private String DateFormatter(String date) {
        //trims the date and time
        String dateBeforeFormat = "";
        //removes all the characters that occur after the character"T"
        dateBeforeFormat = date.substring(0, date.indexOf("T"));
        SimpleDateFormat dateBeforeFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = null;
        try {
            dateObj = dateBeforeFormatter.parse(dateBeforeFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateAfterFormatter = new SimpleDateFormat("dd/MMM/yyyy");
        //save the formatted date in dateBeforeFormat and return it to the view
        dateBeforeFormat = dateAfterFormatter.format(dateObj);

        return dateBeforeFormat;
    }


}