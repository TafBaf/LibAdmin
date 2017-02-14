package com.libadmin.jersey.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Customer {
    private String id;
    private String name;
    private String info;

    public Customer(){}
    
    public Customer (String id, String name){
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
    
    public String getInfo() {
    	return info;
    }
    
    public void setInfo(String info) {
    	this.info = info;
    }
}