package com.example.sbb.answer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.sbb.question.Question;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void create(Question question, String content) {
        //여기서는 Answer 객체를 새로 생성하는 이유는, Answer객체가 아직 불려지지 않았고,
        // 각각의 answer의 VO에서 내용을 쓰고 저장을 해줘야하기 때문에, 객체를 새로 생성하였음.
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        this.answerRepository.save(answer);
    }
}
