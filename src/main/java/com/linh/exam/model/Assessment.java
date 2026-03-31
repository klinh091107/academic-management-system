package com.linh.exam.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

// Abstract base class for assessments
public abstract class Assessment {
    // Assessment attributes
    private int assessmentId;
    private int moduleId;
    private int studentId;
    private String assessmentType;
    private double awardedMark;

    // Initialize assessment data
    public Assessment(int assessmentId, int moduleId, int studentId,
                      String assessmentType, double awardedMark) {
        this.assessmentId = assessmentId;
        this.moduleId = moduleId;
        this.studentId = studentId;
        this.assessmentType = assessmentType;
        this.awardedMark = awardedMark;
    }

    // Calculate assessment score (implemented by subclasses)
    public abstract double calculateScore();

    @Override
    public String toString() {
        return "Assessment{ Assessment ID: " + assessmentId +
                ", Module ID: " + moduleId +
                ", Assessment Type: '" + assessmentType +
                "}";
    }

}
