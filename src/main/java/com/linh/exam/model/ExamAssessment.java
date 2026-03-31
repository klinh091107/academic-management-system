package com.linh.exam.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

// Represents an exam assessment
public class ExamAssessment extends Assessment {

    // Maximum mark for the exam
    private double maximumMark;

    // Initialize exam assessment with fixed type "Exam"
    public ExamAssessment(int assessmentId, int moduleId, int studentId,
                          double awardedMark, double maximumMark) {
        super(assessmentId, moduleId, studentId, "Exam", awardedMark);
        this.maximumMark = maximumMark;
    }

    @Override
    public double calculateScore() {
        // Avoid division by zero
        if (maximumMark == 0) {
            return 0;
        }

        // Calculate percentage score
        return (getAwardedMark() / maximumMark) * 100;
    }
}