package com.lss.teachflow.service;

import com.lss.teachflow.dto.ScoreResponse;
import com.lss.teachflow.dto.ScoreUploadRequest;
import com.lss.teachflow.entity.Exam;
import com.lss.teachflow.entity.Score;
import com.lss.teachflow.entity.Student;
import com.lss.teachflow.repository.ExamRepository;
import com.lss.teachflow.repository.ScoreRepository;
import com.lss.teachflow.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public void batchUpdateScores(Long examId, List<ScoreUploadRequest> scoreRequests) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));

        List<String> studentNumbers = scoreRequests.stream()
                .map(ScoreUploadRequest::getStudentNumber)
                .collect(Collectors.toList());

        List<Student> students = studentRepository.findByStudentNumberIn(studentNumbers);
        Map<String, Student> studentMap = students.stream()
                .collect(Collectors.toMap(Student::getStudentNumber, Function.identity()));
        List<Score> scoresToSave = new ArrayList<>();
        for (ScoreUploadRequest request : scoreRequests) {
            Student student = studentMap.get(request.getStudentNumber());
            if (student == null) {
                throw new RuntimeException("Student not found with student number: " + request.getStudentNumber());
            }
            Score score = scoreRepository.findByExam_ExamIdAndStudent_Id(examId, student.getId())
                    .orElse(new Score());
            score.setExam(exam);
            score.setStudent(student);
            score.setScoreValue(request.getScoreValue());
            scoresToSave.add(score);
        }
        scoreRepository.saveAll(scoresToSave);
    }

    @Override
    public List<ScoreResponse> getScoresByExamId(Long examId) {
        return scoreRepository.findByExam_ExamId(examId).stream().map(ScoreResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<ScoreResponse> getScoresByStudentId(Long studentId) {
        return scoreRepository.findByStudent_Id(studentId).stream().map(ScoreResponse::fromEntity).collect(Collectors.toList());
    }
}
