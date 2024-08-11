package pl.rg.GithubRepositories.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rg.GithubRepositories.model.ErrorDto;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorDto handleUserNotFoundException(UserNotFoundException exception) {
        return new ErrorDto(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(InvalidHeaderException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public @ResponseBody ErrorDto handleInvalidHeaderException(InvalidHeaderException exception) {
        return new ErrorDto(HttpStatus.NOT_ACCEPTABLE.value(), exception.getMessage());
    }
}
