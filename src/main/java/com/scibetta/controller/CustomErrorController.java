package com.scibetta.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value())
                model.addAttribute("code", "Error 404: page not found :(");
            else if (statusCode == HttpStatus.FORBIDDEN.value())
                model.addAttribute("code", "Error 403: access denied");
            else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
                model.addAttribute("code", "Error 500: internal server error");
        }
        return "error";
    }

}
