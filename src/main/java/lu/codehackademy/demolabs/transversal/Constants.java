package lu.codehackademy.demolabs.transversal;

/**
 * Project constants
 * 
 * @author Dominique Righetto
 * 
 */
public class Constants {

	/**
	 * 
	 * Constructor
	 *
	 */
	private Constants() {
		// Avoid instanciation
	}

	/** Logical security role to identity Administrator */
	public static final String ADMIN_SEC_ROLES = "ROLE_ADMIN";

	/** Logical security role to identify normal student */
	public static final String STUDENT_SEC_ROLES = "ROLE_STUDENT";

	/** JSON content type */
	public static final String JSON_MIME_TYPE = "application/json";

	/** FORM content type */
	public static final String FORM_URLENCODED_MIME_TYPE = "application/x-www-form-urlencoded";

	/** Name of the whitelists property file used for input validation */
	public static final String INPUT_VALIDATION_WHITELISTS_NAME = "whitelists";

	/** Numbedr of hash iteration for password */
	public static final int PASSWORD_HASHING_ROUND = 13;

}
