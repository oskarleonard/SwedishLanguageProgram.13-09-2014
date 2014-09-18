package com.oskarfransson.swedishlanguageprogram;
/**
 * This class holds the lessons. 
 * It will open the database to:
 * 			Retrieve the path to the audio file(which will be sent to the mp3 class ass a bundle)
 * 			Get the content which will be displayed in a TextView
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

public class LessonIntroContainer extends Fragment{
	
	View myView;
	TableLayout table;
	boolean saved;
	MyDatabase db;
	int lectureNumber;
	String LectureNR, LectureContent, AudioPath1, AudioPath2; //These strings will get their data from the database
	
	private final String TAG = this.getClass().getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreateView");
		
		myView =  inflater.inflate(R.layout.startoverviewfragment_fragment, container, false);	
		
		//This int comes from the LessonSwipeViews. It is used to know which lesson should be displayed. 
		lectureNumber = 0;
		
		CreateGUI(savedInstanceState);
		//14-08-10 Left of here. Need to create an obj of this fragment in the lessonSwipeView. I.e. change the one that is already there.
		return myView;
	}
	
	/**Create the GUI, i want to try to do the gui for the lesson mostly by java code.
	 * 
	 */
	private void CreateGUI(Bundle savedInstanceState){
		Log.i(TAG, "CreateGUI");
		table = (TableLayout)myView.findViewById(R.id.startoverviewfragmentLayoutId);
		
		//Get data from the database
		GetDatabaseData();
		
		
		//1st ROW - Add the MP3 player
		TableRow tr1 = new TableRow(myView.getContext());
		tr1.setId(1);
		
		/**The below if statement make sure that no extra fragment will be added to the table row.
		 * First i see if the fragment has been created, if not than create. The else if will be 
		 * evoked when screen rotate or coming back from the menu bar, than i will get the old fragment
		 * and not create a new one. This makes sure that everything on that page is saved. 
		 */
		if (isTagInBackStack("cfi") == false){
			Log.i(TAG, "isTagInBackStack false");
			MyMediaPlayer cfi = new MyMediaPlayer();
			Bundle args = new Bundle();
			args.putString("PATH1", AudioPath1);
			args.putString("PATH2", AudioPath2);
			cfi.setArguments(args);
			FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
			transaction.add(tr1.getId(), cfi, "cfi");
			transaction.addToBackStack("cfi");
			transaction.commit();
		}
		else if (isTagInBackStack("cfi") == true){
			Log.i(TAG, "isTagInBackStack true");
			FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
			transaction.replace(tr1.getId(), getChildFragmentManager().findFragmentByTag("cfi"));
			transaction.commit();
			
		}
		table.addView(tr1);
		Log.i(TAG, "CreateGUI add row 2");
		
		//2nd ROW - Put the text
		TableRow tr2 = new TableRow(myView.getContext());
        TextView tvC = new TextView(myView.getContext());
        tvC.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        tvC.setText(LectureContent);
        tr2.addView(tvC);
		table.addView(tr2);
		
	}

	private void GetDatabaseData() {
		// TODO Auto-generated method stub
		
		db = new MyDatabase(this.getActivity());
		ArrayList<HashMap<String, String>> allLectures = db.getAllLectures();
		
		if(allLectures.size() != 0){
			LectureNR = allLectures.get(lectureNumber).get("LectureNR");
			LectureContent = allLectures.get(lectureNumber).get("LectureContent");
			AudioPath1 = allLectures.get(lectureNumber).get("AudioPath1");
			AudioPath2 = allLectures.get(lectureNumber).get("AudioPath2");
		}
		Log.i(TAG, "db.close, AudioPath = " + AudioPath1 + AudioPath2);
		db.close();
		//End of Database
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
	}
	
	private boolean isTagInBackStack(String tag){
		
		Log.i(TAG, "isTagInBackStack() LessonSwipeViews");
		int x;
		boolean toReturn = false;
		int backStackCount = getChildFragmentManager().getBackStackEntryCount();
		Log.i(TAG, "backStackCount = " + backStackCount);
		
		for (x = 0; x < backStackCount; x++){
			Log.i(TAG, "Iter = " + x +" "+ getChildFragmentManager().getBackStackEntryAt(x).getName());
			if (tag == getChildFragmentManager().getBackStackEntryAt(x).getName()){
				toReturn = true;
				
			}
			
		}
		
		Log.i(TAG, "isTagInBackStack() End, toReturn = " + toReturn);
		return toReturn;
	}
}
