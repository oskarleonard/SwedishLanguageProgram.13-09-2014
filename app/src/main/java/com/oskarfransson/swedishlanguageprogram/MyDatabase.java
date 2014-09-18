package com.oskarfransson.swedishlanguageprogram;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "swedishLanguageProgramDatabase.db";
    private static final int DATABASE_VERSION = 3;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        /*This is where i left of 08-02.
         * setForcedUpgrade() in this constructor will force the app to update the locally held 
         * database with the "new" one in the assets folder, everytime the version nr gets incremented.
         */
        setForcedUpgrade();
    }
    
    public ArrayList<HashMap<String, String>> getAllLectures() {
    	
    	ArrayList<HashMap<String, String>> lectureArrayList = new ArrayList<HashMap<String, String>>();
    	String selectQuery = "SELECT * FROM Lectures";
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor c = db.rawQuery(selectQuery, null);
    	
    	if(c.moveToFirst()){
    		do{
    			HashMap<String, String> contactMap = new HashMap<String, String>();
    			contactMap.put("LectureNr", c.getString(0));
    			contactMap.put("LectureContent", c.getString(1));
    			contactMap.put("AudioPath1", c.getString(2));
    			contactMap.put("AudioPath2", c.getString(3));
    			contactMap.put("AudioPath3", c.getString(4));
    			lectureArrayList.add(contactMap);
    		}while(c.moveToNext());
    	}
    	
    	return lectureArrayList;
    }

    
}
