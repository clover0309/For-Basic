package com.example.sbb.question;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.example.sbb.answer.Answer;
import com.example.sbb.answer.AnswerForm;
import com.example.sbb.user.SiteUser;
import com.example.sbb.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//question이라는 경로로 접속하는 것을 prefix로 지정함.
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

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

    //PreAuthorize 어노테이션은 로그인한 경우에만 수행할수 있게해주는 어노테이션이다.
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    //글쓰기 페이지 내부에, QuestionForm을 통한 Vaildation을 적용하였으므로 파라미터에도 QuestionForm을 지정해야 접근시 오류가 없음.
    public String questionCreate(QuestionForm questionForm) {
        return "question_create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_create";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, 
            Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

}
