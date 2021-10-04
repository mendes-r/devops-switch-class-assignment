package com.greglturnquist.payroll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    static String expected = "invalid email input";


    @Test
    void insertValidEmail() {
        //arrange
        String result;
        String firstName = "John";
        String lastName = "von Neumann";
        String description = "The machine";
        String jobTitle = "Architect";
        String email = "john_von_neumann@gmail.com";
        Employee john = new Employee(firstName, lastName, description, jobTitle, email);
        //act
        result = john.getEmail();
        //assert
        Assertions.assertEquals(result, email);
    }

    @Test
    void insertInvalidEmail() {
        //arrange
        String result;
        String firstName = "John";
        String lastName = "von Neumann";
        String description = "The machine";
        String jobTitle = "Architect";
        String email = "john_von_neumann.com";
        Employee john = new Employee(firstName, lastName, description, jobTitle, email);
        //act
        result = john.getEmail();
        //assert
        Assertions.assertNotEquals(result, email);
        Assertions.assertEquals(result, expected);
    }

    @Test
    void insertNullEmail() {
        //arrange
        String result;
        String firstName = "John";
        String lastName = "von Neumann";
        String description = "The machine";
        String jobTitle = "Architect";
        String email = null;
        Employee john = new Employee(firstName, lastName, description, jobTitle, email);
        //act
        result = john.getEmail();
        //assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    void insertSpacesEmail() {
        //arrange
        String result;
        String firstName = "John";
        String lastName = "von Neumann";
        String description = "The machine";
        String jobTitle = "Architect";
        String email = "  ";
        Employee john = new Employee(firstName, lastName, description, jobTitle, email);
        //act
        result = john.getEmail();
        //assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    void insertEmptyEmail() {
        //arrange
        String result;
        String firstName = "John";
        String lastName = "von Neumann";
        String description = "The machine";
        String jobTitle = "Architect";
        String email = "";
        Employee john = new Employee(firstName, lastName, description, jobTitle, email);
        //act
        result = john.getEmail();
        //assert
        Assertions.assertEquals(result, expected);
    }
}
