/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greglturnquist.payroll;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Entity // <1>
public class Employee {

    private @Id
    @GeneratedValue
    Long id; // <2>
    private String firstName;
    private String lastName;
    private String description;
    private String jobTitle;
    private String email;

    private Employee() {
    }

    public Employee(String firstName, String lastName, String description, String jobTitle, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.jobTitle = jobTitle;
        try {
            isValid(email);
        } catch (Exception exception) {
            email = "invalid email input";
        }
        this.email = email;
    }

    /**
     * Method to validate the email address
     *
     * @param emailAddress that is going to be added
     */
    private void isValid(String emailAddress) {
        if (emailAddress == null) {
            throw new NullPointerException("The email Address can't be null");
        }
        if (emailAddress.trim().length() == 0) {
            throw new IllegalArgumentException("The email Address is empty.");
        }
        if (!checkFormat(emailAddress)) {
            throw new IllegalArgumentException("The email Address is not in the correct format.");
        }
    }

    /**
     * Method to verify if the email has the correct format
     * e.g 120000@isep.ipp.pt
     *
     * @param emailAddress - email address
     * @return true if the email is on the correct format
     */
    // Adapted from https://www.geeksforgeeks.org/check-email-address-valid-not-java/
    private boolean checkFormat(String emailAddress) {
        String emailRegex = "[A-Z0-9a-z._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";

        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(emailAddress).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) &&
                Objects.equals(firstName, employee.firstName) &&
                Objects.equals(lastName, employee.lastName) &&
                Objects.equals(description, employee.description) &&
                Objects.equals(jobTitle, employee.jobTitle) &&
                Objects.equals(email, employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, description, jobTitle, email);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        //TODO Validations
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", description='" + description + '\'' +
                ", job title='" + jobTitle + '\'' +
                ", email ='" + email + '\'' +
                '}';
    }
}
// end::code[]
