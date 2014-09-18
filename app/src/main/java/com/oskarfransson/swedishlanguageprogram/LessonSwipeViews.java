package com.oskarfransson.swedishlanguageprogram;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The child fragment is no different than any other fragment other than it is now being maintained by
 * a child FragmentManager.
 * 
 */
public class LessonSwipeViews extends Fragment {
	

	public static final String SHARED_PREF = "MySharedPreferencesHolder";

	/*Two Bundle values, position for the current possition in the page swiper. 
	 * The page swiper will contain 3 swipes (introduction, grammar, text)
	 * selectedLessonNr will contain the selected lesson, so i will load 
	 * the correct content from the database to the lesson.
	 */
    public static final String POSITION_KEY = "FragmentPositionKey";
    private int position;
    public static final String SELECTED_LESSON_NR = "selectedLessonNr";
    private int selectedLessonNr;
    //
    
	
    public static final String TAG = "LessonSwipeViews";
    
    //Retrieve the bundle so it will stay consistent when swiping.
    public static LessonSwipeViews newInstance(Bundle args) {
        LessonSwipeViews fragment = new LessonSwipeViews();
        fragment.setArguments(args);
        return fragment;
    }
    

    /*
     * This is where i set the text for the specified lesson. I will retrive the lessons from a database.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    	View root = inflater.inflate(R.layout.fragment_child, container, false);
        position = getArguments().getInt(POSITION_KEY);
        selectedLessonNr = getArguments().getInt(SELECTED_LESSON_NR);
        
        //Keep track of if user has completed the lesson before. 
        SharedPreferences settings = this.getActivity().getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("level", 3);
        editor.commit();
        
        
        Log.i(TAG, "LessonSwipeViews selectedLessonNr is " + selectedLessonNr + " and position = " + position);

        loadLesson(selectedLessonNr, position);
        
        
        return root;
    }
    
    //load the lesson and return that view
    private void loadLesson(int selectedLessonNr, int position){
    	/**
    	 * TODO Take values from the database and pass them to the ChildFragmentView as bundles. EX. take the path to the audio
    	 * and the lesson text as a string. Than use these values inside the ChildFragmentViews bundle getArguments().
    	 */
    	Log.i(TAG, "loadLesson Start");
    	//This Bundle will pass the Position to the MP3 player so i load the correct audio file from the database
    	Bundle args = new Bundle();
    	
    	FragmentTransaction transaction;
        //TODO Need to get the text from a database
        switch (selectedLessonNr){ //Load data for the selected lesson, lesson 0 is the first lesson
        case 0: // Lesson 1
            switch (position){     // Load data for the selected swipe view 0 is the first view
            case 0:
            	/* VERY IMPORTANT TO USE getChildFragmentManager() And not normal!!. 
            	 * Otherwise will the fragment views just jump around the swipe views.  */
            	try {
					if (getChildFragmentManager().findFragmentByTag("LessonContainer").isAdded()){
						Log.i(TAG, " ChildFragmentIntro is already added");
						break;
					//I think i can delete this else if statement that i use in MainActivity.
					}else if (isTagInBackStack("LessonContainer")){
						Log.i(TAG, "isTagInBackStack(LessonContainer), I need to remember the purpose of this one. Why didnt i make any comment!!");
						transaction = getChildFragmentManager().beginTransaction();
						transaction.replace(R.id.main_activity_container, getChildFragmentManager().findFragmentByTag("GAME_FRAGMENT"));
						transaction.addToBackStack("LessonContainer");
						transaction.commit();
						break;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.i(TAG, "Error Caught, there is no tag with the name LessonContent yet, it will be created now");
				}
            	
            	Log.i(TAG, "Creating ChildFragmentIntro INSTANCE");
            	LessonIntroContainer xxx = new LessonIntroContainer();

    	        args.putInt("lectureNumber", position);
    	        xxx.setArguments(args);
                
        		transaction = getChildFragmentManager().beginTransaction();
        		transaction.replace(R.id.fragment_child, xxx, "LessonContainer");
        		transaction.addToBackStack("LessonContainer");
        		transaction.commit();
                break;
                
            case 1:
                GrammarFragment gram = new GrammarFragment();
        		transaction = getChildFragmentManager().beginTransaction();
        		transaction.replace(R.id.fragment_child, gram);
        		transaction.commit();
                break;
            case 2:
                GrammarFragment grami = new GrammarFragment();
        		transaction = getChildFragmentManager().beginTransaction();
        		transaction.replace(R.id.fragment_child, grami);
        		transaction.commit();
                break;
            }
            break;
        case 1: // Lesson 2
            switch (position){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            }
            break;
        }
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
