package com.libadmin.jersey.rest.resources;

import java.io.IOException;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.libadmin.jersey.rest.controller.LibMySQL;

//Will map the resource to the URL books
@Path("/todos")
public class HealthCheck {

    // Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;	
	
    @GET
    @Path("/healthcheck")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response getHealtCheck(@Context Request request) throws IOException  {
    	SQLException pingExeption = null;
    	String sqlStat = "";
    	
    	try {
			pingExeption = LibMySQL.MySQLPing(null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (pingExeption != null) {
			sqlStat = "fail";
    	} else {
    		sqlStat = "pass";
    	}
		
    	String healtStatus = "{\"server\":\"pass\",\"mysql\":\"" + sqlStat + "\"}";
    	
    	CacheControl cc = new CacheControl();
        cc.setMaxAge(1);
        cc.setNoCache(true); 
        cc.setNoStore(true); 

        EntityTag etag = new EntityTag(Integer.toString(healtStatus.hashCode()));
        ResponseBuilder builder = request.evaluatePreconditions(etag);

        // cached resource did change -> serve updated content
        if(builder == null){
            builder = Response.ok(healtStatus);
            builder.tag(etag);
        }

        builder.cacheControl(cc);
        return builder.build();
    } 


}
