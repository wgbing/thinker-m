package com.jyou.thinker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.jyou.thinker.mapper")
public class ThinkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThinkerApplication.class, args);
    }

}
