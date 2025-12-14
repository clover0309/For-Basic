package com.example.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sbb.answer.AnswerForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//question이라는 경로로 접속하는 것을 prefix로 지정함.
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/")
    public String index() {
        return "question_list";
    }
    @GetMapping("list")
    // 페이지네이션을 추가하였으므로, 페이지네이션을 사용하기 위해, @RequestParam을 통해, page의 수를 받아옴.
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        //QuestionService에 구현해둔 Page형태의 getList를 사용함.
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }


    // 쓴 질문글에 접근.
    @GetMapping(value = "/detail/{id}")
    // 마찬가지로, question_detail에서 answerForm을 통해 Validtion을 검증함으로, 파라미터에 AnswerForm을 통한 파라미터값을 받아야함.
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        //여기서 question_detail.html 파일을 뷰로 지정해서 보여줌.
        return "question_detail";
    }

    @GetMapping("/create")
    //글쓰기 페이지 내부에, QuestionForm을 통한 Vaildation을 적용하였으므로 파라미터에도 QuestionForm을 지정해야 접근시 오류가 없음.
    public String questionCreate(QuestionForm questionForm) {
        return "question_create";
    }

    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "question_create";
        }
        this.questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";
    }


    
}
