package com.oskarfransson.swedishlanguageprogram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class StartGameFragment extends Fragment {

	Button memoryButton;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.start_game_fragment, container, false);
		
		memoryButton = (Button)view.findViewById(R.id.memoryButton);
		memoryButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				GameMemory gm = new GameMemory();
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.main_activity_container, gm);
				transaction.addToBackStack(null);
				
				GetDatabaseData();
				
            	Bundle args = new Bundle();
            	args.putStringArrayList("CARDS", GetDatabaseData());
    	        gm.setArguments(args);
    	        
				transaction.commit();
			}
			
		});
		return view;
	}
	
	private ArrayList<String> GetDatabaseData() {
		// TODO Auto-generated method stub
		
		MyDatabase db = new MyDatabase(this.getActivity());
		ArrayList<HashMap<String, String>> allLectures = db.getAllLectures();
		ArrayList<String> theList = new ArrayList<String>();
		if(allLectures.size() != 0){
			theList.add("text_a_en");
			theList.add("text_a_se");
			theList.add("audio_an_1se");
			theList.add("text_an_en");
			theList.add("text_approximately_en");
			theList.add("audio_approximately_1se");
			theList.add("text_areis_en");
			theList.add("text_areis_se");
			theList.add("text_at_se");
			theList.add("text_at_en");
			theList.add("text_downtown_en");
			theList.add("text_downtown_se");
			theList.add("text_hair_se");
			theList.add("text_hair_en");
			theList.add("text_have_en");
			theList.add("text_have_se");
			
			/*Use the below when i have added the cards name to the database
			theList.add(allLectures.get(0).get("LectureContent"));
			theList.add(allLectures.get(0).get("AudioPath1"));
			theList.add(allLectures.get(0).get("AudioPath2"));
			*/
		}
		db.close();
		//End of Database
		return theList;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().getActionBar().setSelectedNavigationItem(2);
	}
	
	

}
