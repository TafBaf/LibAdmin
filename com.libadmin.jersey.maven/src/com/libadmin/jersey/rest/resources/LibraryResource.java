package com.libadmin.jersey.rest.resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

import com.libadmin.jersey.rest.controller.LibraryController;
import com.libadmin.jersey.rest.model.LibAction;;


// Will map the resource to the URL customer
@Path("/todos")
public class LibraryResource {

    // Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context UriInfo uriInfo;
    @Context Request request;
    

    @GET
    @Path("/library/get")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getLibraryActions(@Context Request request) {
    	
    	ArrayList<LibAction> libActionArray = new ArrayList<LibAction>();
    	try {
    		// TODO : Can't use "instance" here.. problems with ajax sync
    		libActionArray = LibraryController.GetLibraryActions();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        CacheControl cc = new CacheControl();
        cc.setMaxAge(1);
        cc.setNoCache(true); 
        cc.setNoStore(true); 
       // cc.setPrivate(true);

        EntityTag etag = new EntityTag(Integer.toString(libActionArray.hashCode()));
        ResponseBuilder builder = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if(builder == null){
            builder = Response.ok(libActionArray);
            builder.tag(etag);
        }

        builder.cacheControl(cc);
        return builder.build();
    }
    
    
    @POST
    @Path("/library/add")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response AddLibraryAction(InputStream incomingData, @Context HttpServletResponse servletResponse) {

		JSONObject libraryActionJSON = null;
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
		System.out.println("Add library action data received: " + receivedData);
		
		try {
			libraryActionJSON = new JSONObject(receivedData);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			LibraryController.AddLibraryAction(libraryActionJSON);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			LibraryController.UpdateBookStatus(libraryActionJSON);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		// return HTTP response 200 in case of success
		return Response.status(200).entity(strBuilder.toString()).build();
    }      
    
}
