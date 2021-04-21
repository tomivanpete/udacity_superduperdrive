package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorHandlingController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        String errorPageTitle = "500 - Internal Server Error";
        String errorPageMsg = "An internal error occurred.";

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            logger.error("Got HTTP Status Code: " + statusCode);

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorPageTitle = "404 - Page Not Found";
                errorPageMsg = "The page requested was not found.";
            }
        }

        model.addAttribute("errorPageTitle", errorPageTitle);
        model.addAttribute("errorPageMsg", errorPageMsg);
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
