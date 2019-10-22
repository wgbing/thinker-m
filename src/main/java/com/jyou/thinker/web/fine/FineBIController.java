package com.jyou.thinker.web.fine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/fine/bi")
public class FineBIController {
    public static final String TPL_PREFIX = "/fine/bi";

    @GetMapping("/index")
    public String index() {
        return TPL_PREFIX + "/report";
    }
}
