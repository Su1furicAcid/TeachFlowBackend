package com.lss.teachflow.repository;

import com.lss.teachflow.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByTeacherId(Long teacherId);
    Optional<Student> findByIdAndTeacherId(Long id, Long userId);
    boolean existsByIdAndTeacherId(Long id, Long userId);
    void deleteByIdAndTeacherId(Long id, Long userId);
    List<Student> findByStudentNumberIn(List<String> studentNumbers);
}
