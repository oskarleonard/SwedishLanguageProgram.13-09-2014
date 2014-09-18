package com.oskarfransson.actionbar.spinner;
//This code and relating code is taken from
//http://www.androidhive.info/2013/11/android-working-with-action-bar/

public class SpinnerNavItem {
	
	private String title;
	private int icon;
	
	public SpinnerNavItem(String title, int icon){
		this.title=title;
		this.icon=icon;
	}
	
    public String getTitle(){
        return this.title;     
    }
     
    public int getIcon(){
        return this.icon;
    }

}
