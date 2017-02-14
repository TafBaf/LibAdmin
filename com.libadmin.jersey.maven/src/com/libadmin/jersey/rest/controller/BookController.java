package com.libadmin.jersey.rest.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

import com.libadmin.jersey.rest.controller.LibMySQL;
import com.libadmin.jersey.rest.model.Book;



public class BookController {

    // TODO : Restructure code here !!!	
    public static ArrayList<Book> GetAllBooks() throws Exception {
        List<Book> bookArray = new ArrayList<Book>();
        ResultSet resultSet;

        String query = "SELECT id, name, author, description, status FROM book ORDER BY name;";
        
        resultSet = new LibMySQL().MySQLGetRows(null, query, null);       	
        try {
        	int count = 0;
			while (resultSet.next()) {
				Book book = new Book(resultSet.getString("id"), resultSet.getString("name"));
				book.setAuthor(resultSet.getString("author"));
				book.setDescription(resultSet.getString("description"));
				book.setStatus(resultSet.getString("status"));
				bookArray.add(count, book);
				count++;
			}
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return (ArrayList<Book>) bookArray; 
    }

    
    
    public static void UpdateBook(JSONObject bookJSON) throws Exception  {
        if (bookJSON != null) {
			String query = "UPDATE book SET name = ?, author = ?, description = ?, status = ? WHERE id = ?;";
			String[] params = {"name", "author", "description", "status", "id"};
			
		    new LibMySQL().MySQLExecute(bookJSON, query, params);
		}    	 
    }
    
    
    public static void AddBook(JSONObject bookJSON) throws Exception {
    	if (bookJSON != null) {
			String query = "INSERT INTO book (name, author, description, status) VALUES (?, ?, ?, ?);";
			String[] params = {"name", "author", "description", "status"};
			
		    new LibMySQL().MySQLExecute(bookJSON, query, params);
		}    	 
    }
    

    public static void DeleteBook(JSONObject bookJSON) throws Exception {
    	if (bookJSON != null) {
			String query = "DELETE FROM book WHERE id = ? LIMIT 1;";
			String[] params = {"id"};
			
		    new LibMySQL().MySQLExecute(bookJSON, query, params);
		}    	 
    }
    

}