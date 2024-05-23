package com.nitya.hibernate.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.nitya.hibernate.client.GreetingService;
import com.nitya.hibernate.dao.UserDAO;
import com.nitya.hibernate.model.User;
import com.nitya.hibernate.shared.FieldVerifier;
import com.nitya.hibernate.util.HibernateUtil;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	public UserDAO userDAO;

	public GreetingServiceImpl() {
		this.userDAO = new UserDAO(HibernateUtil.getSessionFactory());
	}

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>"
				+ userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	@Override
	public String singUp(String username, String email, String password) throws IllegalArgumentException {

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);

		boolean isSaved = userDAO.saveUser(user);
		if (isSaved) {
			return "success";
		}

		return "failure";
	}

	@Override
	public String login(String email, String password) throws IllegalArgumentException {

		User user = new User();
		user.setEmail(email);
		user.setPassword(password);

		boolean isLogin = userDAO.login(user);
		System.out.println("isLogin: "+isLogin);
		if (isLogin) {
			return "success";
		}

		return "failure";
	}
}
