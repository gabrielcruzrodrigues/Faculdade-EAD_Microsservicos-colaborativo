package com.jdev.student.repository;

import com.jdev.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findByRegistration(String registration);
    Optional<Student> findByUsername(String username);
}
