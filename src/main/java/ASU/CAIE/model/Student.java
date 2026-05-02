package ASU.CAIE.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
	private List<Grade> courseGrades;

	public List<Grade> GetCourseGrades() {
		if (courseGrades == null) courseGrades = new ArrayList<>();
		return courseGrades;
	}
	public void SetCourseGrades(List<Grade> courseGrades) { this.courseGrades = courseGrades; }

	public double CalculateGPA() {
		if (courseGrades == null || courseGrades.isEmpty()) return 0.0;

		double totalPoints = 0.0;
		int totalCredits = 0;

		for (Grade grade : courseGrades) {
			totalPoints += grade.getGPA() * 1; // todo: add credits
			totalCredits += 1;
		}

		return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
	}
}
