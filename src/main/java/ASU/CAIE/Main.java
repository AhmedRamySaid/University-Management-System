package ASU.CAIE;

import ASU.CAIE.Database.UserDao;
import ASU.CAIE.Users.Role;
import ASU.CAIE.Users.User;

public class Main {
	public static void main(String[] args) {
		UserDao userDao = new UserDao();

		// Create a new student object
		User newStudent = new User(
				"Jane Smith",
				"jane.smith@university.edu",
				"MySecurePassword123!",
				Role.STUDENT
		);

		// Save to database
		System.out.println("Attempting to create user...");
		boolean isSuccess = userDao.createUser(newStudent);

		if (isSuccess) {
			System.out.println("Success! User created in the database.");
		} else {
			System.out.println("Failed to create user.");
		}
	}
}