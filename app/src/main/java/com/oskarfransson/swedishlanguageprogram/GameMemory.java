package com.oskarfransson.swedishlanguageprogram;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


import android.content.res.AssetFileDescriptor;

import android.media.MediaPlayer;
import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/* TODO
 * Put all the buttons in a list and sort them. 
 * Problem, knowing with button in the list that belongs to a certain ic_xxx picture. Need to create a method for that. 
 */
public class GameMemory extends Fragment{

	ImageButton theLastButton;
	View theView;
	List<ImageButton> allButtons;
	private final String TAG = this.getClass().getSimpleName();
	TextView attemptsTVnr;
	int attempts, pairsLeft, time;
	private Handler mHandler = new Handler();
	MediaPlayer mediaPlayer;
	//This is the newest comment try commit


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		// Need to initialize the meadiaplyer in case a button should play a sound. 
		mediaPlayer = new MediaPlayer();
		
		//Hide the actionback to open space and make it impossible to navigate out of the 
		getActivity().getActionBar().hide();
		
		theView = inflater.inflate(R.layout.game_memory_fragment, container, false);
		
		//Initialize the button that will hold the last button(card) pressed. 
		theLastButton = new ImageButton(theView.getContext());
		theLastButton.setTag("This buttons doesnt_even exist yet"); //The tag needs to be set to avoid an exception, in the checkCard(). 
		
		/*This fragment have to get one argument, i.e. the cards(images) it will use for the game. 
		 * Technically it is the name of the images as a strings. The fragment will then look in the 
		 * drawable folder for that name. I chose the drawable folder because that way the images can easily adopts
		 * to different screen sizes. */
		List<String> theCards = getArguments().getStringArrayList("CARDS");
		Collections.shuffle(theCards); //Shuffle the cards. 
		
		Log.i(TAG, theCards.toString());
		
    	// Initialize the var the will keep track of how long the user 
    	time = 0;
		//Initialize the attempts, cardsLeft and timerTV.
		attemptsTVnr = (TextView)theView.findViewById(R.id.attemptsTVnr);
		attempts = 0; //Keep track of how many tries the user has done
		pairsLeft = theCards.size()/2; // Keeps track of how many unfolded cards the user has left. When = to 0, a win method will execute.
		
		setUpTheButtonCards(theView, theCards.size(), theCards);
		
		
		return theView;
		
	}
	
	private void setUpTheButtonCards(View theView, int numberOfButtons, List<String> paths){
		Log.i(TAG,"setUpTheButtonCards");
		
		TableLayout table = (TableLayout)theView.findViewById(R.id.TabLayMemoryGame);
		
		TableRow tr = new TableRow(theView.getContext());
		//tb2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		int y=3;
		//Create 
		for (int x = 0; x < numberOfButtons; x++){
			Log.i(TAG,"setUpTheButtonCards inside for x = " + x);

			ImageButton button = new ImageButton(theView.getContext());
			button.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
			button.setId(x);
			button.setTag(paths.get(x));
			button.setImageResource(R.drawable.ic_action_help);
			button.setOnClickListener(checkCard(button, x));
			tr.addView(button);
			
			if(y == x){
				table.addView(tr);
				Log.i(TAG,"Create a Row");
				tr = new TableRow(theView.getContext());
				y+=4;
			}
		}
		if(tr != null){
			Log.i(TAG,"There is still an row with data that needs to be added.");
			table.addView(tr);
		}

	}
	
	View.OnClickListener checkCard(final ImageButton button, final int x)  {
	    return new View.OnClickListener() {
	        public void onClick(View v) {
	        	Log.i(TAG, "Last Button = " + theLastButton + " Clicked Button = " + button.getTag().toString() );
	        	
	        	/*First, check so the last button wasnt the clicked button. This would hapen if the user
	        		double click the same button. In that case dont do anything*/
	        	if(theLastButton == button){
	        		return;
	        	}
	        	
	        	//Start the timer. It only gets executed the first time the player clicks an image. 
	        	if(attempts == 0) {mHandler.postDelayed(mRunnable,100);};
	        	// Increment the attempts by one and set it to its corresponding textview
	        	attempts++;
	        	attemptsTVnr.setText(Integer.toString(attempts));
	        	
	        	//Understand what type of button was pressed. Audio / Text / Picture etc. 
	        	String clickedBtn = button.getTag().toString();
	        	if(clickedBtn.contains("audio")){
	        		Log.i(TAG, "button should play sound");
	        		button.setImageResource(R.drawable.ic_play);
	        		String path = "audio/memory/" + clickedBtn + ".wav";
	        		playButton(path);
	        	}else if(clickedBtn.contains("text")){
		        	//Change the Cards Image. 
		        	int id = getResources().getIdentifier(button.getTag().toString(), "drawable", "com.oskarfransson.swedishlanguageprogram");
		        	button.setImageResource(id);
	        	}

	        	
	        	//Get the string of the clickedButton after it has been split from the _ underscore.x.
	        	/** This could cause problems in the future if the path contains an _  **/
	        	clickedBtn = button.getTag().toString().split("_")[1];
	        	String lastBtn = theLastButton.getTag().toString().split("_")[1];

	        	Log.i(TAG, "clickedBtn = " + clickedBtn);
	        	
	        	//Check if the last button is the pair for the clicked button.
	        	if(lastBtn.equals(clickedBtn)){
		        		Log.i(TAG, "Correct");
		        		//Remove a pair
		        		pairsLeft--;
		        		//Disable the imagebuttons for the completed pairs. 
		        		lockTheCompletedPairs(theLastButton, button);
		        		//Create a new instance of the last button. I.e reset it when a pair is completed. 
		        		theLastButton = new ImageButton(theView.getContext());
		        		theLastButton.setTag("I still need to give_this button a tag");
		        		
		        		//When the user have completed all the cards and win. 
		        		if (pairsLeft==0){
		        			userWin();
		        		}
	        		
	        	}else{
	        		//Hide the last button. And make the recently clicked button the last one before leaving this method.
	        		theLastButton.setImageResource(R.drawable.ic_action_help);
		        	theLastButton = button;
	        	}
	        	Log.i(TAG, "END CLICK METHOD");
	        }
	    };
	}
	
	//Play a sound
	private void playButton (String path){
		
		mediaPlayer.reset();
		try {
			Log.i(TAG, path);
			AssetFileDescriptor afd = getActivity().getAssets().openFd(path);
			mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());;
			mediaPlayer.prepare();
			mediaPlayer.start();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Lock two imagebuttons
	private void lockTheCompletedPairs(ImageButton theLastButton, ImageButton button){
		int lastBtnPicture;
		
		if(theLastButton.getTag().toString().contains("audio")){
			lastBtnPicture = getResources().getIdentifier("ic_play", "drawable", "com.oskarfransson.swedishlanguageprogram");
		}else{
			lastBtnPicture = getResources().getIdentifier(theLastButton.getTag().toString(), "drawable", "com.oskarfransson.swedishlanguageprogram");
		}
		
		theLastButton.setImageResource(lastBtnPicture);
		button.setEnabled(false);
		theLastButton.setEnabled(false);
	}

	private void userWin(){
		Log.i(TAG, "userWin");
		mHandler.removeCallbacks(mRunnable);
		Toast.makeText(theView.getContext(), "Time: " + getTimeString(time) + "\nAttempts = " + attempts, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.i(TAG,"onStop()");
		super.onStop();
		
		//Be sure to remove the callback leaving this game
		mHandler.removeCallbacks(mRunnable);
		
		//Show the actionbar when the user press backBtn i.e. leaves the gameMemory fragment. 
		getActivity().getActionBar().show();
	}
	
	//Keeps counting the time. Since no pause button is necessary i dont any more methods, as i needed with the media player.
	private Runnable mRunnable = new Runnable() {
		@Override
	    public void run() {
			Log.i(TAG, "Override RUN");
			time++;
			TextView timerTV = (TextView)theView.findViewById(R.id.timerTV);
			timerTV.setText(getTimeString(time));
            mHandler.postDelayed(this, 100);
		}
	};
	
	//Format the timmer so it looks like i want it to. 
	private String getTimeString(long millis) {
		Log.i(TAG, "getTimeString millis = " + millis);
	    int minutes = (int) (millis / 600);
	    int seconds = (int) ((millis / 10) % 60);
	    int milliseconds = (int) (millis % 10);
	    return String.format("%d:%02d.%d", minutes, seconds, milliseconds);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		//Dont crash If meadiaplayer hasnt been created.
		try {
			mediaPlayer.release();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
