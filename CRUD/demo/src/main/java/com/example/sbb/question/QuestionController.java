package com.example.sbb.question;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/question/list")
    // 여기서 파라미터를 Model로 지정한 이유는 22번줄에 존재함.
    public String list(Model model) {
        //여기서 QuestionService에서 getList() 메서드를 호출해서 데이터를 가져오고.
        List<Question> questionList = this.questionService.getList();
        //model 파라미터에, questionList라는 이름으로 담아서 뷰로 전달을 진행한다.
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @GetMapping("/")
    public String index() {
        //redirect를 이용하여 사이트 메인 진입시 question/list로 이동하게 설정을 진행함.
        return "redirect:/question/list";
    }

    // 쓴 질문글에 접근.
    @GetMapping(value = "/question/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }
}
