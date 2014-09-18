package com.oskarfransson.swedishlanguageprogram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * This fragment hosts the viewpager that will use a FragmentPagerAdapter to display child fragments.
 */
public class LectureContentViewPager extends Fragment {

    public static final String TAG = LectureContentViewPager.class.getName();
    
    //Contains the current lesson, will be passed in a bundle to LessonSwipeViews.java!!!????!!!
    private static int lessonNr;

    public static LectureContentViewPager newInstance() {
        return new LectureContentViewPager();
    }

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "ParentViewPagerFragment onResume");
		getActivity().getActionBar().setSelectedNavigationItem(1);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        View root = inflater.inflate(R.layout.fragment_parent_viewpager, container, false);
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        
        /** Important: Must use the child FragmentManager or you will see side effects. 
         * VERY VERY IMPORTANT. Had getChildFragmentManager here but not in ChildFragment.java
         * when inflating the swipe views. And indeed there were side effects. The views just jumped around.**/
        
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        
        Bundle args = getArguments();
        int selectedLessonNr = args.getInt("selectedLessonNr");
        lessonNr = selectedLessonNr;
        
        Log.i(TAG, "ParentViewPager onCreate() selectedLessonNr = " + selectedLessonNr);
        return root;
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        // Sets the number of swipe views.
        @Override
        public int getCount() {
            return 3;
        }

        /* IMPORTANT Gets the childfragment for the correct swipe view.
         * This method is IMPORTANT becuase its here where i put the bundles 
         * that are used in the lessonSwipeViews Class. These bundle values 
         * are needed so i can display the correct lesson. 
         */
        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            
            args.putInt(LessonSwipeViews.POSITION_KEY, position);
            args.putInt(LessonSwipeViews.SELECTED_LESSON_NR, lessonNr);
            
            Log.i(TAG, "Are the Two bundle values ok??, selectedLessonNr =  " + lessonNr + "\n position = " + position);
        	
            return LessonSwipeViews.newInstance(args);
        }
        
        // Set the header for the swipe view
        @Override
        public CharSequence getPageTitle(int position) {
        	
        	if (position == 0){
        		Log.i(TAG, "CharSequence getPageTitle, position0 = " + position);
        		return "Introduction";
        	}
        	else if (position == 1){
        		Log.i(TAG, "CharSequence getPageTitle, position1 = " + position);
        		return "Grammar";
        	}
        	else if (position == 2){
        		Log.i(TAG, "CharSequence getPageTitle, position2 = " + position);
        		return "Test";
        	}
        	else
        		return "Child Fragment " + position;
        }
        

    }

}
