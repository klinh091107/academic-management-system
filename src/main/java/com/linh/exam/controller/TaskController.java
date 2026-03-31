package com.linh.exam.controller;

import com.linh.exam.service.TaskExecute;
import java.util.Scanner;

public class TaskController {
    public static void main(String[] args) throws Exception {

        // Create scanner to read user input
        Scanner scanner = new Scanner(System.in);

        // Create controller object to execute tasks
        TaskExecute controller = new TaskExecute();

        // Infinite loop to keep the program running until manually stopped
        while (true) {
            // Display menu
            controller.printMenu();

            // Ask user to enter a menu option
            int choice = getIntInput(scanner, "Enter your choice: ");

            // Handle user choice using switch-case
            switch (choice) {
                case 0: // Exit the program
                    System.out.println("Exiting program");
                    scanner.close();
                    return;

                case 1: // Display modules of a specific student
                    int studentId1 = getIntInput(scanner, "Enter Student ID: ");
                    controller.showStudentModule(studentId1);
                    break;

                case 2: // Display information of a specific module
                    int moduleId2 = getIntInput(scanner, "Enter Module ID: ");
                    controller.showModuleInfo(moduleId2);
                    break;

                case 3: // Display module structure for a specific student and module
                    int moduleId3 = getIntInput(scanner, "Enter Module ID: ");
                    int studentId3 = getIntInput(scanner, "Enter Student ID: ");
                    controller.showModuleStructure(studentId3, moduleId3);
                    break;

                case 4: // Display assessment mark for a student in a specific module
                    int moduleId4 = getIntInput(scanner, "Enter Module ID: ");
                    int assessmentId = getIntInput(scanner, "Enter Assessment ID: ");
                    int studentId4 = getIntInput(scanner, "Enter Student ID: ");
                    controller.showAssessmentMark(moduleId4, assessmentId, studentId4);
                    break;

                case 5: // Display overall marks of a student in a module
                    int studentId5 = getIntInput(scanner, "Enter Student ID: ");
                    int moduleId5 = getIntInput(scanner, "Enter Module ID: ");
                    controller.showStudentModuleMark(studentId5, moduleId5);
                    break;

                case 6: // Display final degree classification of a student
                    int studentId6 = getIntInput(scanner, "Enter Student ID: ");
                    controller.showFinalDegreeClassification(studentId6);
                    break;

                default: // Handle invalid input
                    System.out.println("Invalid choice. Please enter 0-6");
            }
        }
    }

    // Reads user input and ensures it is a valid integer.
    private static int getIntInput(Scanner scanner, String message) {
        int value; // variable to store the user's input

        // Keeps asking until the user enters a correct number.
        while (true) {
            try {
                System.out.print(message);
                value = Integer.parseInt(scanner.nextLine()); // convert input to int
                return value;// return value if valid
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number."); // handle invalid input
            }
        }
    }
}