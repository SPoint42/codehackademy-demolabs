package lu.codehackademy.demolabs.transversal.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Handler for managing case in which logout succeed.<br>
 * Return a HTTP 200 to client application.<br>
 * It is used to avoid redirection and indicate clearly that logout succeed because on clide side, a MVC client application is used <br>
 * and then it's on client side that "UI redirection" is managed.
 * 
 * @author Dominique Righetto
 *
 */
@Component("clientNotificationLogoutSuccessHandler")
public class ClientNotificationLogoutSuccessHandler implements LogoutSuccessHandler {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ClientNotificationLogoutSuccessHandler.class);

	/**
	 * {@inheritDoc}
	 *
	 * @see org.springframework.security.web.authentication.logout.LogoutSuccessHandler#onLogoutSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 *      org.springframework.security.core.Authentication)
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		// Trace
		LOG.info("Logout succeed for user '" + authentication.getName() + "'.");
		// Set a HTTP 200 response code
		response.setStatus(HttpServletResponse.SC_OK);
	}

}
