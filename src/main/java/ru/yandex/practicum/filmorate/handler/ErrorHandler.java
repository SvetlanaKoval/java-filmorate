package ru.yandex.practicum.filmorate.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.EmptyListException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ErrorResponse handleDuplicatedData(final DuplicatedDataException e) {
        log.error(String.format("%s, reason - %s", e.getClass().getName(), e.getMessage()));
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleNotFound(final NotFoundException e) {
        log.error(String.format("%s, reason - %s", e.getClass().getName(), e.getMessage()));
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleValidate(final ValidateException e) {
        log.error(String.format("%s, reason - %s", e.getClass().getName(), e.getMessage()));
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleValidate(final MethodArgumentNotValidException e) {
        log.error(String.format("%s, reason - %s", e.getClass().getName(), e.getMessage()));
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler
    public ErrorResponse handleEmptyList(final EmptyListException e) {
        log.error(String.format("%s, reason - %s", e.getClass().getName(), e.getMessage()));
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse handleError(final Throwable e) {
        log.error(String.format("%s, reason - %s", e.getClass().getName(), e.getMessage()));
        return new ErrorResponse(e.getMessage());
    }

}
