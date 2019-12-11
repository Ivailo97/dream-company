package dreamcompany.web.controller;

import dreamcompany.common.GlobalConstants;
import dreamcompany.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ModelAndView handleBaseException(BaseException exception){
        return fillModelAndView(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView handleUsernameNotFound(UsernameNotFoundException exception){
        return fillModelAndView(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    private ModelAndView fillModelAndView(int statusCode, String message) {

        ModelAndView modelAndView = new ModelAndView(GlobalConstants.GLOBAL_EXCEPTION_VIEW_NAME);
        modelAndView.addObject(GlobalConstants.ERROR_PAGE_STATUS_CODE_ATTRIBUTE_NAME, statusCode);
        modelAndView.addObject(GlobalConstants.ERROR_PAGE_MESSAGE_ATTRIBUTE_NAME, message);
        return modelAndView;
    }
}
