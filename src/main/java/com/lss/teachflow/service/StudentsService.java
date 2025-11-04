package com.lss.teachflow.service;

import com.lss.teachflow.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentsService {
    List<Student> findAllByTeacherId(Long teacherId);
    Optional<Student> findByStudentIdAndTeacherId(Long id, Long userId);
    List<Student> createStudents(List<Student> students);
    Optional<Student> updateStudent(Student student);
    boolean deleteByStudentIdAndTeacherId(Long id, Long userId);
}
