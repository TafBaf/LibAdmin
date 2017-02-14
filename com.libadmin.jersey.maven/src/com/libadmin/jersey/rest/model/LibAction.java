package com.libadmin.jersey.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LibAction {
    private String id;
    private String bookname;
    private String customername;
    private String action;
    private String time;

    public LibAction(){}

    public LibAction (String id){
        this.id = id;
    }    
    
    public String getId() {
    	return id;
    }

    public void setId(String id) {
    	this.id = id;
    }


    public String getBookName() {
    	return bookname;
    }
    
    public void setBookName(String bookname) {
    	this.bookname = bookname;
    }    
    
    public String getCustomerName() {
    	return customername;
    }
    
    public void setCustomerName(String customername) {
    	this.customername = customername;
    }

    public String getAction() {
    	return action;
    }
    
    public void setAction(String action) {
    	this.action = action;
    }    
    
    public String getTime() {
    	return time;
    }
    
    public void setTime(String time) {
    	this.time = time;
    }
}