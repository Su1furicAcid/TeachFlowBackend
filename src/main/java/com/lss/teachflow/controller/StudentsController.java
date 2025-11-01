package com.lss.teachflow.controller;

import com.lss.teachflow.common.ResponseBody;
import com.lss.teachflow.dto.StudentRequest;
import com.lss.teachflow.dto.StudentResponse;
import com.lss.teachflow.entity.Student;
import com.lss.teachflow.service.StudentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "学生管理", description = "管理学生信息")
public class StudentsController {

    private final StudentsService studentsService;

    @GetMapping
    @Operation(summary = "获取学生列表", description = "获取指定教师的所有学生列表")
    public ResponseBody<List<StudentResponse>> list(@RequestParam("teacherId") Long teacherId) {
        List<StudentResponse> data = studentsService.findAllByTeacherId(teacherId).stream()
                .map(StudentResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseBody.success(data);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取学生信息", description = "获取指定ID的学生信息")
    public ResponseBody<StudentResponse> get(@PathVariable("id") Long id,
                                               @RequestParam("teacherId") Long teacherId) {
        return studentsService.findByStudentIdAndTeacherId(id, teacherId)
                .map(StudentResponse::fromEntity)
                .map(ResponseBody::success)
                .orElse(ResponseBody.error("404", "Student not found"));
    }

    @PostMapping
    @Operation(summary = "创建学生", description = "创建一个新学生")
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
    @Operation(summary = "更新学生信息", description = "更新指定ID的学生信息")
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
    @Operation(summary = "删除学生", description = "删除指定ID的学生")
    public ResponseBody<Void> delete(@PathVariable("id") Long id,
                                       @RequestParam("teacherId") Long teacherId) {
        boolean deleted = studentsService.deleteByStudentIdAndTeacherId(id, teacherId);
        return deleted ? ResponseBody.success() : ResponseBody.error("404", "Student not found");
    }
}
