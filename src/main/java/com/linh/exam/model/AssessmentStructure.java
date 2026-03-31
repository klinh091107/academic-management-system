package com.linh.exam.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

// Represents the structure of assessments
public class AssessmentStructure {
    private int relationId;
    private int moduleId;
    private int assessmentId;
    private double weighting;

    // Initialize structure
    public AssessmentStructure(int moduleId, int assessmentId, double weighting) {
        this.moduleId = moduleId;
        this.assessmentId = assessmentId;
        this.weighting = weighting;
    }

    @Override
    public String toString() {
        return "AssessmentStructure: " +
                "Module ID: " + moduleId +
                ", Assessment ID: " + assessmentId +
                ", Weighting: " + weighting;
    }
}
