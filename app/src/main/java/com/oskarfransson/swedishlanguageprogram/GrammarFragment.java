package com.oskarfransson.swedishlanguageprogram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GrammarFragment extends Fragment {
	
	private static final String TAG = "GrammarFragment";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Log.d(TAG, "GrammarFragment onCreateView()");
		
        Log.d(TAG, "GrammarFragment going to inflate");
        
        
		return inflater.inflate(R.layout.grammar_fragment, container, false);
	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "GrammarFragment onResume");
		//getActivity().getActionBar().setSelectedNavigationItem(2);
	}

}
