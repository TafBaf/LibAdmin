package com.libadmin.jersey.rest.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import com.libadmin.jersey.rest.model.Customer;


public class CustomerController {

    // TODO : Restructure code here !!!	
    public static ArrayList<Customer> GetAllCustomers() throws Exception {
        List<Customer> customerArray = new ArrayList<Customer>();
        ResultSet resultSet;

        String query = "SELECT id, name, info FROM customer ORDER BY name;";
        resultSet = new LibMySQL().MySQLGetRows(null, query, null);   
    	
        try {
        	int count = 0;
			while (resultSet.next()) {
				Customer customer = new Customer(resultSet.getString("id"), resultSet.getString("name"));
				customer.setInfo(resultSet.getString("info"));
				customerArray.add(count, customer);
				count++;
			}
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return (ArrayList<Customer>) customerArray; 
    }


	public static Customer GetCustomer(String customerId)  throws Exception {
        ResultSet resultSet;
		Customer customer = null;
        
		JSONObject customerJSON = new JSONObject("{\"id\":\"" + customerId + "\"}"); 
        String query = "SELECT id, name, info FROM customer WHERE id = ? LIMIT 1;";
        String[] params = {"id"};
        
        try {
            resultSet = new LibMySQL().MySQLGetRows(customerJSON, query, params); 
            
			while (resultSet.next()) {
				customer = new Customer(resultSet.getString("id"), resultSet.getString("name"));
				customer.setInfo(resultSet.getString("info"));
			}
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}        
         
		return customer;
	}    
	
    
    public static void UpdateCustomer(JSONObject customerJSON) throws Exception  {
        if (customerJSON != null) {
			String query = "UPDATE customer SET name = ?, info = ? WHERE id = ?;";
			String[] params = {"name", "info", "id"};
			
		    new LibMySQL().MySQLExecute(customerJSON, query, params);
		}    	 
    }
    
    
    public static void AddCustomer(JSONObject customerJSON) throws Exception {
    	if (customerJSON != null) {
    		// check if value is numeric
    		if (customerJSON.getString("id").matches("\\d+")) {
    			// This is for tests
    			String query = "INSERT INTO customer (id, name, info) VALUES (?, ?, ?);";
    			String[] params = {"id", "name", "info"};   			
    			new LibMySQL().MySQLExecute(customerJSON, query, params);
    		} else {
    			String query = "INSERT INTO customer (name, info) VALUES (?, ?);";
    			String[] params = {"name", "info"};      			
    			new LibMySQL().MySQLExecute(customerJSON, query, params);
    		}
		}    	 
    }
    

    public static void DeleteCustomer(String customerId) throws Exception {
    	if (customerId != "") {
    		JSONObject customerJSON = new JSONObject("{\"id\":\"" + customerId + "\"}"); 
			String query = "DELETE FROM customer WHERE id = ? LIMIT 1;";
			String[] params = {"id"};
			
		    new LibMySQL().MySQLExecute(customerJSON, query, params);
		}    	 
    }



}
