package lu.codehackademy.demolabs.transversal.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;

/**
 * Provide somes utilites methods.
 * 
 * @author Dominique Righetto
 * 
 */
public class Utilities {

	/**
	 * Generate a digest of a password
	 * 
	 * @param password
	 *            Plain text password
	 * @return Hash as string
	 * @throws NoSuchAlgorithmException
	 */
	public static String createPasswordHash(String password) throws NoSuchAlgorithmException {
		String digest = null;
		if (StringUtils.isBlank(password)) {
			throw new IllegalArgumentException("Password cannot be null or empty !");
		}
		// Create a SALTED SHA-512 hashed X times
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		String salt = "DuTf07fcIQ0jbhJ646ud";
		int hashingLoop = 13;
		byte[] tmp = (password + salt).getBytes();
		for (int c = 1; c <= hashingLoop; c++) {
			tmp = md.digest(tmp);
		}
		digest = DatatypeConverter.printHexBinary(tmp);
		return digest;
	}
}
