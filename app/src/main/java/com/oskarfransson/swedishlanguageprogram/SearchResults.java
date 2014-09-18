package com.oskarfransson.swedishlanguageprogram;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SearchResults extends Activity{
	
	private TextView txtResults;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
 
        txtResults = (TextView) findViewById(R.id.txtResults);
 
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }
 

 
    /**
     * Handling intent data
     */
    private void doMySearch(String query) {
        /**
         * Use this query to display search results like
         * 1. Getting the data from SQLite and showing in listview
         * 2. Making webrequest and displaying the data
         * for now, just show the result in one textView
         */
    	txtResults.setText("Search Query: " + query);
    }
}
