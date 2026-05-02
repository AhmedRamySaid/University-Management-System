package ASU.CAIE.model;

import java.time.LocalDateTime;

public class Grade {
    private int gradeId;
    private int studentId;
    private int courseId;
    private int score;
	private float gpa;
    private String letterGrade;
    private String semester;

    public Grade() {}

    public Grade(int gradeId, int studentId, int courseId, int score,  String semester) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.score = score;
        this.semester = semester;

		updateGrade();
    }

    public int getGradeId() { return gradeId; }
    public void setGradeId(int gradeId) { this.gradeId = gradeId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getLetterGrade() { return letterGrade; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

	public void updateGrade() {
		if (score == 0) {
			letterGrade = "NA";
			gpa = 0.0f;
		} else if (score < 60) {
			letterGrade = "F";
			gpa = 0.0f;
		} else if (score < 64) {
			letterGrade = "D";
			gpa = 1.0f;
		} else if (score < 67) {
			letterGrade = "D+";
			gpa = 1.3f;
		} else if (score < 70) {
			letterGrade = "C-";
			gpa = 1.7f;
		} else if (score < 73) {
			letterGrade = "C";
			gpa = 2.0f;
		} else if (score < 76) {
			letterGrade = "C+";
			gpa = 2.3f;
		} else if (score < 80) {
			letterGrade = "B-";
			gpa = 2.7f;
		} else if (score < 84) {
			letterGrade = "B";
			gpa = 3.0f;
		} else if (score < 89) {
			letterGrade = "B+";
			gpa = 3.3f;
		} else if (score < 93) {
			letterGrade = "A-";
			gpa = 3.7f;
		} else if (score < 97) {
			letterGrade = "A";
			gpa = 4.0f;
		} else {
			letterGrade = "A+";
			gpa = 4.0f;
		}
	}
}
