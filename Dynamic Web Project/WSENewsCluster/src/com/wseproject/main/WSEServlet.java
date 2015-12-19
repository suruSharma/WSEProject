package com.wseproject.main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Saurabh Pujar(ssp437)
 *
 */
@WebServlet(description = "Servlet class to execute clustering and power front end", urlPatterns = { "/WSEServlet" })
public class WSEServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public WSEServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String count = request.getParameter("clusterCount");
		String region = request.getParameter("region");
		int clusterCount = (count == null || count == "") ? 5 : Integer.parseInt(count);
		int location = (region == null || region == "") ? 5 : Integer.parseInt(region);
		PrintWriter out = response.getWriter();
		String json = (new NewsAggregator()).getJson(clusterCount,location);
		out.println(json);
	}

}
