package lu.codehackademy.demolabs.transversal;

/**
 * Project constants
 * 
 * @author Dominique Righetto
 * 
 */
public interface Constants {

	/** Logical security role to identity Administrator */
	String ADMIN_SEC_ROLES = "ROLE_ADMIN";

	/** Logical security role to identify normal student */
	String STUDENT_SEC_ROLES = "ROLE_STUDENT";

	/** JSON content type */
	String JSON_MIME_TYPE = "application/json";

	/** FORM content type */
	String FORM_URLENCODED_MIME_TYPE = "application/x-www-form-urlencoded";

	/** Name of the whitelists property file used for input validation */
	String INPUT_VALIDATION_WHITELISTS_NAME = "whitelists";

	/** Numbedr of hash iteration for password */
	int PASSWORD_HASHING_ROUND = 13;

}
