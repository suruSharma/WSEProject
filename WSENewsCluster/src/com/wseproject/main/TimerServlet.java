package com.wseproject.main;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Saurabh Pujar(ssp437) , Suruchi Sharma(sss665)
 *
 */
@WebServlet(description = "Start of the crawler", urlPatterns = { "/CrawlerServlet" })
public class TimerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public TimerServlet() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		new NewsCrawler().startCrawler();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Nothing to do. This servlet is just to schedule the timer that
		// executes the crawler
	}

}
