package ASU.CAIE.Users;

public class User {
	private final String email;
	private final Role role;
	private String name;
	private String password;

	public User(String name, String email, String password, Role role) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	// Getters
	public String getName() { return name; }
	public String getEmail() { return email; }
	public String getPassword() { return password; }
	public Role getRole() { return role; }
}
