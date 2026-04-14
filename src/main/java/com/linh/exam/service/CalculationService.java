package com.linh.exam.service;

import com.linh.exam.model.*;

import java.sql.*;

public class CalculationService {
    // Database connection
    private final Connection connection;

    // Initialize service with database connection
    public CalculationService(Connection connection) {
        this.connection = connection;
    }

    // Calculate mark for a specific student's module
    public double calculateModuleMark(int studentId, int moduleId) throws SQLException {

        String sql = "SELECT a.AssessmentID, a.AssessmentType, a.AwardedMarks, " +
                "a.MaximumMarks, ast.Weighting " +
                "FROM Assessment a " +
                "JOIN AssessmentStructure ast ON a.AssessmentID = ast.AssessmentID " +
                "WHERE a.StudentID = ? AND a.ModuleID = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            st.setInt(1, studentId);
            st.setInt(2, moduleId);

            ResultSet rs = st.executeQuery();

            double total = 0;
            boolean hasMissingAssessment = false;

            // Retrieve modules and calculate final mark for each assessment
            while (rs.next()) {
                String type = rs.getString("AssessmentType");

                double mark = rs.getDouble("AwardedMarks");
                double max = rs.getDouble("MaximumMarks");
                double weight = rs.getDouble("Weighting");
                int assessmentId = rs.getInt("AssessmentID");

                // Check if any assessment was not submitted
                if (mark == 0) {
                    return 0;
                }

                Assessment assessment;

                // Create new assessment base on the type
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
                    default:
                        continue;
                }

                // Call the calculate function for each assessment
                double score = assessment.calculateScore();

                // Calculate the total
                total += score * weight / 100;
            }

            return total;
        }
    }
}