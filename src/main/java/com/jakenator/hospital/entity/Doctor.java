package com.jakenator.hospital.entity;
import javax.persistence.Entity;
import javax.persistence.Id;


/** Doctor Table for mySQL Database
 *
 */
@Entity
public class Doctor {

    /**
     * Attributes
     */
    @Id
    private int empId;

    private String firstName;
    private String lastName;
    private int phone;

    /**
     * Constructors
     */
    protected Doctor() {}

    public Doctor(int empId, String firstName, String lastName) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Doctor(int empId, String firstName, String lastName, int phone) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    /**
     * Get Methods
     */
    public int getEmpId() {
        return empId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return String.format("Doctor[empId=%d, firstName='%s', lastName='%s', phone=%d]", empId, firstName, lastName, phone);
    }
}
