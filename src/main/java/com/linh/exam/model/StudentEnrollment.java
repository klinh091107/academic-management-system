package com.linh.exam.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

// Represents the enrollment of a student
public class StudentEnrollment {
    private int studentResultId;
    private int studentId;
    private int moduleId;

    // Initialize enrollment
    public StudentEnrollment(int studentId, int moduleId) {
        this.studentId = studentId;
        this.moduleId = moduleId;
    }

    @Override
    public String toString() {
        return "Student ID: " + studentId +
                " - Module ID: " + moduleId;

    }
}