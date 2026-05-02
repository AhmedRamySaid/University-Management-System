package ASU.CAIE.model;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String email;
	private Role role;
	private String name;
	private int ID;
	private List<Course> takenCourses;

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
	public int GetID() { return ID; }
	public List<Course> GetTakenCourses() {
		if (takenCourses == null) takenCourses = new ArrayList<>();
		return takenCourses;
	}

	// Setters
	public void SetName(String name) { this.name = name; }
	public void SetEmail(String email) { this.email = email; }
	public void SetRole(Role role) { this.role = role; }
	public void SetID(int id) { this.ID = id; }
	public void SetTakenCourses(List<Course> courses) { this.takenCourses = courses; }

	@Override
	public String toString()
	{
		return "Name: " + name + ", ID:" + ID + ", Email: " + email + ", Role: " + role;
	}
}