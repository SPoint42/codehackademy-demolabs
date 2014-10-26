package lu.codehackademy.demolabs.transversal.auth;

import lu.codehackademy.demolabs.transversal.util.Utilities;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Password encoder that use our method to create a digest of a password
 * 
 * @author Dominique Righetto
 *
 */
@Component("saltedDigestPasswordEncoder")
public class SaltedDigestPasswordEncoder implements PasswordEncoder {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DBUserDetailsService.class);

	/**
	 * {@inheritDoc}
	 *
	 * @see org.springframework.security.crypto.password.PasswordEncoder#encode(java.lang.CharSequence)
	 */
	@Override
	public String encode(CharSequence rawPassword) {
		String encodedPassword = null;

		// Check content existance
		if (StringUtils.isBlank(rawPassword)) {
			throw new IllegalArgumentException("Password cannot be NULL or empty !");
		}

		// Try password encoding
		try {
			encodedPassword = Utilities.createPasswordHash((String) rawPassword);
		}
		catch (Exception e) {
			LOG.error("Error during password en coding !", e);
			// Do not let error continue authentication process...
			throw new RuntimeException("Error occur during password encoding !", e);
		}

		return encodedPassword;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.springframework.security.crypto.password.PasswordEncoder#matches(java.lang.CharSequence, java.lang.String)
	 */
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		boolean matches = false;

		// Check content existance
		if (StringUtils.isBlank(encodedPassword) || StringUtils.isBlank(rawPassword)) {
			return matches;
		}

		// Try encoding of the raw password
		String rawPasswordEncoded = this.encode(rawPassword);

		// Compare both encoded password
		matches = rawPasswordEncoded.equals(encodedPassword);

		return matches;
	}
}
