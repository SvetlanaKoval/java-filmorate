package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.RatingRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public Rating findById(Long id) {
        return ratingRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Rating not found"));
    }

}