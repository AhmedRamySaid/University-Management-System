package ASU.CAIE;

import ASU.CAIE.Database.UserDao;
import ASU.CAIE.Users.Role;
import ASU.CAIE.Users.User;

public class Main {
	public static void main(String[] args) {
		UserDao userDao = new UserDao();

//		// Create a new student object
//		User newStudent = new User(
//				"Jane Student",
//				"Janey@university.edu",
//				Role.STUDENT
//		);
//
//		// Save to database
//		System.out.println("Attempting to create user...");
//		boolean isSuccess = userDao.createUser(newStudent, "MyPassword123");

		boolean fail = userDao.VerifyUserPassword
				("Janey@university.edu", "MyPassword");
		boolean pass = userDao.VerifyUserPassword
				("Janey@university.edu", "MyPassword123");
		System.out.println("Password verification (should fail): " + fail);
		System.out.println("Password verification (should pass): " + pass);

		User retrievedUser = userDao.GetUser("Janey@university.edu").orElse(null);

		if(retrievedUser != null) {
			System.out.println("Retrieved user: " + retrievedUser.GetName() + ", Role: " + retrievedUser.GetRole());
		} else {
			System.out.println("User not found.");
		}
	}
}