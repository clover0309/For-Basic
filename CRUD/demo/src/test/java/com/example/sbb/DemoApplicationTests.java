package com.example.sbb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.sbb.answer.AnswerRepository;
import com.example.sbb.question.QuestionRepository;
import com.example.sbb.question.QuestionService;

@SpringBootTest
class DemoApplicationTests {
	
	// Autowired를 통해 객체 종속성 주입을 해야 questionRepository를 사용할 수 있음.
	// 자동으로 Bean객체를 생성해서 등록을 진행함. 따라서 Autowired를 사용해야함.
	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionService questionService;

	@Test
	void contextLoads() {
	}

    @Test
    void testJpa() {
		for(int i = 1; i <= 100; i++) {
			String subject = String.format("더미 데이터");
			String content = String.format("냉무");
			this.questionService.create(subject, content);
		}

    }

}
