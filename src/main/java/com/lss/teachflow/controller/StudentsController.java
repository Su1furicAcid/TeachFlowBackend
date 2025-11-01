package com.lss.teachflow.controller;

import com.lss.teachflow.common.ResponseBody;
import com.lss.teachflow.dto.StudentRequest;
import com.lss.teachflow.dto.StudentResponse;
import com.lss.teachflow.entity.Student;
import com.lss.teachflow.service.StudentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentsController {

    private final StudentsService studentsService;

    @GetMapping
    public ResponseBody<List<StudentResponse>> list(@RequestParam("teacherId") Long teacherId) {
        List<StudentResponse> data = studentsService.findAllByTeacherId(teacherId).stream()
                .map(StudentResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseBody.success(data);
    }

    @GetMapping("/{id}")
    public ResponseBody<StudentResponse> get(@PathVariable("id") Long id,
                                               @RequestParam("teacherId") Long teacherId) {
        return studentsService.findByStudentIdAndTeacherId(id, teacherId)
                .map(StudentResponse::fromEntity)
                .map(ResponseBody::success)
                .orElse(ResponseBody.error("404", "Student not found"));
    }

    @PostMapping
    public ResponseBody<StudentResponse> create(@Valid @RequestBody StudentRequest request) {
        Student toSave = Student.builder()
                .teacherId(request.getTeacherId())
                .studentName(request.getStudentName())
                .grade(request.getGrade())
                .clazz(request.getClazz())
                .build();
        Student saved = studentsService.createStudent(toSave);
        StudentResponse body = StudentResponse.fromEntity(saved);
        return ResponseBody.success(body, "Student created successfully");
    }

    @PutMapping("/{id}")
    public ResponseBody<StudentResponse> update(@PathVariable("id") Long id,
                                                  @Valid @RequestBody StudentRequest request) {
        Student toUpdate = Student.builder()
                .studentId(id)
                .teacherId(request.getTeacherId())
                .studentName(request.getStudentName())
                .grade(request.getGrade())
                .clazz(request.getClazz())
                .build();
        return studentsService.updateStudent(toUpdate)
                .map(StudentResponse::fromEntity)
                .map(ResponseBody::success)
                .orElse(ResponseBody.error("404", "Student not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseBody<Void> delete(@PathVariable("id") Long id,
                                       @RequestParam("teacherId") Long teacherId) {
        boolean deleted = studentsService.deleteByStudentIdAndTeacherId(id, teacherId);
        return deleted ? ResponseBody.success() : ResponseBody.error("404", "Student not found");
    }
}
