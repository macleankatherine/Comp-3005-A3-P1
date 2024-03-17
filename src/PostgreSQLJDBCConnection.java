package src;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet; 
import java.util.Date; 



public class PostgreSQLJDBCConnection {
    // Global Connection object
    private static Connection conn;

    public static void main(String[] args) {
        // Establish database connection
        conn = establishConnection();

        if (conn != null) {
            System.out.println("Connected to PostgreSQL successfully!\n");
            
            getAllStudents();

            addStudent("kat", "M", "maclean@gmail.com", new Date(System.currentTimeMillis()));
            getAllStudents();

            updateStudentEmail(2, "Carleton@gmail.ca");
            getAllStudents();

            deleteStudent(3);
            getAllStudents();
        } 
        else {
            System.out.println("Failed to establish connection.");
        } 

        try {
            if (conn != null)
                conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to establish a database connection
    private static Connection establishConnection() {
        Connection connection = null;
        String url = "jdbc:postgresql://localhost:5432/Student";
        String user = "postgres";
        String password = "Katherine";
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Method to retrieve all students from the database and display them
    static void getAllStudents(){
        try{
            Statement stmt = conn.createStatement();
            String SQL = "SELECT student_id, first_name, last_name, email, enrollment_date FROM Students";
            ResultSet rs = stmt.executeQuery(SQL);

            while(rs.next()){
                // Retrieve student information from the ResultSet
                int studentId = rs.getInt("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                Date enrollmentDate = rs.getDate("enrollment_date");

                System.out.println("Student ID: " + studentId);
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Enrollment Date: " + enrollmentDate);
                System.out.println("------------------");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add a new student to the database
    static void addStudent(String first_name, String last_name, String email, Date enrollment_date){
        
        String insertSQL = "INSERT INTO Students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, first_name);
            pstmt.setString(2, last_name);
            pstmt.setString(3, email);
            java.sql.Date sqlEnrollmentDate = new java.sql.Date(enrollment_date.getTime());

            pstmt.setDate(4, sqlEnrollmentDate);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("\nStudent added successfully!\n");
            }
        }
        
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method to update the email of a student
    static void updateStudentEmail(int student_id, String new_email){
        String insertSQL = "UPDATE Students SET email = ? WHERE student_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, new_email);
            pstmt.setInt(2, student_id);
           
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("\nStudent updated successfully!\n");
            }
        }
        
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
    // Method to delete a student from the database
    static void deleteStudent(int student_id){
        String insertSQL = "DELETE FROM Students WHERE student_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, student_id);
           
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("\nStudent deleted successfully!\n");
            }
        }
        
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}