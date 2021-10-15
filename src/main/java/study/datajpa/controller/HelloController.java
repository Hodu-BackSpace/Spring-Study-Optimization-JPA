package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/hello")
    public String hello(HttpServletResponse response) throws IOException {
        throw new RuntimeException();
    }

    @PostMapping("/hellop")
    public String helloP() {
        return "hello";
    }

}
