package lu.codehackademy.demolabs.transversal.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * Handler for managing case in which authentication failed.<br>
 * Return a HTTP 403 to client application.<br>
 * It is used to avoid redirection and indicate clearly that login failed because on clide side, a MVC client application is used <br>
 * and then it's on client side that "UI redirection" is managed.
 * 
 * @author Dominique Righetto
 *
 */
@Component("clientNotificationAuthenticationFailureHandler")
public class ClientNotificationAuthenticationFailureHandler implements AuthenticationFailureHandler {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ClientNotificationAuthenticationFailureHandler.class);

	/**
	 * {@inheritDoc}
	 *
	 * @see org.springframework.security.web.authentication.AuthenticationFailureHandler#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 *      org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException error) throws IOException, ServletException {
		// Trace
		LOG.warn("Authentication fail for user '" + req.getParameter("email") + "' !");
		// Set a HTTP 403 response code
		resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
	}

}
