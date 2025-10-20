package com.lss.teachflow.repository;

import com.lss.teachflow.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByTeacherId(Long teacherId);
    Optional<Student> findByStudentIdAfterAndTeacherId(Long id, Long userId);
    boolean existsByStudentIdAndTeacherId(Long id, Long userId);
    void deleteByStudentIdAndTeacherId(Long id, Long userId);
}
