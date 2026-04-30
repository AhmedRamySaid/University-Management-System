package ASU.CAIE.Users;

public class User {
	private String email;
	private Role role;
	private String name;
	private String password;

	public User() { }

	public User(String name, String email, Role role) {
		this.name = name;
		this.email = email;
		this.role = role;
	}

	public User(String name, String email, String password, Role role) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	// Getters (DB branch)
	public String GetName() { return name; }
	public String GetEmail() { return email; }
	public Role GetRole() { return role; }

	// Getters (Login branch)
	public String getName() { return name; }
	public String getEmail() { return email; }
	public String getPassword() { return password; }
	public Role getRole() { return role; }

	// Setters
	public void SetName(String name) { this.name = name; }
	public void SetEmail(String email) { this.email = email; }
	public void SetRole(Role role) { this.role = role; }
	public void setPassword(String password) { this.password = password; }
}
