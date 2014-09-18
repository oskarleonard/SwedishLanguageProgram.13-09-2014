package com.oskarfransson.swedishlanguageprogram;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LectureTitleFragment extends ListFragment {
	
	private final String TAG = this.getClass().getSimpleName();
	public static final String SHARED_PREF = "MySharedPreferencesHolder";

	//Will monitor if a headline is clicked on
	//The parent who uses this class will need to implement this interface
	//to make sure that clicking a lecture title will lead to something
	OnLectureSelectedListener mCallback;
	
    public interface OnLectureSelectedListener {
        public void onLectureSelected(int LecturePosition);
    }
    
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().getActionBar().setSelectedNavigationItem(1);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i(TAG, "Lecture title onCreate");
		//Create a layout at runtime to hold the titles of the lessons.
		//i will get the titles from the Lecture class in its string[] LectureHeadline
		int Lay = android.R.layout.simple_list_item_1;
		setListAdapter(new ArrayAdapter<String> (getActivity(), Lay, Lecture.LectureHeadLine));
	}
	
    // Called when a Fragment is attached to an Activity
    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        try {
            mCallback = (OnLectureSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onLectureSelected");
        }
    }
    
    /* Notify the parent activity of selected item. 
     * And only open the lesson if the user has finished the 
     * previous lesson's test. The users level is saved in the 
     * shared preference "level". 
     */
    @Override
    public void onListItemClick(ListView l, View v, int LecturePosition, long id) {

    	SharedPreferences settings = this.getActivity().getSharedPreferences(SHARED_PREF, 0);
    	
    	Log.i(TAG, "Lecture title onListItemCLick, SP level = " + settings.getInt("level", -1));
    	
    	if(settings.getInt("level", 0) >= LecturePosition){
    		mCallback.onLectureSelected(LecturePosition);
    	}
    	else
    		Toast.makeText(getActivity(), "Pass the test of all the \nprevious lessions to open \nthis lession", Toast.LENGTH_SHORT).show();
       
    }
    
    
}
