package com.jakenator.hospital.entity;

import javax.persistence.*;

/** Patient Table for mySQL Database
 *
 */

@Entity
public class Patient {

    /**
     * Attributes
     */
    @Id
    private int ssn;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "familyDoc")
    private Doctor familyDoc;

    private String firstName;
    private String lastName;
    private int phone;
    private String address;

    /**
     *  Constructors
     */
    protected Patient() {}

    public Patient(int ssn, Doctor familyDoc, String firstName, String lastName) {
        this.ssn = ssn;
        this.familyDoc = familyDoc;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Patient(int ssn, Doctor familyDoc, String firstName, String lastName, int phone) {
        this.ssn = ssn;
        this.familyDoc = familyDoc;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public Patient(int ssn, Doctor familyDoc, String firstName, String lastName, String address) {
        this.ssn = ssn;
        this.familyDoc = familyDoc;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public Patient(int ssn, Doctor familyDoc, String firstName, String lastName, int phone, String address) {
        this.ssn = ssn;
        this.familyDoc = familyDoc;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    /**
     * Get Methods
     */
    public int getSsn() {
        return ssn;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Doctor getFamilyDoc() {
        return familyDoc;
    }

    public int getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return String.format("Patient[SSN=%d, familyDoc='%d', firstName='%s', lastName='%s', phone=%d, address='%s']", ssn, familyDoc.getEmpId(), firstName, lastName, phone, address);
    }
}
