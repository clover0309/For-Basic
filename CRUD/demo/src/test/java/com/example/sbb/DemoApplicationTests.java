package com.example.sbb;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.sbb.answer.Answer;
import com.example.sbb.answer.AnswerRepository;
import com.example.sbb.question.Question;
import com.example.sbb.question.QuestionRepository;

@SpringBootTest
class DemoApplicationTests {
	
	// Autowired를 통해 객체 종속성 주입을 해야 questionRepository를 사용할 수 있음.
	// 자동으로 Bean객체를 생성해서 등록을 진행함. 따라서 Autowired를 사용해야함.
	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Test
	void contextLoads() {
	}

    @Test
    void testJpa() {
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);
		a.setCreateDate(java.time.LocalDateTime.now());
		this.answerRepository.save(a);

    }

}
