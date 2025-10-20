package com.lss.teachflow.service;

import com.lss.teachflow.entity.Student;
import com.lss.teachflow.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentsServiceImpl implements StudentsService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> findAllByTeacherId(Long teacherId) {
        return studentRepository.findByTeacherId(teacherId);
    }

    @Override
    public Optional<Student> findByStudentIdAndTeacherId(Long id, Long userId) {
        return studentRepository.findByStudentIdAfterAndTeacherId(id, userId);
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> updateStudent(Student student) {
        Optional<Student> existingStudentOpt = studentRepository.findById(student.getStudentId());
        if (existingStudentOpt.isPresent()) {
            Student existingStudent = existingStudentOpt.get();
            existingStudent.setStudentName(student.getStudentName());
            existingStudent.setTeacherId(student.getTeacherId());
            existingStudent.setClazz(student.getClazz());
            existingStudent.setGrade(student.getGrade());
            return Optional.of(studentRepository.save(existingStudent));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteByStudentIdAndTeacherId(Long id, Long userId) {
        if (studentRepository.existsByStudentIdAndTeacherId(id, userId)) {
            studentRepository.deleteByStudentIdAndTeacherId(id, userId);
            return true;
        }
        return false;
    }
}
