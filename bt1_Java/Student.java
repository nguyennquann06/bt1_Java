import java.util.HashMap;  

public class Student {  
    private String firstName;  
    private String lastName;  
    private String birthdate;  
    private String address;  
    private String className;  
    private HashMap<String, Double> grades; // Điểm các môn học  

    public Student(String firstName, String lastName, String birthdate, String address, String className) {  
        this.firstName = firstName;  
        this.lastName = lastName;  
        this.birthdate = birthdate;  
        this.address = address;  
        this.className = className;  
        this.grades = new HashMap<>();  
    }  

    public void setGrade(String subject, double grade) {  
        grades.put(subject, grade);  
    }  

    public double getAverageGrade() {  
        return grades.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);  
    }  

    public String getFullName() {  
        return firstName + " " + lastName;  
    }  

    public String getRank() {  
        double avg = getAverageGrade();  
        if (avg >= 8.0) return "A";  
        else if (avg >= 7.0) return "B";  
        else if (avg >= 5.0) return "C";  
        else if (avg >= 4.0) return "D";  
        return "<D";  
    }  
}