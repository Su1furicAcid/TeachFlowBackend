package com.lss.teachflow.controller;

import com.lss.teachflow.common.ResponseBody;
import com.lss.teachflow.dto.StudentRequest;
import com.lss.teachflow.dto.StudentResponse;
import com.lss.teachflow.entity.Student;
import com.lss.teachflow.service.StudentsService;
import com.lss.teachflow.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Tag(name = "学生管理", description = "管理学生信息")
public class StudentsController {

    private final StudentsService studentsService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "获取学生列表", description = "获取当前教师的所有学生列表")
    public ResponseBody<List<StudentResponse>> list() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Long userId = userService.getUserIdByUsername(userName);
        List<Student> students = studentsService.findAllByTeacherId(userId);
        List<StudentResponse> body = students.stream()
                .map(StudentResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseBody.success(body);
    }

    @PostMapping
    @Operation(summary = "创建学生", description = "创建新学生")
    public ResponseBody<List<StudentResponse>> create(@Valid @RequestBody List<StudentRequest> requestList) {
        List<Student> studentsToSave = requestList.stream()
                .map(request -> Student.builder()
                        .studentName(request.getStudentName())
                        .studentNumber(request.getStudentNumber())
                        .grade(request.getGrade())
                        .clazz(request.getClazz())
                        .build())
                .collect(Collectors.toList());

        List<Student> savedStudents = studentsService.createStudents(studentsToSave);

        List<StudentResponse> body = savedStudents.stream()
                .map(StudentResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseBody.success(body, "Students created successfully");
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取学生信息", description = "获取指定ID的学生信息")
    public ResponseBody<StudentResponse> get(@PathVariable("id") Long id) {
        return studentsService.findByStudentId(id)
                .map(StudentResponse::fromEntity)
                .map(ResponseBody::success)
                .orElse(ResponseBody.error("404", "Student not found"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新学生信息", description = "更新指定ID的学生信息")
    public ResponseBody<StudentResponse> update(@PathVariable("id") Long id,
                                                  @Valid @RequestBody StudentRequest request) {
        Student toUpdate = Student.builder()
                .id(id)
                .studentName(request.getStudentName())
                .studentNumber(request.getStudentNumber())
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
    public ResponseBody<Void> delete(@PathVariable("id") Long id) {
        boolean deleted = studentsService.deleteByStudentId(id);
        return deleted ? ResponseBody.success() : ResponseBody.error("404", "Student not found");
    }
}
