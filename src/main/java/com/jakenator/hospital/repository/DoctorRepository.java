package com.jakenator.hospital.repository;

import com.jakenator.hospital.entity.Doctor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents Doctor table in database
 */
@Repository
public interface DoctorRepository extends CrudRepository<Doctor, Integer> {

    /**
     * Find Doctor by their ID
     */
    Doctor findById(int id);

    /**
     * Find list of Doctors with a certain last name
     */
    List<Doctor> findByLastName(String lastName);

    /**
     * Delete a Doctor by their ID
     */
    @Modifying
    @Transactional
    @Query(value="delete from Doctor d where d.empId = ?1")
    void deleteById(int id);
}