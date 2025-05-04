package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AuthorNotFoundException.class)
    public ModelAndView handeNotFoundException(AuthorNotFoundException ex) {
        String errorText = messageSource.getMessage("author-not-found-error", null,
                LocaleContextHolder.getLocale());
        return new ModelAndView("customError", "errorText", errorText);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ModelAndView handeBookNotFoundException(BookNotFoundException ex) {
        String errorText = messageSource.getMessage("book-not-found-error", null,
                LocaleContextHolder.getLocale());
        return new ModelAndView("customError", "errorText", errorText);
    }

}
