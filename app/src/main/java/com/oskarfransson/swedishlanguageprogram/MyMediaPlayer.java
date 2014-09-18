package com.oskarfransson.swedishlanguageprogram;

/* - This is my customized MediaPlayer class. It has an customized Layout that i use, and than has much of the functionallity of
 * 		a regular mediaPlayer class. 
 * - Use this class as a Fragment i.e. load it as a fragment inside another layout. Dont use it to just play a song since there
 * 		already is a way to do this with the built in mediaplayer class. 
 * - The fragment that uses this class will need to pass bundle values as strings to an existing audioFile. 
 * 		There is a maximum of three audiofiles. One for each button. I dont use a next button!! 
 */

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MyMediaPlayer extends Fragment  implements OnSeekBarChangeListener{

	private final String TAG = this.getClass().getSimpleName();
	MediaPlayer mediaPlayer;
	SeekBar seek;
	private Handler mHandler = new Handler();
	private ImageButton maleButton,childButton, femaleButton;
	private TextView tracker, lengthOfSound;
	private int ProgressSave;
	private int buttonPressed; //Keeps track of which button is pressed. 1 for male, 2 for female 3 for child.
	
	String audioPath1, audioPath2, audioPath3; //These strings will get their data from the database

	/* SAVEDSTATE: 
	 * savedInstanceState: This is to retrieve the right track when the user screen rotates.
	 * 
	 * Other else ifs are to retrieve the right track when the user has navigated out of the 
	 * 						lesson and is coming back either thrgough a backpress of the "normal" way. 
	 * 					SavedInstanceState is not true when pressing the back button.
	 */	
	private void SavedStateRotateAndBack(Bundle savedInstanceState){
        if (savedInstanceState != null) {
        	Log.i(TAG, "onCreateView, savedInstanceState != null");
        	ProgressSave = savedInstanceState.getInt("PROGRESS");
        	buttonPressed = savedInstanceState.getInt("buttonPressed");
        	Log.i(TAG, "ProgressSave AND buttonPressed = " + ProgressSave + buttonPressed);
        	if(buttonPressed == 1){
        		Log.i(TAG, "onCreateView, savedInstanceState != null - INSIDE FIRST IF STATEMENT");
        		maleButton.setImageResource(R.drawable.ic_male_speaker);
        		StartTrack(audioPath1);
        		mediaPlayer.pause();
        	}else if(buttonPressed == 2){
        		femaleButton.setImageResource(R.drawable.ic_female_speaker);
        		StartTrack(audioPath2);
        		mediaPlayer.pause();
        	}
        }else if(buttonPressed == 1){
        	Log.i(TAG, "onCreateView, savedInstanceState != null - INSIDE FIRST IF STATEMENT");
        	maleButton.setImageResource(R.drawable.ic_male_speaker);
        	StartTrack(audioPath1);
        	mediaPlayer.pause();
        }else if(buttonPressed == 2){
        	femaleButton.setImageResource(R.drawable.ic_female_speaker);
        	StartTrack(audioPath2);
        	mediaPlayer.pause();
        }
		//EndSavedState
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView START");
		
		//Get the bundle for the paths to the audiofiles. This comes from LessonContentFragment.
		audioPath1 = getArguments().getString("PATH1");
		audioPath2 = getArguments().getString("PATH2");
		//audioPath3 = getArguments().getString("PATH3");
		
		Log.i(TAG, "THIS IS THE BUNDLE FOR THE MP3Player " + audioPath1);
		
        View view =  inflater.inflate(R.layout.my_mediaplayer, container, false);
        InitializeComponents(view);
		
        
        SavedStateRotateAndBack(savedInstanceState);
        	
        mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer arg0) {
            	Log.i(TAG, "Inside onCompletionListener");
            	changeButtonsPressed("normalize");
            	seek.setProgress(0);
			}
        	
        });
        
		//Button 1
		maleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            	Log.i(TAG, "Play button, Duration of the track: " + mediaPlayer.getDuration()  + " buttonPressed = " + buttonPressed);
            	//Play the correct track and change Imagebutton pictures that reflect the correct state of the mediaplayer.
				if(TrackExists(audioPath1) == false){
					Toast.makeText(view.getContext(), "no track", Toast.LENGTH_SHORT).show();
					mediaPlayer.start();
				}else{
					PlayTrackX(maleButton, 1, audioPath1);
				}
            }

        });
		//Button 2
		femaleButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				if(TrackExists(audioPath2) == false){
					Toast.makeText(view.getContext(), "no track", Toast.LENGTH_SHORT).show();
					mediaPlayer.start();
				}else{
					PlayTrackX(femaleButton, 2, audioPath2);
				}
			}
		});
		//Button 3
		childButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){
				
				if(TrackExists(audioPath3) == false){
					Toast.makeText(view.getContext(), "no track", Toast.LENGTH_SHORT).show();
					mediaPlayer.start();
				}else{
					PlayTrackX(childButton, 3, audioPath3);
				}
				
			   }	
		});
		
		return view;
		
	}
	
	//Just initialization, to keep the onCreateView smaller
	private void InitializeComponents(View view){
        femaleButton  = (ImageButton)view.findViewById(R.id.girlButton);
		maleButton = (ImageButton)view.findViewById(R.id.playButton);
	    childButton = (ImageButton)view.findViewById(R.id.crazyButton);
	    tracker = (TextView)view.findViewById(R.id.tracker);
	    lengthOfSound = (TextView)view.findViewById(R.id.lengthOfSound);
		seek = (SeekBar)view.findViewById(R.id.seekBar1);
		seek.setOnSeekBarChangeListener(this);
		mediaPlayer = new MediaPlayer();
	}
	
	private String getTimeString(long myLong) {
	    int minutes = (int) (myLong / (1000 * 60));
	    int seconds = (int) ((myLong / 1000) % 60);
	    return String.format("%d:%02d", minutes, seconds);
	}
	
	
	@Override
	public void onPause(){
		super.onPause();
		Log.i(TAG, " onPause()");
		changeButtonsPressed("normalize");
		
		mediaPlayer.pause(); // Pauses the application when MP.Pause is called and when navigating the app away from the mediaplayer. 
	}
	
	/* This code (from the startSome btn) "mHandler.postDelayed(mRunnable,1000);" executes the code below.
	 * Then it will executes until media player isnt playing any longer (the if statement).
	 * It is the  mHandler.postDelayed(this, 1000); that keeps this method going every 1000 m/s i.e. every second.
	 * So when mediaplayer isn't playing it will pause since the else statement is empty.
	 */
	private Runnable mRunnable = new Runnable() {
		@Override
	    public void run() {
			Log.i(TAG, "Override RUN");
			try { 
				if(mediaPlayer.isPlaying()){
		            Log.i(TAG, "Override RUN inside if mediaPlayer.isPlaying" );
		            seek.setProgress((int)mediaPlayer.getCurrentPosition());
		            mHandler.postDelayed(this, 1000);
		        }else{
		        	//mediaPlayer.pause();
		        }
		        
			} catch (Exception e) {}
		}
	};
	
	// This method gets executed whenere i use a method for the seek bar. i.e. seek.setProgress. 
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		ProgressSave = progress;
		tracker.setText(getTimeString(progress));
		Log.i(TAG, "Override onProgressChanged, progress = " + progress + " fromUser = " + fromUser);
        if(mediaPlayer != null && fromUser){
        	Log.i(TAG, "onProgressChanged inside if()");
        	mediaPlayer.seekTo(progress);
        }
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		Log.i(TAG, "onStartTrackingTouch");
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		Log.i(TAG, "onStopTrackingTouch");
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("PROGRESS", ProgressSave);
		outState.putInt("buttonPressed", buttonPressed);
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		Log.i(TAG, "onDestroy");
		mediaPlayer.release();
	}

	@Override
	public void onStop() {

		super.onStop();
		Log.i(TAG, "onStop");
	}

	@Override
	public void onResume() {

		super.onResume();
		Log.i(TAG, "onResume ");
	}
	
	
	
	/*This changes the image button resources to show play and pause pictures of the buttons*/
	private void changeButtonsPressed(String theButton){
		theButton.toLowerCase();
		
		if (theButton == "male"){
			maleButton.setImageResource(R.drawable.ic_male_speaker); //This should be changed to male pressed
		}else if(theButton == "female"){
			femaleButton.setImageResource(R.drawable.ic_female_speaker); //this
		}else if(theButton == "child"){
			childButton.setImageResource(R.drawable.ic_male_speaker); //this
		}else if (theButton == "normalize"){
			maleButton.setImageResource(R.drawable.ic_male_speaker);
			femaleButton.setImageResource(R.drawable.ic_female_speaker);
			childButton.setImageResource(R.drawable.ic_child);
		}

	}
	
	/* This will load and play an audiofile. The string is the path to the mp3 file. I should make this a bool in future to handle fails*/
	private boolean StartTrack(String path){
		Log.i(TAG, "StartTrack START ");
	    	try {
				
				AssetFileDescriptor afd = getActivity().getAssets().openFd(path);
				mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());;
				
				PresetReverb mReverb = new PresetReverb(0,mediaPlayer.getAudioSessionId());
				mReverb.setPreset(PresetReverb.PRESET_SMALLROOM);
				mReverb.setEnabled(true);
				mediaPlayer.setAuxEffectSendLevel(1.0f);
				
				mediaPlayer.prepare();
				mediaPlayer.seekTo(ProgressSave);
				Log.i(TAG, "ProgressSave =  " + ProgressSave);
				
				//Set the length of the seekbar and textview. 
				lengthOfSound.setText(getTimeString(mediaPlayer.getDuration()));
				Log.i(TAG, "mediaPlayer.getDuration() = " + mediaPlayer.getDuration());
				seek.setMax(mediaPlayer.getDuration());
				
				mediaPlayer.start();
	    		mHandler.postDelayed(mRunnable,1000);
	    		return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		
	}
	private boolean TrackExists(String path){
    	try {
			getActivity().getAssets().openFd(path);
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/*This method is to clean up the code inside the buttons. It just takes the button and buttonNumber (which keeps track of which
	 * button that is pressed) the path to the right Audipath for the button and a string that should be named either, male, female
	 * or child this will update the buttons whenever the user goes from listening to the female track from the male track (and wiseversa).
	 */
	private void PlayTrackX(ImageButton buttonX, int buttonNumber, String path){
    	if(mediaPlayer.isPlaying() && buttonPressed == buttonNumber){
    		changeButtonsPressed("normalize");
    		mediaPlayer.pause();
    	}else if(buttonPressed == buttonNumber){
    		buttonX.setImageResource(R.drawable.ic_pause);
    		mediaPlayer.start();
    		mHandler.postDelayed(mRunnable,1000);
    	}else{
    		changeButtonsPressed("normalize");
    		mediaPlayer.reset();
    		seek.setProgress(0);
    		buttonX.setImageResource(R.drawable.ic_pause);
    		StartTrack(path);
    	}
    	
    	buttonPressed = buttonNumber;
	}

	
	
}
