package com.nitya.hibernate.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

	void singUp(String username, String email, String password, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void login(String email, String password, AsyncCallback<String> callback) throws IllegalArgumentException;

}
