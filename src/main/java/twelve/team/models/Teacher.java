package twelve.team.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import twelve.team.Database;

/**
 * The user representation of a teacher. Initialized after authentication and
 * saves a list of semesters. A teacher object is passed into the semester selection
 * screen as a reference to CRUD semesters.
 */
public class Teacher {
    public static Database db;
    
    /* Teacher Variables */
    String name;
    String teacherID;
    ArrayList<Semester> semesters;

    public Teacher(String teacherID) {
        this.teacherID = teacherID;
    }
    
    public Teacher(String teacherID, String name) {
        this.teacherID = teacherID;
        this.name = name;
    }

    private boolean fetch(String teacherID) {
        if (db == null) {
            db = Database.getDatabase();
        }
        
        try {
            /* First fetch the teacher's name */
            String query = "select * from teacher where teacherID = '" + teacherID + "'";
            ResultSet result = db.getQuery(query);
            
            if (result.next()) {
                this.name = result.getString("teacherName");
            }

            // TODO: Move to semester as static method fetch
            
            /* Fetch the existing semesters of the teacher */
            query = "select * from semester where teacherID = '" + teacherID + "'";
            result = db.getQuery(query);
            
            while (result.next()) {
                Semester semester = new Semester(result.getString("semesterID"),
                    result.getString("semesterName"));
                semesters.add(semester);
            }
        } catch(SQLException e) {
            e.printStackTrace(); // DEBUG
            return false;
        }

        return false;
    }
    
    public static Teacher create(String teacherId, String password, String name) {
        try {
            String query = "insert into teacher (teacherID, password, teacherName) values (?, ?, ?)";
            PreparedStatement prpst = Database.getDatabase().prepareStatement(query);
            prpst.setString(1, teacherId);
            prpst.setString(2, password);
            prpst.setString(3, name);
            prpst.executeUpdate();

            return new Teacher(teacherId, name);
        } catch (SQLException e) {
            e.printStackTrace(); // DEBUG
            return null;
        }
    }

    public ArrayList<Semester> getSemesters() {
        if (semesters == null) {
            semesters = new ArrayList<Semester>();
            fetch(teacherID);
        }
        
        return semesters;
    }

    public boolean editSemester(String name) {
        return false;
    }

    public boolean removeSemester(String name, long semesterId) {
        return false;
    }

    public boolean addSemester(Semester semester) {
        return false;
    }
}
