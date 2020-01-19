package com.jakenator.hospital.repository;

import com.jakenator.hospital.entity.Patient;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents Patient Table in database
 */
@Repository
public interface PatientRepository extends CrudRepository<Patient, Integer> {

    /**
     * Search for list of patients with a certain last name
     */
    List<Patient> findByLastName(String lastName);

    /**
     * Search for patient by id
     */
    Patient findById(int id);

    /**
     * Delete a patient with their ID
     */
    @Modifying
    @Transactional
    @Query(value="delete from Patient p where p.id = ?1")
    void deleteById(int id);
}
