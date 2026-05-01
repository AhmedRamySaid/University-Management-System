package ASU.CAIE.model;

public class Parent {
    private int parentId;
    private String name;
    private int linkedStudentId;

    public Parent() {}

    public Parent(int parentId, String name, int linkedStudentId) {
        this.parentId = parentId;
        this.name = name;
        this.linkedStudentId = linkedStudentId;
    }

    public int getParentId() { return parentId; }
    public void setParentId(int parentId) { this.parentId = parentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getLinkedStudentId() { return linkedStudentId; }
    public void setLinkedStudentId(int linkedStudentId) { this.linkedStudentId = linkedStudentId; }
}
