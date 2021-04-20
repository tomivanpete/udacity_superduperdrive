package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.DuplicateFileNameException;
import com.udacity.jwdnd.course1.cloudstorage.exception.InvalidUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class ExceptionHandlingController {

    private Logger logger = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxUploadSize;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String maxUploadSizeExceededExceptionHandler(Model model) {
        model.addAttribute("errorWithMsg", true);
        model.addAttribute("errorMsg", "File exceeds size limit of " + maxUploadSize + ".");
        return "result";
    }

    @ExceptionHandler(DuplicateFileNameException.class)
    public String duplicateFilenameExceptionHandler(Model model) {
        model.addAttribute("errorWithMsg", true);
        model.addAttribute("errorMsg", "Cannot have more than 1 file with the same name.");
        return "result";
    }

    @ExceptionHandler(InvalidUserException.class)
    public String invalidUserExceptionHandler(Model model, InvalidUserException invalidUserException) {
        model.addAttribute("errorWithMsg", true);
        model.addAttribute("errorMsg", invalidUserException.getMessage());
        return "result";
    }

    @ExceptionHandler(Exception.class)
    public String baseExceptionHandler(Model model, Exception exception) {
        logger.error(exception.getMessage(), exception);
        model.addAttribute("error", true);
        return "result";
    }
}
