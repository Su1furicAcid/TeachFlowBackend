package com.lss.teachflow.controller;

import com.lss.teachflow.dto.StudentRequest;
import com.lss.teachflow.dto.StudentResponse;
import com.lss.teachflow.entity.Student;
import com.lss.teachflow.service.StudentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentsController {

    private final StudentsService studentsService;

    @GetMapping
    public ResponseEntity<List<StudentResponse>> list(@RequestParam("teacherId") Long teacherId) {
        List<StudentResponse> data = studentsService.findAllByTeacherId(teacherId).stream()
                .map(StudentResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> get(@PathVariable("id") Long id,
                                               @RequestParam("teacherId") Long teacherId) {
        return studentsService.findByStudentIdAndTeacherId(id, teacherId)
                .map(StudentResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequest request) {
        Student toSave = Student.builder()
                .teacherId(request.getTeacherId())
                .studentName(request.getStudentName())
                .grade(request.getGrade())
                .clazz(request.getClazz())
                .build();
        Student saved = studentsService.createStudent(toSave);
        StudentResponse body = StudentResponse.fromEntity(saved);
        return ResponseEntity.created(URI.create("/api/students/" + saved.getStudentId()))
                .body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> update(@PathVariable("id") Long id,
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
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id,
                                       @RequestParam("teacherId") Long teacherId) {
        boolean deleted = studentsService.deleteByStudentIdAndTeacherId(id, teacherId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
