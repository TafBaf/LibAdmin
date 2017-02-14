/**
 * 
 *   @LibAdmin 
 *   @2017
 *
 */



// TODO : Create a class !!
$(document).ready(function(){
	
	$.ajaxSetup({
	    scriptCharset: "utf-8",
	    contentType: "application/json; charset=utf-8",
    	cache: false
	});
	
	// Initialize UI -->
	runHealthCheck();			
	createBookList();
	// TODO : Following functions can be executed by catching tab click. 
	createCustomerList();	
	createLibraryActionBookList();
	createLibraryActionCustomerList()
	// TODO : Create dynamic action list from database
	createLibraryActionLogList();
	
	// Books -->
	$(document).on('click', '#addBookBtn', function() {
		addBook();
	});
	
	$(document).on('click', '#updateBookBtn', function() {
		updateBook($(this).val());
	});
	
	// TODO : Add user prompt before delete
	$(document).on('click', '#deleteBookBtn', function() {
		deleteBook($(this).val());
	});	
	

	// Customers -->	
	$(document).on('click', '#addCustomerBtn', function() {
		addCustomer();
	});
	
	$(document).on('click', '#updateCustomerBtn', function() {
		updateCustomer($(this).val());
	});

	$(document).on('click', '#deleteCustomerBtn', function() {
		deleteCustomer($(this).val());
	});
	
	
	// Library actions -->
	$(document).on('click', '#addLibraryActionBtn', function() {
		addLibraryActionBtn();
	});
	
});



function runHealthCheck() {
	$.ajax({
	    type: "GET",
	    url: "rest/todos/healthcheck",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
		cache: false,
		success: function(data,  textStatus) {
			var errorMsg = "";
			var msg      = "";
			$.each(data, function(key, val) {
				if (key == "server"){
					if (val == "pass"){
						msg += "Server check: Pass.   ";
					} else {
						errorMsg += "Server check: Failed.    ";
					}
				}

				if (key == "mysql"){
					if (val == "pass"){
						msg += "MySQL connection check: Pass.     ";
					} else {
						errorMsg += "MySQL connection check: Failed.   ";
					}
				}
			})

			if (msg != "") {
				showInfoBox(msg, true);		
			}
			if (errorMsg != "") {
				showErrorBox(errorMsg + "   " + msg, true);
			}
         },
         error: function (jqXHR, exception, errorThrown) {
         	ajaxErrorMsgHandler(jqXHR, exception, errorThrown);
         } 
	});
}



/************************************
 * 
 *     Customers related logic
 * 
 ************************************/


/* Generates customers administration list */
function createCustomerList() {
	$.ajax({
	    type: "GET",
	    url: "rest/todos/customer/get",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
		cache: false,
		success: function(data,  textStatus) {
			var items = [];
			items.push("<table id='customerTable'>");
			items.push("<tr><td colspan = '4'><h2>All Customers</h2></td></tr>");
			items.push("<tr><td>#</td><td>Name</td><td>Info</td></tr>");
			$.each(data, function(key, val) {
				items.push("<tr><td>" + (key + 1) + ".</td>  "
						  + "<td><input type='text' id='customer_edit_name_" + val.id + "' name='customer_edit_name' value='" + val.name + "'></td>" 
						  + "<td><input type='text' id='customer_edit_info_" + val.id + "'name='customer_edit_info' value='" + val.info + "' size=70></td>"
						  + "<td><button id='updateCustomerBtn' value='" + val.id + "' class='ui-button ui-widget ui-corner-all'>Update<span class='ui-icon ui-icon-pencil'></span></button></td>"
						  + "<td><button id='deleteCustomerBtn' value='" + val.id + "' class='ui-button ui-widget ui-corner-all'>Delete<span class='ui-icon ui-icon-close'></span></button></td>"		    		  
						  + "</tr>" );
			});
			items.push('</table>');
			    
			$( "#allCustomers" ).html(items.join(""));   	
         },
         error: function (jqXHR, exception, errorThrown) {
         	ajaxErrorMsgHandler(jqXHR, exception, errorThrown);
         } 
	});	
}


/* Takes data from "Add a new customer" fields and creates new database record */
function addCustomer() {
	var preparedData = getCustomerDataForAddAsJson(); 
	
	$.ajax({
	    type: "POST",
	    url: "rest/todos/customer/add",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
	    data: preparedData,
	    success: function(data, textStatus, jqXHR) {
	    	showInfoBox("Customer added successfully.", true);
	    	createCustomerList();
	    },
	    error: function (jqXHR, exception, errorThrown) {
        	ajaxErrorMsgHandler(jqXHR, exception, errorThrown);
        } 
	});	
}


/* Takes data from customer list and updates related customer record */
function updateCustomer(customerId) {
	var customerData = getCustomerDataForUpdateAsJson(customerId)

	$.ajax({
	    type: "POST",
	    url: "rest/todos/customer/update",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
	    data: customerData,
	    success: function(data, textStatus, jqXHR) {
	    	showInfoBox("Customer updated successfully.", true);	
	    	createCustomerList;
	    },
	    error: function (jqXHR, exception, errorThrown) {
        	ajaxErrorMsgHandler(jqXHR, exception, errorThrown);
        } 
	});	
}


/* Deletes customer record from database */
function deleteCustomer(CustomerId) {
	$.ajax({
	    type: "GET",
	    url: "rest/todos/customer/delete/" + CustomerId,
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
	    success: function(json) {
	    	showInfoBox("Customer deleted successfully.", true);
	    	createCustomerList();
	    },
	    error: function (jqXHR, exception, errorThrown) {
        	ajaxErrorMsgHandler(jqXHR, exception, errorThrown);
        }    
	});	
}


/* Creates select menu widget to Library tab */
function createLibraryActionCustomerList() {
	$.ajax({
	    type: "GET",
	    url: "rest/todos/customer/get",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
		cache: false,
		success: function(data,  textStatus) {
			var items = [];
			items.push("<select id='library_customer_list'>");
			items.push("<option></option>");
			$.each(data, function(key, val) {
				items.push("<option value='" + val.id + "'>" + val.name + "</option>");
			});
			items.push('</select>');
			    
			$( "#library_add_customer" ).html(items.join(""));   	
         },
         error: function (jqXHR, exception, errorThrown) {
         	ajaxErrorMsgHandler(jqXHR, exception, errorThrown);
         } 
	});		
}


function getCustomerDataForAddAsJson() {
	var custmerData = '{"id":"", '
		          + '"name":"' + $('#customer_add_name').val() + '", '
				  + '"info":"' + $('#customer_add_info').val() + '"}';
	return custmerData;
}


function getCustomerDataForUpdateAsJson(customerID) {
	var customerData = '{"id":"' + customerID + '", ' 
					+ '"name":"' + $('#customer_edit_name_' + customerID).val() + '", '
					+ '"info":"' + $('#customer_edit_info_' + customerID).val() + '"}';
	return customerData;
}



/************************************
 * 
 *      Books related logic
 * 
 ************************************/


/* Generates books administration list */
function createBookList() {
	$.ajax({
	    type: "GET",
	    url: "rest/todos/book/get",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
		cache: false,
		success: function(data,  textStatus) {
			var items = [];
			items.push("<table id='bookTable'>");
			items.push("<tr><td colspan = '4'><h2>All books</h2></td></tr>");
			items.push("<tr><td>#</td><td>Name</td><td>Author</td><td>Description</td><td>Staus</td></tr>");
			$.each(data, function(key, val) {
				items.push("<tr><td>" + (key + 1) + ".</td>  "
						  + "<td><input type='text' id='book_edit_name_" + val.id + "' name='name' value='" + val.name + "'></td>" 
						  + "<td><input type='text' id='book_edit_author_" + val.id + "'name='author' value='" + val.author + "'></td>"
						  + "<td><input type='text' id='book_edit_description_" + val.id + "' name='description' value='" + val.description + "'></td>"
						  + "<td><select id='book_edit_status_" + val.id + "'><option>" + val.status + "</option><option>----</option><option>On shelf</option><option>Lent out</option></select></td></td>"
						  + "<td><button id='updateBookBtn' value='" + val.id + "' class='ui-button ui-widget ui-corner-all'>Update<span class='ui-icon ui-icon-pencil'></span></button></td>"
						  + "<td><button id='deleteBookBtn' value='" + val.id + "' class='ui-button ui-widget ui-corner-all'>Delete<span class='ui-icon ui-icon-close'></span></button></td>"		    		  
						  + "</tr>" );
			});
			items.push('</table>');
			    
			$( "#allBooks" ).html(items.join(""));   	
         },
         error: function (jqXHR, exception, errorThrown) {
         	ajaxErrorMsgHandler(jqXHR, exception, errorThrown);
         } 
	});	
}


/* Takes data from "Add a new book" fields and creates new database record */
function addBook() {
	var bookData = getBookDataAsJsonFromAddList() 
	
	$.ajax({
	    type: "POST",
	    url: "rest/todos/book/add",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
	    data: bookData,
	    success: function(json) {
	    	showInfoBox("Book added successfully.", true);
	    	createBookList();
	    },
	    error: function (jqXHR, exception, errorThrown) {
        	ajaxErrorMsgHandler(jqXHR, exception, errorThrown);
        } 
	});	
}


/* Takes data from book list and updates related book record */
function updateBook(bookID) {
	var bookData = getBookDataAsJsonFromList(bookID) 
	
	$.ajax({
	    type: "POST",
	    url: "rest/todos/book/update",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
	    data: bookData,
	    success: function(json) {
	    	showInfoBox("Book updated successfully.", true);	
	    	createBookList();
	    },
	    error: function (jqXHR, exception, errorThrown) {
        	ajaxErrorMsgHandler(jqXHR, exception, errorThrown);
        } 
	});	
}


/* Deletes book record from database */
function deleteBook(bookID) {
	$.ajax({
	    type: "POST",
	    url: "rest/todos/book/delete",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
	    data: '{"id":"' + bookID + '"}',
	    success: function(json) {
	    	showInfoBox("Book deleted successfully.", true);
	    	createBookList();
	    },
        error: function (jqXHR, exception, errorThrown) {
        	ajaxErrorMsgHandler(jqXHR, exception, errorThrown);
        }    
	});	
}



function createLibraryActionBookList() {
	$.ajax({
	    type: "GET",
	    url: "rest/todos/book/get",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
		cache: false,
		success: function(data,  textStatus) {
			var items = [];
			items.push("<select id='library_book_list'>");
			items.push("<option></option>");
			$.each(data, function(key, val) {
				items.push("<option value='" + val.id + "'>" + val.name + "</option>");
			});
			items.push('</select>');
			    
			$( "#library_add_book" ).html(items.join(""));   	
         },
         error: function (jqXHR, exception, errorThrown) {
         	ajaxErrorMsgHandler(jqXHR, exception, errorThrown);
         } 
	});		
}



function getBookDataAsJsonFromList(bookID) {
	var bookData = '{"id":"' + bookID + '", '
				  + '"name":"' + $('#book_edit_name_' + bookID).val() + '", '
				  + '"author":"' + $('#book_edit_author_' + bookID).val() + '", '
				  + '"description":"' + $('#book_edit_description_' + bookID).val() + '", '
				  + '"status":"' + $('#book_edit_status_' + bookID).val() + '"}';
	return bookData;
}


function getBookDataAsJsonFromAddList() {
	var bookData = '{"name":"' + $('#book_add_name').val() + '", '
				  + '"author":"' + $('#book_add_author').val() + '", '
				  + '"description":"' + $('#book_add_description').val() + '", '
				  + '"status":"' + $('#book_add_status').val() + '"}';
	return bookData;
}



/************************************
 * 
 *     Library Actions
 * 
 ************************************/

function addLibraryActionBtn(){
	var preparedrData = getLibActionDataForAddAsJson();
	
	$.ajax({
	    type: "POST",
	    url: "rest/todos/library/add",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
	    data: preparedrData,
	    success: function(json) {
	    	showInfoBox("Library action added successfully.", true);	
	    	createBookList();
	    	// TODO : Clear fields 
	    	createLibraryActionLogList();
	    },
	    error: function (jqXHR, exception, errorThrown) {
        	ajaxErrorMsgHandler(jqXHR, exception, errorThrown);
        } 
	});		
}


function getLibActionDataForAddAsJson() {
	var preparedrData = '{"bookid":"' + $('#library_book_list').val() + '", '
					  + '"customerid":"' + $('#library_customer_list').val() + '", '
				      + '"action":"' + $('#library_action_list').val() + '"}';
	return preparedrData;
}


function createLibraryActionLogList() {
	$.ajax({
	    type: "GET",
	    url: "rest/todos/library/get",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
		cache: false,
		success: function(data, textStatus) {
			var items = [];
			items.push("<br />");
			items.push("<table>");
			items.push("<tr><td colspan = '4'><h2>Library action logs</h2></td></tr>");
			items.push("<tr><td><h4>Book name</h4></td><td><h4>Customer name</h4></td><td><h4>Action</h4></td><td><h4>Time</h4></td></tr>");
			$.each(data, function(key, val) {
				items.push("<tr>");
				items.push("<td width='25%' id=><strong>" + val.bookName + "</strong></td>");
				items.push("<td width='25%'>" + val.customerName + "</td>");
				items.push("<td width='25%'>" + val.action + "</td>");				
				items.push("<td width='25%'>" + val.time + "</td>");
				items.push("</tr>");
			});
			items.push('</table>');
			    
			$( "#libraryActionLog" ).html(items.join(""));   	
         },
         error: function (jqXHR, exception, errorThrown) {
         	ajaxErrorMsgHandler(jqXHR, exception, errorThrown);
         } 
	});		
}


/************************************
 * 
 *     Helpers
 * 
 ************************************/


function ajaxErrorMsgHandler(jqXHR, exception, errorThrown) {
	var errorMsg = "";
     if (jqXHR.status === 0) {
    	 errorMsg = 'Not connect.\n Verify Network.';
     } else if (jqXHR.status == 404) {
    	 errorMsg = 'Requested page not found. [404]';
     } else if (jqXHR.status == 500) {
    	 errorMsg = 'Internal Server Error [500].';
     } else if (exception === 'parsererror') {
    	 errorMsg = 'Requested JSON parse failed.';
     } else if (exception === 'timeout') {
    	 errorMsg = 'Time out error.';
     } else if (exception === 'abort') {
    	 errorMsg = 'Ajax request aborted.';
     } else {
    	 errorMsg = 'Uncaught Error.\n' + jqXHR.responseText;
     }
 	if (errorMsg != "") {
		showErrorBox(errorMsg, true);		
	}  	
}


//TODO : Add clear previous content or append()
function showErrorBox(errorMsg, init) {
	msgBox = "<div class='ui-state-error ui-corner-all' style='padding: 0 .7em;'>"
		+ "<p><span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span>"
		+ "<strong>Alert:</strong> " + errorMsg + "</p>";
	$( "#errorMsgBox" ).html(msgBox);		
}


//TODO : Add clear previous content or append()
function showInfoBox(msg, init) {
	msgBox = "<div class='ui-state-highlight ui-corner-all' style='margin-top: 20px; padding: 0 .7em;'>"
		   + "<p><span class='ui-icon ui-icon-info' style='float: left; margin-right: .3em;'></span>"
		   + "<strong>Status:</strong> " + msg + "</p></div>";
	$( "#statusMsgBox" ).html(msgBox);		
}



