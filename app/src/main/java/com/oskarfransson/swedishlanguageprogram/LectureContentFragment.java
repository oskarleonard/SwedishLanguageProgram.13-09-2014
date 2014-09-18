package com.oskarfransson.swedishlanguageprogram;

/*
 * CURRENTLY NOT IN USE (I only changed MainActivity>onLectureSelected and changed the instace from LectureContentFragment to 
 * ParentViewPagerFragment....
 *  "I dont really need this class, i can incorporate its functionality into the ParentViewPagerFragment class. 
 * For now it works as a FrameContainer for the ParentViewPagerFragment class which holds the Swipe views." 
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LectureContentFragment extends Fragment{
	
	public static final String TAG = LectureContentViewPager.class.getName();
	// Used to pass the current article selected between activities
    final static String ARG_LESSONNR = "lessonNr";
    // Used to track the current article in this class
    int mCurrentPosition = -1;
    
    //This method is a leftover from a previous configuration.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            return;
        }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
    	
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_LESSONNR);
        }

        // Inflate the layout for this fragment
        Log.i(TAG, "Lecturecontent oncteateview ...");
        return inflater.inflate(R.layout.page_view_container, container, false);
	}
	
    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
        if (savedInstanceState != null) {
            return;
        }
        Log.i(TAG, "Lecturecontent onActivityCreated.. ...");
        
        LectureContentViewPager lessonContent = new LectureContentViewPager();
        Bundle args = getArguments();
        int selectedLessonNr = args.getInt("lessonNr");
        args.putInt("selectedLessonNr", selectedLessonNr);
        lessonContent.setArguments(args);
        
        Log.i(TAG, "LessonContentFragment, selectedLessonNr =  " + selectedLessonNr);

        
        switch(selectedLessonNr){
        case 0:
        	//do this
        	break;
        case 1:
        	//do this
        	break;
        default:
        	//do this
        	break;
        }
		
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.add(R.id.page_view_container, lessonContent);
		transaction.commit();
	}
	
	/* 
	@Override old code. Might be useful to still have
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // In onStart the layout has already been applied to the fragment
        // which makes it safe to set the text of lectureTextView inside the
        // method updateArticleView()
        
        Bundle args = getArguments();
        
        // Check if an article had been selected
        
        if (args != null) {
        	
            // Set article based on argument passed in
            updateArticleView(args.getInt(ARG_POSITION));
            
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
        	
            updateArticleView(mCurrentPosition);
        }
    }
    
    // Put the text for the selected article in the article TextView
    public void updateArticleView(int position) {
        TextView article = (TextView) getActivity().findViewById(R.id.lectureTextView);
        article.setText(Lecture.LectureContent[position]);
        mCurrentPosition = position;
    }
*/
	
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        
        outState.putInt(ARG_LESSONNR, mCurrentPosition);
    }
    
}
