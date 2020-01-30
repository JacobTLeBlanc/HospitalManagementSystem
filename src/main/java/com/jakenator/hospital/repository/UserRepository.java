package com.jakenator.hospital.repository;

import com.jakenator.hospital.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value="delete from User u where u.email = ?1")
    void deleteById(String email);
}
