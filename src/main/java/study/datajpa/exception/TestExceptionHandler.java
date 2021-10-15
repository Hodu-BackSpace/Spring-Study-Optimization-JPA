package study.datajpa.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;

@RestControllerAdvice
@RequiredArgsConstructor
public class TestExceptionHandler {

    @ExceptionHandler(value = {NoResultException.class, RuntimeException.class})
    public String testException(Exception e) {
        return "test Exception" + e.getMessage();
    }
}
