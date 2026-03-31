package com.linh.exam.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

// Represents a module
public class Module {
    private int moduleId;
    private int level;

    // Initialize module data
    public Module(int moduleId, int level) {
        this.moduleId = moduleId;
        this.level = level;
    }

    @Override
    public String toString() {
        return "Module Id: " + moduleId +
                " - Level: " + level;
    }
}


