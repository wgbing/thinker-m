package com.jyou.thinker.web.rest;

import com.jyou.thinker.common.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rest/test")
public class TestRestController {

    @GetMapping("/get")
    public ApiResult test(){
        return ApiResult.success();
    }
}
