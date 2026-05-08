package ASU.CAIE.Database;

import ASU.CAIE.Database.AsyncDao.AsyncCourseDao;
import ASU.CAIE.Database.AsyncDao.AsyncGradeDao;
import ASU.CAIE.Database.AsyncDao.AsyncUserDao;
import ASU.CAIE.Database.Dao.CourseDao;
import ASU.CAIE.Database.Dao.GradeDao;
import ASU.CAIE.Database.Dao.UserDao;
import ASU.CAIE.model.User;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class DatabaseManager {
	private static final Dotenv dotenv = Dotenv.load();

	private static final String URL = "jdbc:postgresql://" +
			dotenv.get("PGHOST") + ":5432/" + dotenv.get("PGDATABASE");
	private static final String USER = dotenv.get("PGUSER");
	private static final String PASSWORD = dotenv.get("PGPASSWORD");

	// Single-thread executor: DB calls are sequential, no connection race conditions
	private static final ExecutorService DB_EXECUTOR =
			Executors.newSingleThreadExecutor(r -> {
				Thread t = new Thread(r, "db-thread");
				t.setDaemon(true); // won't prevent app shutdown
				return t;
			});

	public static User CurrentUser;

	// Raw DAO instances — use these only inside other DAO/service code
	public static final UserDao   UserDaoInstance   = new UserDao();
	public static final GradeDao  GradeDaoInstance  = new GradeDao();
	public static final CourseDao CourseDaoInstance = new CourseDao();

	// Async facades — use these from UI code
	public static final AsyncCourseDao Courses = new AsyncCourseDao();
	public static final AsyncUserDao Users   = new AsyncUserDao();
	public static final AsyncGradeDao Grades  = new AsyncGradeDao();

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	/** Submits any DB work to the background thread and returns a future. */
	public static <T> CompletableFuture<T> runAsync(Supplier<T> task) {
		return CompletableFuture.supplyAsync(task, DB_EXECUTOR);
	}

	/** For fire-and-forget writes with no return value. */
	public static CompletableFuture<Void> runAsync(Runnable task) {
		return CompletableFuture.runAsync(task, DB_EXECUTOR);
	}
}