import java.util.ArrayList;  
import java.util.List;  

public class Classroom {  
    private String classId;  
    private List<Student> students;  

    public Classroom(String classId) {  
        this.classId = classId;  
        this.students = new ArrayList<>();  
    }  

    public void addStudent(Student student) {  
        students.add(student);  
    }  

    public List<Student> getStudents() {  
        return students;  
    }  

    public int countRank(String rank) {  
        return (int) students.stream().filter(student -> student.getRank().equals(rank)).count();  
    }  

    public void printSummary() {  
        int countA = countRank("A");  
        int countB = countRank("B");  
        int countC = countRank("C");  
        int countD = countRank("D");  
        int countLessD = students.size() - (countA + countB + countC + countD);  
        
        System.out.println("So sinh vien theo rank:");  
        System.out.println("A: " + countA);  
        System.out.println("B: " + countB);  
        System.out.println("C: " + countC);  
        System.out.println("D: " + countD);  
        System.out.println("<D: " + countLessD);  
    }  

    public String getClassId() {  
        return classId;  
    }  
}