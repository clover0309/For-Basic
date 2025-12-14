package com.example.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/question/list";
    }

    @GetMapping("/sbb")
    @ResponseBody
    public String index() {
        return "안녕하세요. 이건 sbb입니다.";
    }
}
