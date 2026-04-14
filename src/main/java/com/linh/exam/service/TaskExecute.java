package com.linh.exam.service;

import com.linh.exam.model.*;
import com.linh.exam.database.DatabaseConnection;
import com.linh.exam.model.Module;
import com.linh.exam.model.Assessment;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TaskExecute {
    // Database connection and calculation service
    private final Connection connection;
    private final CalculationService calculator;

    public TaskExecute() throws SQLException {
        // Initialize database connection and calculation service
        connection = DatabaseConnection.connect();
        calculator = new CalculationService(connection);
    }

    //Display MENU
    public void printMenu() {
        System.out.println("===== MENU =====");
        System.out.println("1. Show Student Module");
        System.out.println("2. Show Module Info");
        System.out.println("3. Show Module Structure");
        System.out.println("4. Show Assessment Mark");
        System.out.println("5. Show Student Module Mark");
        System.out.println("6. Show Final Degree Classification");
        System.out.println("Enter 0 to exit");
    }

    //TASK 1: STUDENT'S MODULE
    public void showStudentModule(int studentId) throws SQLException {
        //Handle negative or zero input
        if (studentId <= 0) {
            System.out.println("Invalid Student ID. Please enter integer ID larger than 0");
            return;
        }

        // Check validation and existence
        if (!exists("SELECT 1 FROM StudentEnrolment WHERE StudentID = ?", studentId)) {
            System.out.println("No student found with ID: " + studentId);
            return;
        }

        String sql = "SELECT se.StudentID, m.ModuleID " +
                "FROM StudentEnrolment se " +
                "JOIN Module m ON se.ModuleID = m.ModuleID " +
                "WHERE se.StudentID = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            // Set parameters for the prepared statement
            st.setInt(1, studentId);
            ResultSet rs = st.executeQuery();

            boolean found = false;

            // Process each record returned from the query
            while (rs.next()) {
                found = true;

                int studentId1 = rs.getInt("StudentID");
                int moduleId1 = rs.getInt("ModuleID");

                // Create StudentEnrollment object from query result
                StudentEnrollment enrollment = new StudentEnrollment(studentId1, moduleId1);

                // Display result
                System.out.println(enrollment);
            }
        }
    }

    //TASK 2: MODULE INFORMATION (ID, LEVEL)
    public void showModuleInfo(int moduleId) throws SQLException {
        //Handle negative or zero input
        if (moduleId <= 0) {
            System.out.println("Invalid Module ID. Please enter integer ID larger than ");
            return;
        }

        // Check validation and existence
        if (!exists("SELECT 1 FROM Module WHERE ModuleID = ?", moduleId)) {
            System.out.println("No module found with ID: " + moduleId);
            return;
        }

        String sql = "SELECT ModuleID, Level " +
                "FROM Module " +
                "WHERE ModuleID = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            // Set parameters for the prepared statement
            st.setInt(1, moduleId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int level = rs.getInt("Level");
                System.out.println(new Module(moduleId, level));
            }
        }
    }

    //TASK 3: MODULE STRUCTURE
    public void showModuleStructure(int studentId, int moduleId) throws SQLException {
        //Handle negative or zero input
        if (studentId <= 0 || moduleId <= 0) {
            System.out.println("Invalid input IDs. Please enter integer ID larger than 0");
            return;
        }

        // Check validation and existence
        boolean studentExists = exists("SELECT 1 FROM StudentEnrolment WHERE StudentID = ?", studentId);
        boolean moduleExists = exists("SELECT 1 FROM Module WHERE ModuleID = ?", moduleId);

        //Handle validation and existence case
        if (!studentExists && !moduleExists) {
            System.out.println("No student and module found with given IDs");
            return;
        } else if (!studentExists) {
            System.out.println("No student found with ID: " + studentId);
            return;
        } else if (!moduleExists) {
            System.out.println("No module found with ID: " + moduleId);
            return;
        }

        String sql = "SELECT ast.AssessmentID, a.AssessmentType, ast.Weighting " +
                "FROM AssessmentStructure ast " +
                "JOIN Assessment a ON ast.AssessmentID = a.AssessmentID " +
                "WHERE a.StudentID = ? AND ast.ModuleID = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            // Set parameters for the prepared statement
            st.setInt(1, studentId);
            st.setInt(2, moduleId);

            ResultSet rs = st.executeQuery();

            boolean found = false;

            // Process each record returned from the query
            while (rs.next()) {
                found = true;

                int assessmentId = rs.getInt("AssessmentID");
                String type = rs.getString("AssessmentType");
                double weight = rs.getDouble("Weighting");

                // Create object Assessment Structure
                AssessmentStructure ast = new AssessmentStructure(moduleId, assessmentId, weight);

                // Display result
                System.out.println(ast + " - Type: " + type);
            }

            // Handle case when no structure is found
            if (!found) {
                System.out.println("No structure found for student " + studentId +
                        " in module " + moduleId);
            }
        }
    }

    // TASK 4: SPECIFIC ASSESSMENT MARK
    public void showAssessmentMark(int moduleId, int assessmentId, int studentId) throws SQLException {
        //Handle negative or zero input
        if (studentId <= 0 || moduleId <= 0 || assessmentId <= 0) {
            System.out.println("Invalid input IDs. Please enter integer ID larger than 0");
            return;
        }

        // Check validation and existence
        boolean studentExists = exists("SELECT 1 FROM StudentEnrolment WHERE StudentID = ?", studentId);
        boolean moduleExists = exists("SELECT 1 FROM Module WHERE ModuleID = ?", moduleId);

        boolean assessmentExists = exists(
                "SELECT 1 FROM Assessment WHERE AssessmentID = ? AND ModuleID = ? AND StudentID = ?",
                assessmentId, moduleId, studentId
        );

        // Handle validation and existence cases
        if (!studentExists && !moduleExists) {
            System.out.println("No student and module found with given IDs");
            return;
        } else if (!studentExists) {
            System.out.println("No student found with ID: " + studentId);
            return;
        } else if (!moduleExists) {
            System.out.println("No module found with ID: " + moduleId);
            return;
        } else if (!assessmentExists) {
            System.out.println("No assessment found for this student in this module");
            return;
        }

        String sql = "SELECT a.AssessmentID, a.ModuleID, a.StudentID, a.AssessmentType, " +
                "a.AwardedMarks, a.MaximumMarks " +
                "FROM Assessment a " +
                "WHERE a.ModuleID = ? AND a.AssessmentID = ? AND a.StudentID = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            // Set parameters for the prepared statement
            st.setInt(1, moduleId);
            st.setInt(2, assessmentId);
            st.setInt(3, studentId);

            ResultSet rs = st.executeQuery();

            // Process each record returned from the query
            while (rs.next()) {

                String type = rs.getString("AssessmentType");
                double mark = rs.getDouble("AwardedMarks");
                double max = rs.getDouble("MaximumMarks");

                Assessment assessment;
                // Create Assessment object based on type
                switch (type) {
                    case "Presentation":
                        assessment = new PresentationAssessment(assessmentId, moduleId, studentId, mark);
                        break;

                    case "Report":
                        assessment = new ReportAssessment(assessmentId, moduleId, studentId, mark);
                        break;

                    case "Exam":
                        assessment = new ExamAssessment(assessmentId, moduleId, studentId, mark, max);
                        break;

                    //Handle case when the assessment type not found
                    default:
                        System.out.println("Unknown assessment type: " + type);
                        continue;
                }

                // Calculate score
                double score = assessment.calculateScore();

                // Display result
                System.out.println("ASSESSMENT " + assessmentId + " - MODULE " + moduleId + " SCORE");

                System.out.println("Student ID: " + studentId +
                        " - Type: " + assessment.getAssessmentType() +
                        " - Score: " + String.format("%.1f", score));
            }
        }
    }

    //TASK 5: SPECIFIC STUDENT'S MODULE MARK
    public void showStudentModuleMark(int studentId, int moduleId) throws SQLException {
        //Handle negative or zero input
        if (studentId <= 0 || moduleId <= 0) {
            System.out.println("Invalid input IDs. Please enter integer ID larger than 0");
            return;
        }

        // Check validation and existence
        boolean studentExists = exists("SELECT 1 FROM StudentEnrolment WHERE StudentID = ?", studentId);
        boolean moduleExists = exists("SELECT 1 FROM Module WHERE ModuleID = ?", moduleId);

        // Handle validation and existence cases
        if (!studentExists && !moduleExists) {
            System.out.println("No student and module found with given IDs");
            return;
        } else if (!studentExists) {
            System.out.println("No student found with ID: " + studentId);
            return;
        } else if (!moduleExists) {
            System.out.println("No module found with ID: " + moduleId);
            return;
        }

        // Retrieve and calculate the final module mark from assessments
        double mark = calculator.calculateModuleMark(studentId, moduleId);

        // Display result
        System.out.println("MODULE " + moduleId + " SCORE");

        // If any assessment was not submit -> module fail
        if (mark == 0) {
            System.out.println("Module " + moduleId + " FAILED (do not submit all assessments)");
        } else {
            System.out.printf("Student ID: %d - Final Module Mark: %.1f%n", studentId, mark);
        }
    }

    // TASK 6: FINAL DEGREE CLASSIFICATION
    public void showFinalDegreeClassification(int studentId) throws SQLException {
        //Handle negative or zero input
        if (studentId <= 0) {
            System.out.println("Invalid Student ID. Please enter integer ID larger than 0");
            return;
        }

        // Check validation and existence
        if (!exists("SELECT 1 FROM StudentEnrolment WHERE StudentID = ?", studentId)) {
            System.out.println("No student found with ID: " + studentId);
            return;
        }

        String sql = "SELECT DISTINCT a.ModuleID, m.Level " +
                "FROM Assessment a " +
                "JOIN Module m ON a.ModuleID = m.ModuleID " +
                "WHERE a.StudentID = ? AND m.Level IN (5,6)";

        // Stores final mark of each module (module ID, mark)
        Map<Integer, Double> moduleMarks = new HashMap<>();

        // Stores level of each module (module ID, level)
        Map<Integer, Integer> moduleLevels = new HashMap<>();

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            // Set parameters for the prepared statement
            st.setInt(1, studentId);

            ResultSet rs = st.executeQuery();

            // Retrieve modules and calculate final mark for each module
            while (rs.next()) {  //Retrieve modules
                int moduleId = rs.getInt("ModuleID");
                int level = rs.getInt("Level");

                // Calculate final mark for the given student and module
                double mark = calculator.calculateModuleMark(studentId, moduleId);

                // If any module fail -> Fail
                if (mark == 0) {
                    System.out.println("FINAL DEGREE CLASSIFICATION");
                    System.out.println("FAIL (Module " + moduleId + " failed)");
                    return;
                }

                moduleMarks.put(moduleId, mark);
                moduleLevels.put(moduleId, level);
            }
        }

        // Handle case when not found data
        if (moduleMarks.isEmpty()) {
            System.out.println("No data found for student " + studentId);
            return;
        }

        // Find the module has the lowest mark
        double minMark = Double.MAX_VALUE;
        int removedModuleId = -1;
        int removedLevel = -1;

        // Iterate through all module Ids
        for (int moduleId : moduleMarks.keySet()) {

            double mark = moduleMarks.get(moduleId);
            int level = moduleLevels.get(moduleId);

            // Check and update the module with the lowest mark
            if (mark < minMark) {
                minMark = mark;
                removedModuleId = moduleId;
                removedLevel = level;
            }
        }

        // Sum marks for Level 5 and Level 6 excluding the lowest module
        double sumL5 = 0;
        double sumL6 = 0;

        // Iterate through all module Ids
        for (int moduleId : moduleMarks.keySet()) {

            // Skip the removed lowest module
            if (moduleId == removedModuleId) continue;

            double mark = moduleMarks.get(moduleId);
            int level = moduleLevels.get(moduleId);

            if (level == 5) sumL5 += mark;
            if (level == 6) sumL6 += mark;
        }

        double avgL5, avgL6;

        // Adjust calculation depend on which level is removed
        if (removedLevel == 5) {
            avgL5 = sumL5 / 5;
            avgL6 = sumL6 / 6;
        } else {
            avgL5 = sumL5 / 6;
            avgL6 = sumL6 / 5;
        }

        // Calculate final weighted mark for classification
        double finalMark = (avgL5 * 0.3) + (avgL6 * 0.7);

        String classification;

        // Determine degree classification based on final mark
        if (finalMark >= 70) classification = "First Class";
        else if (finalMark >= 60) classification = "2nd Class 1st Division";
        else if (finalMark >= 50) classification = "2nd Class 2nd Division";
        else if (finalMark >= 40) classification = "3rd Class Pass";
        else classification = "Fail";

        //Display final result
        System.out.println("FINAL DEGREE CLASSIFICATION");
        System.out.println("Removed module: " + removedModuleId + " (Level " + removedLevel + ")");
        System.out.printf("Final Mark: %.1f - Class: %s%n", finalMark, classification);
    }

    // Check if a record exists in the database based on a query and parameters
    private boolean exists(String sql, Object... params) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            // Set all parameters into the SQL query (starting index = 1)
            for (int i = 0; i < params.length; i++) {
                st.setObject(i + 1, params[i]);
            }
            // Execute query and get result
            ResultSet rs = st.executeQuery();
            // Return true if at least one record exists, otherwise false
            return rs.next();
        }
    }
}