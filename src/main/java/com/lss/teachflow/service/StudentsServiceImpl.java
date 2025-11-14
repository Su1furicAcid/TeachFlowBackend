package com.lss.teachflow.service;

import com.lss.teachflow.entity.Student;
import com.lss.teachflow.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentsServiceImpl implements StudentsService {
    private final StudentRepository studentRepository;

    private final UserService userService;

    @Autowired
    public StudentsServiceImpl(StudentRepository studentRepository, UserService userService) {
        this.studentRepository = studentRepository;
        this.userService = userService;
    }

    @Override
    public List<Student> findAllByTeacherId(Long teacherId) {
        return studentRepository.findByTeacherId(teacherId);
    }

    @Override
    public Optional<Student> findByStudentId(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Long userId = userService.getUserIdByUsername(userName);
        return studentRepository.findByIdAndTeacherId(id, userId);
    }

    @Override
    public List<Student> createStudents(List<Student> students) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Long userId = userService.getUserIdByUsername(userName);
        for (Student student : students) {
            student.setTeacherId(userId);
        }
        return studentRepository.saveAll(students);
    }

    @Override
    public Optional<Student> updateStudent(Student student) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Long userId = userService.getUserIdByUsername(userName);
        Optional<Student> existingStudentOpt = studentRepository.findById(student.getId());
        if (existingStudentOpt.isPresent()) {
            Student existingStudent = existingStudentOpt.get();
            existingStudent.setStudentName(student.getStudentName());
            existingStudent.setTeacherId(userId);
            existingStudent.setClazz(student.getClazz());
            existingStudent.setGrade(student.getGrade());
            return Optional.of(studentRepository.save(existingStudent));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteByStudentId(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Long userId = userService.getUserIdByUsername(userName);
        if (studentRepository.existsByIdAndTeacherId(id, userId)) {
            studentRepository.deleteByIdAndTeacherId(id, userId);
            return true;
        }
        return false;
    }
}
