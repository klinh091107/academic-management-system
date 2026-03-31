package com.linh.exam.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

// Represents presentation assessment
public class PresentationAssessment extends Assessment {

    // Initialize presentation assessment with fixed type "Presentation"
    public PresentationAssessment(int assessmentId, int moduleId, int studentId, double awardedMark) {
        super(assessmentId, moduleId, studentId, "Presentation", awardedMark);
    }

    @Override
    public double calculateScore() {
        return getAwardedMark();  // Calculate score by taking awarded mark
    }
}