package com.oskarfransson.swedishlanguageprogram;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class InfoFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		return inflater.inflate(R.layout.info_page, container, false);
		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		
		//inflater.inflate(R.menu.testmenu, menu);
	}

}
