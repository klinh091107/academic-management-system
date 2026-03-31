package com.linh.exam.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

// Represents report assessment
public class ReportAssessment extends Assessment {

    // Initialize presentation assessment with fixed type "Report
    public ReportAssessment(int assessmentId, int moduleId, int studentId, double awardedMark) {
        super(assessmentId, moduleId, studentId, "Report", awardedMark);
    }

    @Override
    public double calculateScore() {
        return getAwardedMark();  // Calculate score by taking awarded mark
    }
}