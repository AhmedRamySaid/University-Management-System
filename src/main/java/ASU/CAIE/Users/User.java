package ASU.CAIE.Users;

public class User {
	private String email;
	private Role role;
	private String name;

	public User() { }

	public User(String name, String email, Role role) {
		this.name = name;
		this.email = email;
		this.role = role;
	}

	// Getters
	public String GetName() { return name; }
	public String GetEmail() { return email; }
	public Role GetRole() { return role; }

	// Setters
	public void SetName(String name) { this.name = name; }
	public void SetEmail(String email) { this.email = email; }
	public void SetRole(Role role) { this.role = role; }

	@Override
	public String toString()
	{
		return "Name: " + name + ", Email: " + email + ", Role: " + role;
	}
}
