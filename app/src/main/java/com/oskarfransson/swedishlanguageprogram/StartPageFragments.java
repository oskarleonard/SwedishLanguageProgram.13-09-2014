package com.oskarfransson.swedishlanguageprogram;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class StartPageFragments extends Fragment{
	
	private final String TAG = this.getClass().getSimpleName();
	ImageButton btnLectures, btnGame, btnOverview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view =  inflater.inflate(R.layout.start_page, container, false);
		getActivity().getActionBar().setSelectedNavigationItem(0);
		
		Log.i(TAG, "StartPageFragment onCreateView");
		//Lesson Button
		btnLectures = (ImageButton)view.findViewById(R.id.btnLectures);
		btnLectures.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
				//Toast.makeText(getActivity(), "Button is working", Toast.LENGTH_LONG).show();
    			/*LectureTitleFragment infoFrag = new LectureTitleFragment();
    			FragmentTransaction transaction = getFragmentManager().beginTransaction();
    			transaction.replace(R.id.main_activity_container, infoFrag, "second");
    			transaction.addToBackStack(null);
    			transaction.commit();*/
            	getActivity().getActionBar().setSelectedNavigationItem(1);
            }

        });
		
		//Exam Button
		btnOverview = (ImageButton)view.findViewById(R.id.btnOverview);
		btnOverview.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				getActivity().getActionBar().setSelectedNavigationItem(3);
				
			}
			
		});
		
		//Test Button
		
		//Game Button
		btnGame = (ImageButton)view.findViewById(R.id.btnGame);
		btnGame.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				getActivity().getActionBar().setSelectedNavigationItem(2);
				
			}
			
		});
		
		
		
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "StartPageFragment onResume");
		getActivity().getActionBar().setSelectedNavigationItem(0);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	

}
