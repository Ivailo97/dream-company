package dreamcompany.web.controller;

import dreamcompany.GlobalConstraints;
import dreamcompany.error.duplicates.*;
import dreamcompany.error.WrongOldPasswordException;
import dreamcompany.error.invalidservicemodels.InvalidUserServiceModelException;
import dreamcompany.error.notexist.TaskNotFoundException;
import dreamcompany.error.notexist.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ModelAndView handleUsernameAlreadyExist(UsernameAlreadyExistException exception) {
        return fillModelAndView(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ModelAndView handleEmailAlreadyExist(EmailAlreadyExistException exception) {
        return fillModelAndView(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(WrongOldPasswordException.class)
    public ModelAndView handleWrongOldPassword(WrongOldPasswordException exception) {
        return fillModelAndView(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(ProjectNameAlreadyExistException.class)
    public ModelAndView handleProjectNameAlreadyExist(ProjectNameAlreadyExistException exception) {
        return fillModelAndView(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(TaskNameAlreadyExistException.class)
    public ModelAndView handleTaskNameAlreadyExist(TaskNameAlreadyExistException exception) {
        return fillModelAndView(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(TeamNameAlreadyExistException.class)
    public ModelAndView handleTeamNameAlreadyExist(TeamNameAlreadyExistException exception) {
        return fillModelAndView(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFound(UserNotFoundException exception) {
        return fillModelAndView(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ModelAndView handleTaskNotFound(TaskNotFoundException exception) {
        return fillModelAndView(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(InvalidUserServiceModelException.class)
    public ModelAndView handleUserServiceModelNotValid(InvalidUserServiceModelException exception){
        return fillModelAndView(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView handleUsernameNotFound(UsernameNotFoundException exception){
        return fillModelAndView(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }


    private ModelAndView fillModelAndView(int statusCode, String message) {

        ModelAndView modelAndView = new ModelAndView(GlobalConstraints.GLOBAL_EXCEPTION_VIEW_NAME);
        modelAndView.addObject(GlobalConstraints.ERROR_PAGE_STATUS_CODE_ATTRIBUTE_NAME, statusCode);
        modelAndView.addObject(GlobalConstraints.ERROR_PAGE_MESSAGE_ATTRIBUTE_NAME, message);

        return modelAndView;
    }
}
