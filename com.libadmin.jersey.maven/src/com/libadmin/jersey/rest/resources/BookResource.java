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

import com.libadmin.jersey.rest.controller.BookController;
import com.libadmin.jersey.rest.model.Book;


// Will map the resource to the URL books
@Path("/todos")
public class BookResource {

    // Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    
    @GET
    @Path("/book/get")
    @Produces(MediaType.TEXT_XML)
    public Response getBooksBrowser(@Context Request request) {
    	
    	List<Book> booksArray = new ArrayList<Book>();
    	try {
    		// TODO : Can't use "instance" here.. problems with ajax sync
			booksArray = BookController.GetAllBooks();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        CacheControl cc = new CacheControl();
        cc.setMaxAge(1);
        cc.setNoCache(true); 
        cc.setNoStore(true); 
       // cc.setPrivate(true);

        EntityTag etag = new EntityTag(Integer.toString(booksArray.hashCode()));
        ResponseBuilder builder = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if(builder == null){
            builder = Response.ok(booksArray);
            builder.tag(etag);
        }

        builder.cacheControl(cc);
        return builder.build();
    }



    @GET
    @Path("/book/get")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response getBooks(@Context Request request)  {
    	
    	List<Book> booksArray = new ArrayList<Book>();
    	try {
    		// TODO : Can't use "instance" here.. problems with ajax sync
			booksArray = BookController.GetAllBooks();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        CacheControl cc = new CacheControl();
        cc.setMaxAge(1);
        cc.setNoCache(true); 
        cc.setNoStore(true); 
       // cc.setPrivate(true);

        EntityTag etag = new EntityTag(Integer.toString(booksArray.hashCode()));
        ResponseBuilder builder = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if(builder == null){
            builder = Response.ok(booksArray);
            builder.tag(etag);
        }

        builder.cacheControl(cc);
        return builder.build();
    }    
    

    @POST
    @Path("/book/update")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UpdateBook(InputStream incomingData, @Context HttpServletResponse servletResponse)  {

		JSONObject bookJSON = null;
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
		System.out.println("Update book data received: " + receivedData);
		
		try {
			bookJSON = new JSONObject(receivedData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			BookController.UpdateBook(bookJSON);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// return HTTP response 200 in case of success
		return Response.status(200).entity(strBuilder.toString()).build();
    }   

    
    
    @POST
    @Path("/book/add")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response AddBook(InputStream incomingData, @Context HttpServletResponse servletResponse) {

		JSONObject bookJSON = null;
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
		System.out.println("Add book data received: " + receivedData);
		
		try {
			bookJSON = new JSONObject(receivedData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			BookController.AddBook(bookJSON);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// return HTTP response 200 in case of success
		return Response.status(200).entity(strBuilder.toString()).build();
    }     
    


    @POST
    @Path("/book/delete")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response DeleteBook(InputStream incomingData, @Context HttpServletResponse servletResponse) {

		JSONObject bookJSON = null;
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
		System.out.println("Delete book ID received: " + receivedData);
		
		try {
			bookJSON = new JSONObject(receivedData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		try {
			BookController.DeleteBook(bookJSON);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// return HTTP response 200 in case of success
		return Response.status(200).entity(strBuilder.toString()).build();
    }     

}