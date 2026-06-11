package com.librosonline.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("errorTitle", "404 - No Encontrado");
        modelAndView.addObject("errorDescription", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("errorTitle", "500 - Error Interno");
        modelAndView.addObject("errorDescription", "Lo sentimos, ocurrió un error inesperado en nuestros servidores.");
        modelAndView.addObject("errorDetails", ex.getMessage());
        return modelAndView;
    }
}
