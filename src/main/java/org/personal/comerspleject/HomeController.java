package org.personal.comerspleject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "서버가 정상적으로 실행 중입니다";
    }

}
