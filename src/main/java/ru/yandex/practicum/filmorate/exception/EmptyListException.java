package ru.yandex.practicum.filmorate.exception;

public class EmptyListException extends RuntimeException {
    public EmptyListException(String message) {
        super(message);
    }
}
