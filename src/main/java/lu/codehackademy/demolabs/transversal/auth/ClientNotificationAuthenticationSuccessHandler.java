package lu.codehackademy.demolabs.transversal.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Handler for managing case in which authentication succeed.<br>
 * Return a HTTP 200 to client application.<br>
 * It is used to avoid redirection and indicate clearly that login succeed because on clide side, a MVC client application is used <br>
 * and then it's on client side that "UI redirection" is managed.
 * 
 * @author Dominique Righetto
 *
 */
@Component("clientNotificationAuthenticationSuccessHandler")
public class ClientNotificationAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ClientNotificationAuthenticationSuccessHandler.class);

	/**
	 * {@inheritDoc}
	 *
	 * @see org.springframework.security.web.authentication.AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 *      org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth) throws IOException, ServletException {
		// Trace
		LOG.info("Authentication succeed for user '" + auth.getName() + "'.");
		// Set a HTTP 200 response code
		resp.setStatus(HttpServletResponse.SC_OK);
	}

}
