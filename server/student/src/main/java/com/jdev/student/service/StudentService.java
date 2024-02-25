package com.jdev.student.service;

import com.jdev.student.model.DTO.StudentRegistrationDTO;
import com.jdev.student.model.DTO.StudentUpdateDTO;
import com.jdev.student.model.Student;
import com.jdev.student.model.enums.SemesterEnum;
import com.jdev.student.repository.StudentRepository;
import com.jdev.student.service.exceptions.UserNotFoundException;
import com.jdev.student.utils.GenerateNewName;
import com.jdev.student.utils.GenerateRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    //admin
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Student create(StudentRegistrationDTO studentDTO) {
        Student studentSave = modelingNewStudent(studentDTO);
        return studentRepository.save(studentSave);
    }

    private Student modelingNewStudent(StudentRegistrationDTO student) {
        return Student.builder()
                .completeName(student.completeName())
                .username(this.generateRandomUsername())
                .email(student.email())
                .password(student.password())
                .cpf(student.cpf())
                .course(null)
                .semester(SemesterEnum.PRIMEIRO)
                .birthday(student.birthday())
                .registration(this.generateRegistration())
                .city(student.city())
                .nationality(student.nationatily())
                .ethnicity(student.ethnicity())
                .phone(student.phone())
                .address(student.address())
                .numberHouse(student.numberHouse())
                .active(true)
                .access(true)
                .build();
    }

    private String generateRandomUsername() {
        String codec = GenerateNewName.generateRandomId();
        String username = codec.replaceAll("-", "0");
        Boolean confirm = findByUsernameForRegistration(username);
        if (confirm != null) {
            return username;
        } else {
            return username + UUID.randomUUID().toString().substring(0,1);
        }
    }

    private String generateRegistration() {
        String codec = GenerateRegister.newRegister();
        String registration = codec.replaceAll("-", "0");
        Boolean confirm = findByRegistrationForGenerateRegistration(registration);
        if (confirm != null) {
            return registration;
        } else {
            return registration + UUID.randomUUID().toString().substring(0, 5);
        }
    }

    //admin
    public Student findByRegistration(String registration) {
        Optional<Student> student = studentRepository.findByRegistration(registration);
        return student.orElseThrow(UserNotFoundException::new);
    }

    private Boolean findByRegistrationForGenerateRegistration(String registration) {
        Optional<Student> student = studentRepository.findByRegistration(registration);
        return student != null;
    }

    //admin
    public Student findById(UUID id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElseThrow(() -> new RuntimeException("student not found!"));
    }

    //admin
    public Boolean findByUsernameForRegistration(String username) {
        Optional<Student> student = studentRepository.findByUsername(username);
        return (student != null) ? true : false;
    }

    public Student updateStudent(StudentUpdateDTO studentUpdate) {
        Student student = this.findByRegistration(studentUpdate.registration());
        student.setCompleteName(studentUpdate.completeName());
        student.setEmail(studentUpdate.email());
        student.setCpf(studentUpdate.cpf());
        student.setBirthday(studentUpdate.birthday());
        student.setCity(studentUpdate.city());
        student.setNationality(studentUpdate.nationatily());
        student.setEthnicity(studentUpdate.ethnicity());
        student.setPhone(studentUpdate.phone());
        student.setAddress(studentUpdate.address());
        student.setNumberHouse(studentUpdate.numberHouse());
        return studentRepository.save(student);
    }

    public void setAsNotActive(UUID id) {
        Student student = this.findById(id);
        if (student.isActive()) {
            student.setActive(false);
            studentRepository.save(student);
        }
    }
}

