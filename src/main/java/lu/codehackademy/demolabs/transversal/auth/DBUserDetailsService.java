package lu.codehackademy.demolabs.transversal.auth;

import java.util.Collection;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import lu.codehackademy.demolabs.dal.DataProvider;
import lu.codehackademy.demolabs.transversal.Constants;
import lu.codehackademy.demolabs.transversal.vo.CHStudent;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * "UserDetailsService" custom implementation using custom database to load user (student) infos.
 * 
 * @author Dominique Righetto
 * @see "http://stackoverflow.com/a/18224564"
 * 
 */
@Component("dbUserDetailsService")
public class DBUserDetailsService implements UserDetailsService {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DBUserDetailsService.class);

	/** Data accessor */
	@Autowired
	@Qualifier("dataProvider")
	private DataProvider dataProvider = null;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String email = username;// We use email as username
		User user = null;
		try {
			// Check email presence
			if (StringUtils.isBlank(email)) {
				throw new UsernameNotFoundException("Cannot load user infos for NULL or empty username !");
			}

			// Apply security check on email
			CHStudent tmpForSecCheck = new CHStudent();
			tmpForSecCheck.setEmail(email);
			tmpForSecCheck.setPassword("XXXXXXXXXXXXXXXXX");
			tmpForSecCheck.setLastnameFirstname("XXXXXXXXXXXXXXXXX");
			tmpForSecCheck.setMotivation("XXXXXXXXXXXXXXXXX");
			Validator val = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<CHStudent>> constraintViolations = val.validate(tmpForSecCheck);
			if (constraintViolations.size() > 0) {
				StringBuilder buffer = new StringBuilder("Input validation failed to properties : ");
				for (ConstraintViolation<CHStudent> constraintViolation : constraintViolations) {
					buffer.append("'");
					buffer.append(constraintViolation.getPropertyPath());
					buffer.append("' ; ");
				}
				LOG.warn(buffer.toString());
				throw new UsernameNotFoundException("Input validation failed for username '" + email + "' !");
			}

			// Try to load user profile
			CHStudent s = this.dataProvider.retrieveStudent(email, true);
			if (s == null) {
				throw new UsernameNotFoundException("Cannot load user infos for username '" + email + "' !");
			}

			// Check if student is administrator
			boolean isAdmin = this.dataProvider.isAdmin(s);

			// Fill UserDetails object
			Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Constants.STUDENT_SEC_ROLES);
			if (isAdmin) {
				authorities = AuthorityUtils.createAuthorityList(Constants.ADMIN_SEC_ROLES, Constants.STUDENT_SEC_ROLES);
			}
			user = new User(s.getEmail(), s.getPassword(), authorities);
		}
		catch (Exception e) {
			String msg = "Error during retrieving of infos for user '" + email + "'";
			LOG.error(msg, e);
			throw new UsernameNotFoundException(msg, e);
		}

		return user;
	}
}
