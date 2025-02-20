import java.util.HashMap;  
import java.util.Scanner;  

public class Main {  
    public static void main(String[] args) {  
        HashMap<String, Classroom> classrooms = new HashMap<>();  
        
        // Khởi tạo lớp học và sinh viên  
        Classroom classA = new Classroom("CNTT1");  
        classA.addStudent(new Student("Nguyen", "An", "01/01/2001", "Ha Noi", "CNTT1"));  
        classA.addStudent(new Student("Tran", "Binh", "02/02/2000", "Ha Noi", "CNTT1"));  
        classA.addStudent(new Student("Le", "Cao", "03/03/1999", "Ha Noi", "CNTT1"));  

        // Thêm điểm cho sinh viên  
        classA.getStudents().get(0).setGrade("Lap trinh huong doi tuong", 9);  
        classA.getStudents().get(0).setGrade("Quan ly du an", 8);  
        classA.getStudents().get(0).setGrade("Hoc may", 9);  
        classA.getStudents().get(0).setGrade("Co so du lieu", 7);  
        classA.getStudents().get(0).setGrade("Lap trinh ung dung cho TBDD", 6);  

        classA.getStudents().get(1).setGrade("Lap trinh huong doi tuong", 7);  
        classA.getStudents().get(1).setGrade("Quan ly du an", 6);  
        classA.getStudents().get(1).setGrade("Hoc May", 5);  
        classA.getStudents().get(1).setGrade("Co so du lieu", 6);  
        classA.getStudents().get(1).setGrade("Lap trinh ung dung cho TBDD", 7);  

        classA.getStudents().get(2).setGrade("Lap trinh huong doi tuong", 8);  
        classA.getStudents().get(2).setGrade("Quan ly du an", 7);  
        classA.getStudents().get(2).setGrade("Hoc May", 6);  
        classA.getStudents().get(2).setGrade("Co so du lieu", 7);  
        classA.getStudents().get(2).setGrade("Lap trinh ung dung cho TBDD", 8);  
        
        classrooms.put(classA.getClassId(), classA);   

        Classroom classB = new Classroom("HTTT4");  
        classB.addStudent(new Student("Vu Hoai", "Lien", "01/01/2004", "Ha Noi", "HTTT4"));  
        classB.addStudent(new Student("Cao Khai", "Hung", "02/02/2004", "Ha Noi", "HTTT4"));  
        classB.addStudent(new Student("Nguyen Duc", "Quan", "06/05/2004", "Ha Noi", "HTTT4"));  

        classB.getStudents().get(0).setGrade("Lap trinh huong doi tuong", 9);  
        classB.getStudents().get(0).setGrade("Quan ly du an", 8);  
        classB.getStudents().get(0).setGrade("Hoc may", 9);  
        classB.getStudents().get(0).setGrade("Co so du lieu", 7);  
        classB.getStudents().get(0).setGrade("Lap trinh ung dung cho TBDD", 6);  

        classB.getStudents().get(1).setGrade("Lap trinh huong doi tuong", 7);  
        classB.getStudents().get(1).setGrade("Quan ly du an", 6);  
        classB.getStudents().get(1).setGrade("Hoc May", 5);  
        classB.getStudents().get(1).setGrade("Co so du lieu", 6);  
        classB.getStudents().get(1).setGrade("Lap trinh ung dung cho TBDD", 7);  

        classB.getStudents().get(2).setGrade("Lap trinh huong doi tuong", 8);  
        classB.getStudents().get(2).setGrade("Quan ly du an", 7);  
        classB.getStudents().get(2).setGrade("Hoc May", 6);  
        classB.getStudents().get(2).setGrade("Co so du lieu", 7);  
        classB.getStudents().get(2).setGrade("Lap trinh ung dung cho TBDD", 8);  

        classrooms.put(classB.getClassId(), classB);  
        // Giao diện người dùng  
        Scanner scanner = new Scanner(System.in);  
        System.out.println("Danh sach cac lop:");  
        classrooms.keySet().forEach(System.out::println); // In ra danh sách lớp  

        System.out.print("Nhap ma lop de xem danh sach sinh vien: ");  
        String classId = scanner.nextLine();  

        if (classrooms.containsKey(classId)) {  
            Classroom selectedClass = classrooms.get(classId);  
            System.out.println("Danh sach sinh vien trong lop " + classId + ":");  
            for (Student student : selectedClass.getStudents()) {  
                System.out.println(student.getFullName() + " - Diem trung binh: " + student.getAverageGrade() + " - Rank: " + student.getRank());  
            }  
            selectedClass.printSummary();
        } else {  
            System.out.println("Lop khong ton tai.");  
        }  
        
        scanner.close();  
    }  
}