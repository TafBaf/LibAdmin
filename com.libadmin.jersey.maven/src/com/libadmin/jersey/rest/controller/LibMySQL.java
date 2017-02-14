package com.libadmin.jersey.rest.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;

import org.json.JSONObject;

import com.mysql.jdbc.PreparedStatement;


public class LibMySQL {
	
	public static Connection MySQLConnect () throws SQLException {
		Connection sqlConnection = null;
		
		try {
			String sqlHost = "jdbc:mysql://localhost:3306/library?characterEncoding=UTF-8";	
			Properties properties = new Properties();
			properties.setProperty("user", "root");
			properties.setProperty("password", "root");
			// TODO : add encoding to setprop
			// properties.setProperty("useSSL", "false");
			// properties.setProperty("autoReconnect", "true");
			
		    try {
		        Class.forName("com.mysql.jdbc.Driver").newInstance();
		    } catch (ClassNotFoundException e) {
		        throw new Exception("No database");
		    }

			sqlConnection = DriverManager.getConnection(sqlHost, properties);
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			System.err.println(e);
		}
		return sqlConnection;	
	}	

	
	public static SQLException MySQLPing (Connection sqlConnection) throws Exception {
		Statement statement   = null;
		String query = "/* ping */ SELECT 1";
		
		if (sqlConnection == null) {
			sqlConnection = MySQLConnect();			
		}

		try {
			statement  = sqlConnection.createStatement();
			statement.executeQuery(query);
		} catch (Exception e) {
            if (e instanceof SQLException) {
               return (SQLException) e;
            } else {
        		System.out.println("Unexpected MySQL Ping error! " + e.toString());
        		return new SQLException("MySQL ping (SELECT 1) failed: " + e.toString());
            }
         } 
		return null;		
	}
	
	
	public ResultSet MySQLGetRows (JSONObject objJSON, String query, String[] params) throws Exception {
		ResultSet resultSet = null;
		Connection sqlConnection = null;
		
		try {
			sqlConnection = MySQLConnect();

	    	SQLException MySQLPing = null;
	    	MySQLPing = MySQLPing(sqlConnection);
			if (MySQLPing != null) {
		    	throw new SQLException(MySQLPing);
		    }
			
			PreparedStatement prepStatement = (PreparedStatement) sqlConnection.prepareStatement( query );
			if (objJSON != null && params != null) {
				for (int i = 0; i < params.length; i++) {
					prepStatement.setString( i + 1, objJSON.getString(params[i]) );      
				}
			}	

			System.out.println("Query: " + prepStatement.toString());
			resultSet = prepStatement.executeQuery();			
			
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			System.err.println(e);
		} 
	
		return resultSet;
	}
	
	
	public void MySQLExecute (JSONObject objJSON, String query, String[] params) throws Exception {
		Connection sqlConnection = null;
		PreparedStatement prepStatement = null;
		
		try {
			sqlConnection = MySQLConnect();
			
	    	SQLException MySQLPing = null;
	    	MySQLPing = MySQLPing(sqlConnection);
			if (MySQLPing != null) {
		    	throw new SQLException(MySQLPing);
		    }
			
			prepStatement = (PreparedStatement) sqlConnection.prepareStatement( query );
			for (int i = 0; i < params.length; i++) {
				prepStatement.setString( i + 1, objJSON.getString(params[i]) );      
			}

			System.out.println("MySQL execute query: " + prepStatement.toString());
			prepStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			MySQLCloseConnection(sqlConnection, prepStatement);
		}
	}
	
	public void MySQLCloseConnection(Connection sqlConnection, PreparedStatement prepStatement) throws SQLException  {
        if (prepStatement != null) {
            try {
            	prepStatement.close();
            } catch (SQLException ignore) {
                // ignore, as we can't do anything about it here
            }
            prepStatement = null;
        }

        if (sqlConnection != null) {
            try {
           	 sqlConnection.close();
            } catch (SQLException ignore) {
                // ignore, as we can't do anything about it here
            }
            sqlConnection = null;
        }
	}

}
