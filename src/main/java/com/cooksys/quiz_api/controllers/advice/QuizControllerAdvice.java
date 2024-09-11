package com.cooksys.quiz_api.controllers.advice;

import com.cooksys.quiz_api.dtos.ErrorResponseDto;
import com.cooksys.quiz_api.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = { "com.cooksys.quiz_api.controllers" })
@ResponseBody
public class QuizControllerAdvice {
    // This is required to prevent massive walls of text in error responses.
    // We only need the message from the custom NotFoundException.
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponseDto handleNotFoundException(NotFoundException notFoundException) {
        return new ErrorResponseDto(notFoundException.getMessage());
    }
}
