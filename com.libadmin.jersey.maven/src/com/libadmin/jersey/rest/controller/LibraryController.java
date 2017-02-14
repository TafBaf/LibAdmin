package com.libadmin.jersey.rest.controller;

import java.sql.ResultSet;
import java.util.ArrayList;
import org.json.JSONObject;

import com.libadmin.jersey.rest.model.LibAction;



public class LibraryController {

	public static void AddLibraryAction(JSONObject libraryActionJSON) throws Exception {
    	if (libraryActionJSON != null) {
    		String query = "INSERT INTO libraryaction (id_book, id_customer, action) VALUES (?, ?, ?);";
			String[] params = {"bookid", "customerid", "action"};
			new LibMySQL().MySQLExecute(libraryActionJSON, query, params);
		}   
	}

	
	public static void UpdateBookStatus(JSONObject libraryActionJSON) throws Exception {
		String query = "UPDATE book SET status = ? WHERE id = ?;";
		String[] params = {"action", "bookid"};
	    new LibMySQL().MySQLExecute(libraryActionJSON, query, params);	
	}


	public static ArrayList<LibAction> GetLibraryActions() throws Exception {
		ArrayList<LibAction> libActionArray = new ArrayList<LibAction>();
        ResultSet resultSet;
        
        String query = new StringBuilder()
                .append("SELECT a.id, b.name AS 'bookname', c.name AS 'customername', a.action, a.timestamp AS 'time' ")
                .append("FROM libraryaction AS a ")
                .append("INNER JOIN book AS b on b.id = a.id_book ")
                .append("INNER JOIN customer AS c ON c.id = a.id_customer ")
                .append("ORDER BY a.timestamp DESC; ")
                .toString();
        
        resultSet = new LibMySQL().MySQLGetRows(null, query, null);
        
        int count = 0;
        while(resultSet.next()) {
        	LibAction libAction = new LibAction(resultSet.getString("id"));
        	libAction.setBookName(resultSet.getString("bookname"));
        	libAction.setCustomerName(resultSet.getString("customername"));
        	libAction.setAction(resultSet.getString("action"));
        	libAction.setTime(resultSet.getString("time"));
        	libActionArray.add(count, libAction);
			count++;
        }
        return libActionArray; 
	}

}
