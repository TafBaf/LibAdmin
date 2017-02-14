package com.libadmin.jersey.rest.resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.json.JSONException;
import org.json.JSONObject;

import com.libadmin.jersey.rest.controller.CustomerController;
import com.libadmin.jersey.rest.model.Customer;


// Will map the resource to the URL customer
@Path("/todos")
public class CustomerResource {

    // Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    
    // Return the list of customers to the user in the browser
    @GET
    @Path("/customer/get")
    @Produces(MediaType.TEXT_XML)
    public Response getCustomersBrowser(@Context Request request) {
    	
    	List<Customer> customerArray = new ArrayList<Customer>();
    	try {
    		// TODO : Can't use "instance" here.. problems with ajax sync
    		customerArray = CustomerController.GetAllCustomers();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        CacheControl cc = new CacheControl();
        cc.setMaxAge(1);
        cc.setNoCache(true); 
        cc.setNoStore(true); 

        EntityTag etag = new EntityTag(Integer.toString(customerArray.hashCode()));
        ResponseBuilder builder = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if(builder == null){
            builder = Response.ok(customerArray);
            builder.tag(etag);
        }

        builder.cacheControl(cc);
        return builder.build();
    }



    @GET
    @Path("/customer/get")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response getCustomers(@Context Request request) {
    	
    	List<Customer> customerArray = new ArrayList<Customer>();
    	try {
    		// TODO : Can't use "instance" here.. problems with ajax sync
    		customerArray = CustomerController.GetAllCustomers();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        CacheControl cc = new CacheControl();
        cc.setMaxAge(1);
        cc.setNoCache(true); 
        cc.setNoStore(true); 

        EntityTag etag = new EntityTag(Integer.toString(customerArray.hashCode()));
        ResponseBuilder builder = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if(builder == null){
            builder = Response.ok(customerArray);
            builder.tag(etag);
        }

        builder.cacheControl(cc);
        return builder.build();
    }
    

    @GET
    @Path("/customer/get/{customerId}")
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCustomer(@Context Request request, @PathParam("customerId") String customerId) {
    	
    	Customer customer = new Customer();
    	try {
    		// TODO : Can't use "instance" here.. problems with ajax sync
    		customer = CustomerController.GetCustomer(customerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        CacheControl cc = new CacheControl();
        cc.setMaxAge(1);
        cc.setNoCache(true); 
        cc.setNoStore(true); 

        EntityTag etag = new EntityTag(Integer.toString(customer.hashCode()));
        ResponseBuilder builder = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if(builder == null){
            builder = Response.ok(customer);
            builder.tag(etag);
        }

        builder.cacheControl(cc);
        return builder.build();
    }    
    
    
    @POST
    @Path("/customer/update")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UpdateCustomer(InputStream incomingData, @Context HttpServletResponse servletResponse) {

		JSONObject customerJSON = null;
    	StringBuilder strBuilder = new StringBuilder();

    	try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData, "UTF8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				strBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
			e.printStackTrace();
		}

    	String receivedData = strBuilder.toString();
		System.out.println("Update customer data received: " + receivedData);
		
		try {
			customerJSON = new JSONObject(receivedData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			CustomerController.UpdateCustomer(customerJSON);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// return HTTP response 200 in case of success
		return Response.status(200).entity(strBuilder.toString()).build();
    }   

    
    
    @POST
    @Path("/customer/add")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response AddCustomer(InputStream incomingData, @Context HttpServletResponse servletResponse) {
		JSONObject customerJSON = null;
    	StringBuilder strBuilder = new StringBuilder();

    	try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData, "UTF8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				strBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error parsing input stream");
			e.printStackTrace();
		}

    	String receivedData = strBuilder.toString();
		System.out.println("Add customer data received: " + receivedData);
		
		try {
			customerJSON = new JSONObject(receivedData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			CustomerController.AddCustomer(customerJSON);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// return HTTP response 200 in case of success
		return Response.status(200).entity(strBuilder.toString()).build();
    }     
    

    @GET
    @Path("/customer/delete/{customerId}")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response DeleteCustomer(@Context Request request, @PathParam("customerId") String customerId) {

    	try {
			CustomerController.DeleteCustomer(customerId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// return HTTP response 200 in case of success
		return Response.status(200).entity(customerId).build();
    }     

}