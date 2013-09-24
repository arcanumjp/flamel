package jp.arcanum.click;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorServlet extends HttpServlet {

	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {

		arg1.sendRedirect( ArUtil.APPNAME + "/click/not-found.htm");
		
	}

	
	
	
	
}
