package com.oskarfransson.databasetools;

/** @author gangleri
 *This is class purpose is just to convert the items in an array into insert statement that are used in a sql database.
 * The insertstatements gets printed out in the console window. To make this work, steps from this http://www.geekbit.es/2014/06/solution-to-fatal-error-invalid-layout.html?showComment=1406899955163#c7343705536633018398
 * needed to be done!
 */

public class DatabaseLectures {

	static String[][] lecturesArray = {
			{"Lecture 1", "Välkommen till den första lektionen (welcome to this first lecture)"},
			{"Lecture 2", "This is your second Lecture"},
			{"Lecture 3", "Den tredje lektionen"}
	};
	
	public static void main(String[] args){
		
		
		DatabaseLectures databaseLecturesTools = new DatabaseLectures();
		
		// Outputs the INSERT statement for the LecturesArray table.
		databaseLecturesTools.generateInsertStatements(lecturesArray, "lectures",
				"lecture_number", "lecture_content");
		
	}

	private void generateInsertStatements(String[][] theArray,
			String tableName, String cellName1, String cellName2) {
		
		for(String[] item : theArray){
			System.out.println("INSERT INTO " + tableName + " (" + 
			cellName1 + ", " + cellName2 + ") VALUES ('" + 
			item[0] + "', '" + item[1] + "');");
		}
	}
	
}
