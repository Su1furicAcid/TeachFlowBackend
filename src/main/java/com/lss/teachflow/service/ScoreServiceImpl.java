package com.lss.teachflow.service;

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

        List<Long> studentIds = scoreRequests.stream()
                .map(ScoreUploadRequest::getStudentId)
                .collect(Collectors.toList());

        Map<Long, Student> studentMap = studentRepository.findAllById(studentIds).stream()
                .collect(Collectors.toMap(Student::getStudentId, Function.identity()));

        Map<Long, Score> existingScoresMap = scoreRepository.findByExamId(examId).stream()
                .collect(Collectors.toMap(score -> score.getStudent().getStudentId(), Function.identity()));
        List<Score> scoresToSave = new ArrayList<>();
        for (ScoreUploadRequest req : scoreRequests) {
            Student student = studentMap.get(req.getStudentId());
            if (student == null) {
                System.err.println("Student not found with id: " + req.getStudentId());
                continue;
            }
            Score score = existingScoresMap.get(req.getStudentId());
            if (score != null) {
                score.setScoreValue(req.getScoreValue());
                scoresToSave.add(score);
            } else {
                Score newScore = Score.builder()
                        .exam(exam)
                        .student(student)
                        .scoreValue(req.getScoreValue())
                        .build();
                scoresToSave.add(newScore);
            }
        }
        scoreRepository.saveAll(scoresToSave);
    }
}
