package ASU.CAIE.Database.AsyncDao;

import ASU.CAIE.Database.DatabaseManager;
import ASU.CAIE.model.Grade;
import ASU.CAIE.model.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AsyncUserDao {
	// Async facades for UserDao methods
	public CompletableFuture<Boolean> VerifyUserPassword(String username, String password) {
		return DatabaseManager.runAsync(() -> DatabaseManager.UserDaoInstance.VerifyUserPassword(username, password));
	}

	public CompletableFuture<Optional<User>> GetUser(String username) {
		return DatabaseManager.runAsync(() -> DatabaseManager.UserDaoInstance.GetUser(username));
	}

	public CompletableFuture<Optional<User>> GetUser(int id) {
		return DatabaseManager.runAsync(() -> DatabaseManager.UserDaoInstance.GetUser(id));
	}
}
