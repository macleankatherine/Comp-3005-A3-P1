package src;
//import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet; // Import ResultSet from java.sql
import java.util.Date; // Import Date from java.util



public class PostgreSQLJDBCConnection {
    private static Connection conn;

    public static void main(String[] args) {

        // JDBC & Database credentials
        // Replace <HOST>, <PORT>, <DATABASE_NAME>, <USERNAME>, and <PASSWORD> with your actual
        conn = establishConnection();

        // String url = "jdbc:postgresql://localhost:5432/Student";
        // String user = "postgres";
        // String password = "Katherine";

      //  try { // Load PostgreSQL JDBC Driver

           // Class.forName("org.postgresql.Driver");
            // Connect to the database
           // Connection conn = DriverManager.getConnection(url, user, password);

            if (conn != null) {
                System.out.println("Connected to PostgreSQL successfully!\n");
                
                getAllStudents();
                addStudent("kat", "M", "maclean@gmail.com", new Date(System.currentTimeMillis()));
                getAllStudents();
                updateStudentEmail(2, "Maclean@gmail.ca");
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Close the connection (in a real scenario, do this in a finally
            //conn.close();
       // }
        // catch (ClassNotFoundException | SQLException e) {
        //     e.printStackTrace();
        // }  
    }

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

    static void getAllStudents(){
        try{
            Statement stmt = conn.createStatement();
            String SQL = "SELECT student_id, first_name, last_name, email, enrollment_date FROM Students";
            ResultSet rs = stmt.executeQuery(SQL);

            while(rs.next()){

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
            // Handle the exception or print the error message
            e.printStackTrace();
        }
    }

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