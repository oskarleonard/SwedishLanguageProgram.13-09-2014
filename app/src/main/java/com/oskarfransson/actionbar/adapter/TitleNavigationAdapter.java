package com.oskarfransson.actionbar.adapter;

import com.oskarfransson.actionbar.spinner.SpinnerNavItem;
import com.oskarfransson.swedishlanguageprogram.R;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleNavigationAdapter extends BaseAdapter{
	
	private final String TAG = this.getClass().getSimpleName();
	
    private ImageView imgIcon;
    private TextView txtTitle;
    private ArrayList<SpinnerNavItem> spinnerNavItem;
    private Context context;
 
    public TitleNavigationAdapter(Context context,
            ArrayList<SpinnerNavItem> spinnerNavItem) {
        this.spinnerNavItem = spinnerNavItem;
        this.context = context;
        
        Log.i(TAG, "TitleNavigationAdapter Constructor");
    }
 
    @Override
    public int getCount() {
    	/*Since i want the first item to represent the title for my spinner
    	 * and dont hold any event i need to shorten my list by one. 
    	 */
        return spinnerNavItem.size();
    }
 
    @Override
    public Object getItem(int index) {
        return spinnerNavItem.get(index);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_items, null);
        }
         
        imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
        txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
         
        imgIcon.setImageResource(spinnerNavItem.get(position).getIcon());
        //imgIcon.setVisibility(View.GONE);
        txtTitle.setText(spinnerNavItem.get(position).getTitle());
        return convertView;
    }
 
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_items, null);
        }
        
        Log.i(TAG, "This is the value of position int: " + position);
        

            imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
            txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
             
            imgIcon.setImageResource(spinnerNavItem.get(position).getIcon());       
            txtTitle.setText(spinnerNavItem.get(position).getTitle());

        
        return convertView;

        
        
    }
    
 
}

