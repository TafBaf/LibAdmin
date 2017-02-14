package com.libadmin.jersey.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Book {
    private String id;
    private String name;
    private String author;
    private String description;
    private String status;

    public Book(){}

    public Book (String id, String name){
        this.id   = id;
        this.name = name;
    }
    
    public String getId() {
    	return id;
    }
    
    public void setId(String id) {
    	this.id = id;
    }

    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }    
    
    public String getAuthor() {
    	return author;
    }
    
    public void setAuthor(String author) {
    	this.author = author;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }

    public String getStatus() {
    	return status;
    }
    
    public void setStatus(String status) {
    	this.status = status;
    }
}