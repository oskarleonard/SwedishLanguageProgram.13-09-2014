package com.oskarfransson.swedishlanguageprogram;
/* Notes:
 * Finding backStack & finding current showing fragment
  I name the fragment and the backStack entry (below they are named the same "GRAMMAR_FRAGMENT"
    			-- transaction.replace(R.id.main_activity_container, grammarFrag, "GRAMMAR_FRAGMENT");
				-- transaction.addToBackStack("GRAMMAR_FRAGMENT");
  so that i can call to see which fragment is currently showing: 
  				getFragmentManager().findFragmentByTag("GRAMMAR_FRAGMENT").isVisible()
  and by naming the backStack entry when i create the fragment i can later know what a certain backStack item was: 
				getFragmentManager().getBackStackEntryAt(2).getName();
	Unfortantly, the getName() only work for API 14+. And i want to develop for API 11+
	So, i will need to figure out something else to achieve this functionality.

* ERROR: "SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length"
	Is cause by a third party keyboard
	http://stackoverflow.com/questions/13670374/android-span-exclusive-exclusive-spans-cannot-have-a-zero-length

06/27: Switched to use Support fragments app.v4 because i needed viewpager functionality. And supportFragments
seems to be better. 
http://stackoverflow.com/questions/17553374/android-app-fragments-vs-android-support-v4-app-using-viewpager
* 
 */
import java.util.ArrayList;

import com.oskarfransson.actionbar.adapter.TitleNavigationAdapter;
import com.oskarfransson.actionbar.spinner.SpinnerNavItem;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener, 
LectureTitleFragment.OnLectureSelectedListener, OnBackStackChangedListener {
    
	public static final String SHARED_PREF = "MySharedPreferencesHolder";
	
	// action bar
    private ActionBar actionBar;
 
    // Title navigation Spinner data
    int spinnerPosition = 0;
    private ArrayList<SpinnerNavItem> navSpinner;
     
    // Navigation adapter
    private TitleNavigationAdapter adapter;
    
    ImageButton btnLectures;
    private final String TAG = this.getClass().getSimpleName();
    
    
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActionBar().setSelectedNavigationItem(spinnerPosition);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_container);
		
		SetUpTheActionBarSpinner();
        // If we're being restored from a previous state,
        // don't do anything or you could end up with overlapping fragments
        if (savedInstanceState != null) {
        	
        	int ok = savedInstanceState.getInt("Position");
        	spinnerPosition = ok;
        	Log.i(TAG, "This is the savedinstancestate spinner position: " + ok);
        	
        	
            return;
        }
        
        
        
        //Enable onBackStackChanged() 
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        
        
        StartPageFragments startPageFragment = new StartPageFragments();
		FragmentTransaction transactio = getSupportFragmentManager().beginTransaction();
		transactio.add(R.id.main_activity_container, startPageFragment, "STARTPAGE_FRAGMENT");
		/* I dont want addToBackStack here since R.id.main_activity_container is just a framecontainer
			and i dont want it displayed empty (without a fragment in this case the startPageFragment)*/
		transactio.commit();
		
        Log.i(TAG, "onCreate END");
        
	}
	
	private void SetUpTheActionBarSpinner(){
        actionBar = getActionBar();
        
        // Hide the action bar title
        actionBar.setDisplayShowTitleEnabled(false);
 
        // Enabling Spinner dropdown navigation
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        
        // Spinner title navigation data
        navSpinner = new ArrayList<SpinnerNavItem>();
        navSpinner.add(new SpinnerNavItem("Go to:", R.drawable.ic_navigation));
        navSpinner.add(new SpinnerNavItem("Lectures", R.drawable.ic_swedish_lecture1));
        navSpinner.add(new SpinnerNavItem("Game", R.drawable.ic_game));
        navSpinner.add(new SpinnerNavItem("Test", R.drawable.ic_test));    
         
        // title drop down adapter
        adapter = new TitleNavigationAdapter(getApplicationContext(), navSpinner);
 
        // assigning the spinner navigation    
        actionBar.setListNavigationCallbacks(adapter, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()){
		case R.id.action_info:
			InfoFragment infoFrag = new InfoFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.main_activity_container, infoFrag);
			transaction.addToBackStack(null);
			transaction.commit();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    /**
     * ActionBar navigation item select listener
     * */
	@Override
	public boolean onNavigationItemSelected(int ItemPosition, long itemId) {
		
		/* http://stackoverflow.com/questions/767821/is-else-if-faster-than-switch-case
		 * Is “else if” faster than “switch()?
		 * If a switch contains more than five items, it's implemented using a lookup table or a hash list. This means that all items get the same access time, compared to a list of if:s where the last item takes much more time to reach as it has to evaluate every previous condition first.
		 * True, but with an if-else-if chain you can order the conditions based on how likely they are to be true. –  
		 */
		//"I will use an else if Since i use ItemPosition (so i dont have to grab the R.id) and i know that 
		// Home is most likely to be use the most
		/*
		 * Edit: For clarity's sake: implement whichever design is clearer and more maintainable. 
		 * Generally when faced with a huge switch-case or if-else block the solution is to use 
		 * POLYMORPHISM. 
		 * Find the behavior that's changing and encapsulate it. I've had to deal with huge, ugly 
		 * switch case code like this before and generally it's not that difficult to simplify. 
		 * But oh so satisfying.
		 */
		
		
		
		//Home
		if(ItemPosition == 0){
			/* A silent error will happen here if the app hasent created an instance of
			 * a fragment labeled STARTPAGE_FRAGMENT. 
			 */
			try{
				/* Prevent double execution, during BACK Btn and updating the spinner values. 
				 * Check if the startpage fragment is visible. If it is visible
				 * dont waist energy to create another instance of the same fragment.
				 * This is a small hack from my side: when the user presses the back button
				 * i need to update the header of the spinner to reflect the correct fragment
				 * Without creating an additional instance.
				 */
				
				if (getSupportFragmentManager().findFragmentByTag("STARTPAGE_FRAGMENT").isVisible()){
					Log.i(TAG, "first already visible");
					//nothing need to be done here. 
					return false;
				}
			}catch(NullPointerException e){
				Log.i(TAG, "this error got caugth: " + e);
			}
			
			Log.i(TAG, "StartPageFragment Created  -- old comment:ERROR: If first is already visiable this text should not be!!");
			StartPageFragments startPage = new StartPageFragments();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.main_activity_container, startPage, "STARTPAGE_FRAGMENT");
			transaction.addToBackStack("STARTPAGE_FRAGMENT");
			transaction.commit();
			return true;
			
		}
		//Lectures
		else if(ItemPosition == 1){
			Log.i(TAG, "OnNavigationItemSelected LECTURE_TITLE 1");
			try{
				if (getSupportFragmentManager().findFragmentByTag("LECTURE_TITLE").isVisible() || getSupportFragmentManager().findFragmentByTag("LESSON_CONTENT").isVisible()){
					Log.i(TAG, "OnNavigationItemSelected LECTURE_TITLE Already instantiated");
					//nothing need to be done here. 
					return false;
				}
			}catch(NullPointerException e){
				Log.i(TAG, "this error got caugth: " + e);
			}
			
			Log.i(TAG, "OnNavigationItemSelected LECTURE_TITLE instance created");
			LectureTitleFragment ltf = new LectureTitleFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.main_activity_container, ltf, "LECTURE_TITLE");
			transaction.addToBackStack("LECTURE_TITLE");
			transaction.commit();
			return true;
			
		}
		//Next
		else if(ItemPosition == 2){
			try{
				Log.i(TAG, "Checking if Grammar fragment is already visable");
				
				/* This ifelse statement are a HUGE sideeffect of making my own navigation method (spinners) work. 
				 * First i need to check if the fragment is already visable so i wont create another instance of
				 * that fragment (this happens during BACK Btn). Than if the user navigates through the SPINNER MENU
				 * to make it work like the BACK Btn, i.e. have DATA SAVED, i need to go search the backstack for 
				 * that fragment and use that specific fragment (not create a new one). If i create a new one, the data
				 * on that fragment would be lost. If i took away the else if statement that is exactly what would happen. 
				 */
				if (getSupportFragmentManager().findFragmentByTag("GAME_FRAGMENT").isVisible()){
					Log.i(TAG, "OnNavigationItemSelected GRAMMAR_FRAGMENT Already instantiated");
					//nothing needs to be done here. 
					return false;
				}else if (isTagInBackStack("GAME_FRAGMENT")){
					Log.i(TAG, "Tag is in BackStack!!!! frag ret = " + getSupportFragmentManager().findFragmentByTag("GAME_FRAGMENT"));
					
					FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
					transaction.replace(R.id.main_activity_container, getSupportFragmentManager().findFragmentByTag("GAME_FRAGMENT"));
					transaction.addToBackStack("GAME_FRAGMENT");
					transaction.commit();
					return false;
					
				}
			}catch(NullPointerException e){
				Log.i(TAG, "this error got caugth: " + e);
			}
			Log.i(TAG, "OnNavigationItemSelected GRAMMAR_FRAGMENT instance created");
			
			
			
			//startActivity(getIntent(ParentViewPagerFragment.TAG, R.string.action_bar_search));
			
			StartGameFragment gameFragment = new StartGameFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.main_activity_container, gameFragment, "GAME_FRAGMENT");
			transaction.addToBackStack("GAME_FRAGMENT");
			transaction.commit();
			return true;
		}
		
		//Next
		else if(ItemPosition == 3){
			try{
				if (getSupportFragmentManager().findFragmentByTag("StartOverViewFragment").isVisible()){
					//nothing needs to be done here. 
					return false;
				}else if (isTagInBackStack("StartOverViewFragment")){
					Log.i(TAG, "Tag is in BackStack!!!! frag ret = " + getSupportFragmentManager().findFragmentByTag("StartOverViewFragment"));
					
					FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
					transaction.replace(R.id.main_activity_container, getSupportFragmentManager().findFragmentByTag("StartOverViewFragment"));
					transaction.addToBackStack("StartOverViewFragment");
					transaction.commit();
					return false;
					
				}
			}catch(NullPointerException e){
				Log.i(TAG, "this error got caugth: " + e);
			}

			LessonIntroContainer startOverViewFragment = new LessonIntroContainer();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.main_activity_container, startOverViewFragment, "StartOverViewFragment");
			transaction.addToBackStack("StartOverViewFragment");
			transaction.commit();
			return true;
		}
		return true;
	}
	
	/** A method listener that listens if the user has pressed a LessonTitle. 
	 * Used to navigate from the LectureTitleFragment to the Lecture content.
	 */
	@Override
	public void onLectureSelected(int LecturePosition) {
		
		/* Same if else statement that is in onNavigationItemSelected. When clicking the LessonTitle, i first
		 * check if a LectureContentViewPager fragment has been created. Is so, i use that one instead of a new one.
		 * That way, the data in the viewpager, like how long the mediaplayer has played and the test  data the user
		 * has entered, all this will be saved this way. 
		 */
		
		if (isTagInBackStack("LESSON_CONTENT")){
			Log.i(TAG, "Tag is in BackStack!!!! frag ret = " + getSupportFragmentManager().findFragmentByTag("LESSON_CONTENT"));
			
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.main_activity_container, getSupportFragmentManager().findFragmentByTag("LESSON_CONTENT"));
			transaction.addToBackStack("LESSON_CONTENT");
			transaction.commit();
		}else{
	        // Create the LectureContentFragment
			LectureContentViewPager lectureContentFragment = new LectureContentViewPager();
	        
	        // The Bundle contains information passed between activities.
	        // It will pass the int LecturePosition with a string (works like a dictionary, ex.
	        // type the string and find the int, so int = string.)
	        // this information will be used in the LectureContentFragment onCreatView() to
	        // help it set the current_lecture.xml TextView witht he correct content.
	        Bundle args = new Bundle();
	        Log.i(TAG, " onLectureSelected SelectedlessonNR  = " + LecturePosition);
	        args.putInt(LessonSwipeViews.SELECTED_LESSON_NR, LecturePosition);
	        // Add the article value to the new Fragment
	        lectureContentFragment.setArguments(args);
	        
	        
	        // Finally, replace the LectureTitle (the list xml containing the lecture titles which was created at runtime "onCreate()")
	        // with the lectureContentFragment
	        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	        transaction.replace(R.id.main_activity_container, lectureContentFragment, "LESSON_CONTENT");
	        //
	        // addToBackStack() causes the transaction to be remembered. 
	        // It will reverse this operation when it is later popped off 
	        // the stack.
	        transaction.addToBackStack("LESSON_CONTENT");
	        transaction.commit();
		}
        
		
	}
	
	/** Notify the user that He will exit the app if pressing the back btn again
	 * 
	 */
	private long backPressTime = 0;
    @Override
    public void onBackPressed() {
    	Log.i(TAG, "onBackPresses START");
    	int lastStack = getSupportFragmentManager().getBackStackEntryCount();
    	Log.i(TAG, "There are:" + lastStack + " Many entries in backStack");
    	
		// IF backBtn is pressed only once within 3 seconds and backstackcount == 0 give a message that show that 
    	// the user will exit the app if backbtn is pressed again (fast)
    	if (this.backPressTime < System.currentTimeMillis() - 3000  && getSupportFragmentManager().getBackStackEntryCount() == 0){
    		Toast.makeText(this, "Press back again to close this app", Toast.LENGTH_SHORT).show();
    		this.backPressTime = System.currentTimeMillis();
    		/*This is how you clear the backstack
    		 *getFragmentManager().popBackStack();
    		 */
    		
    	}else{
    		super.onBackPressed();
    	}
    	
		Log.i(TAG, "onBackPresses END");
    }
    
    //Gets called every time the backstack is changed
	@Override
	public void onBackStackChanged() {
		Log.i(TAG, "change back stack");
    	int lastStack = getSupportFragmentManager().getBackStackEntryCount();
    	Log.i(TAG, "There are:" + lastStack + " Many entries in backStack");
    	Log.i(TAG, "END back stack");
		// Updates the Spinner, it recreates it. 
    	//actionBar.setListNavigationCallbacks(adapter, this);
		
	}
	
	//Not in use
	private Fragment LastViewInBackStack(){
		Log.i(TAG, "START CurrentlyVisable method");
		Fragment currentlyVisable = new Fragment();
		Log.i(TAG, "new frag cteated");
		
		if (getSupportFragmentManager().findFragmentByTag("STARTPAGE_FRAGMENT").isVisible()){
			Log.i(TAG, "Currently Visable Fragment is STARTPAGE");
			currentlyVisable = getSupportFragmentManager().findFragmentByTag("STARTPAGE_FRAGMENT");
		}
		else if (getSupportFragmentManager().findFragmentByTag("LECTURE_TITLE").isVisible()){
			Log.i(TAG, "Currently Visable Fragment is LECTURE_TITLE");
			currentlyVisable = getSupportFragmentManager().findFragmentByTag("LECTURE_TITLE");
		}
		else if (getSupportFragmentManager().findFragmentByTag("GRAMMAR_FRAGMENT").isVisible()){
			Log.i(TAG, "Currently Visable Fragment is GRAMMAR_FRAGMENT");
			currentlyVisable = getSupportFragmentManager().findFragmentByTag("GRAMMAR_FRAGMENT");
		}
		else{
			currentlyVisable = null;
		}
		Log.i(TAG, "END CurrentlyVisable() the Fragment was: " + currentlyVisable.toString());
		
		return currentlyVisable;
	}
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//Needed for screen rotate
		outState.putInt("Position", spinnerPosition);
	}

	private void UpdateSpinnerNav(){
		/* This code only updates the icon/title in the spinner
		 * but doesnt change it to the correct selected one
		navSpinner.set(0, (SpinnerNavItem) adapter.getItem(1));
		*/
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		spinnerPosition = getActionBar().getSelectedNavigationIndex();
	}
	
	public boolean isTagInBackStack(String tag){
		
		Log.i(TAG, "isTagInBackStack() Start");
		int x;
		boolean toReturn = false;
		int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
		Log.i(TAG, "backStackCount = " + backStackCount);
		
		for (x = 0; x < backStackCount; x++){
			Log.i(TAG, "Iter = " + x +" "+ getSupportFragmentManager().getBackStackEntryAt(x).getName());
			if (tag == getSupportFragmentManager().getBackStackEntryAt(x).getName()){
				toReturn = true;
				
			}
			
		}
		
		Log.i(TAG, "isTagInBackStack() End, toReturn = " + toReturn);
		return toReturn;
	}

}