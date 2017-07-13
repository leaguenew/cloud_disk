package com.echoii.cloud.platform.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;



/**
 * Servlet implementation class RedirectServlet
 */
public class PageRedirect extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger  log = Logger.getLogger( PageRedirect.class );

    /**
     * Default constructor. 
     */
    public PageRedirect() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURI().toLowerCase();
		
		
		log.debug( "url = " + url );
		
		String target = "index.jsp";
		if( url.contains("/page/file") ){
			target = "file.jsp";
		}else if( url.contains("/page/group") ){
			target = "group.jsp";
		}
		
		log.debug("target = " + target );
		
		RequestDispatcher dispatcher = request.getRequestDispatcher( target );
		dispatcher.forward(request, response); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
