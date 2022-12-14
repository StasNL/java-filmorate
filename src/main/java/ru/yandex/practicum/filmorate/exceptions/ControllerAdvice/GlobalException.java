package ru.yandex.practicum.filmorate.exceptions.ControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationExceptionResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<Error> errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Error(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        log.info(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
        return new ValidationExceptionResponse(errors);
    }

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<String> onNullPointerException(RuntimeException e) {
        final List<String> npe = new ArrayList<>();
        npe.add(e.getMessage());
        log.info(e.getMessage());
        return npe;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public List<String> onRuntimeException(RuntimeException e) {
        final List<String> re = new ArrayList<>();
        re.add(e.getMessage());
        log.info(e.getMessage());
        return re;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public List<String> onNotFoundException(NotFoundException e) {
        final List<String> nfe = new ArrayList<>();
        nfe.add(e.getMessage());
        log.info(e.getMessage());
        return nfe;
    }
}
