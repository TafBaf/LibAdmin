package com.libadmin.jersey.maven.test;


import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import com.libadmin.jersey.rest.model.Customer;
import com.libadmin.jersey.rest.resources.CustomerResource;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CustomerResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
    	enable(TestProperties.LOG_TRAFFIC);
    	enable(TestProperties.DUMP_ENTITY);
    	return new ResourceConfig(CustomerResource.class);
    }

    
	@Test
	public void TestCreate(){
	    Customer customer = new Customer("1", "Integratsioonitest");
	    customer.setInfo("Lisatud testi poolt.");
	    Response output = target("todos/customer/add").request().post(Entity.entity(customer, MediaType.APPLICATION_JSON));
	
	    Assert.assertEquals("Should return status 200", 200, output.getStatus());
	}
	

	@Test
	public void TestGetAll() {
	    Response output = target("todos/customer/get/").request(MediaType.APPLICATION_JSON).get();
	    Assert.assertEquals("should return status 200", 200, output.getStatus());
	    Assert.assertNotNull("Should return list", output.getEntity());
	}
	

	@Test
	public void TestUpdate(){
	    Customer customer = new Customer("1", "Integratsioonitest");
	    customer.setInfo("Muudetud testi poolt");
	    Response output = target("todos/customer/update").request().post(Entity.entity(customer, MediaType.APPLICATION_JSON));
	    Assert.assertEquals("Should return status 200", 200, output.getStatus());
	}

	
	@Test
    public void TestGetById(){
		Customer customer = target("todos/customer/get/1").request(MediaType.APPLICATION_JSON).get(Customer.class);
        Assert.assertEquals("Integratsioonitest", customer.getName());		
    }
	
	
	@Test
	public void TestDelete() throws JSONException{
	    Response output = target("todos/customer/delete/1").request().get();
	    Assert.assertEquals("Should return status 200", 200, output.getStatus());
	}

}