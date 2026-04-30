package ASU.CAIE.Database;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

	/**
	 * Hashes a plain-text password using BCrypt.
	 * The salt is automatically generated and included in the resulting string.
	 **/
	public static String hashPassword(String plainTextPassword) {
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
	}

	/**
	 * Verifies if a plain-text password matches a stored BCrypt hash.
	 **/
	public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
		try {
			return BCrypt.checkpw(plainTextPassword, hashedPassword);
		} catch (IllegalArgumentException e) {
			// This happens if the 'hashedPassword' is null or not a valid BCrypt hash
			return false;
		}
	}
}
