package com.lseraponte.cupidapi.hh.service;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelStartupService {

    private static final Logger logger = LoggerFactory.getLogger(HotelStartupService.class);

    private final CupidApiService cupidApiService;
    private final HotelService hotelService; // Assuming you have a repository

    public HotelStartupService(CupidApiService cupidApiService,
                               HotelService hotelService) {
        this.cupidApiService = cupidApiService;
        this.hotelService = hotelService;
    }

    @PostConstruct
    public void fetchHotelsOnStartup() {
        List<Integer> hotelIds = loadHotelIdsFromFile("hotel_ids.txt");
//        List<Integer> hotelIds = loadHotelIdsFromFile("partial_hotel_ids.txt");

        Flux.fromIterable(hotelIds)
                .concatMap(hotelId -> {
                    logger.info("Fetching data for hotel ID: {}", hotelId);

                    return cupidApiService.getHotelById(hotelId)
                            .onErrorResume(e -> {
                                logger.error("Failed to fetch hotel ID: {}. Skipping...", hotelId, e);
                                return Mono.empty(); // Stop processing for this hotel
                            })
                            .zipWhen(h -> cupidApiService.getHotelByIdWithTranslation(hotelId, "fr"))
                            .zipWhen(h -> cupidApiService.getHotelByIdWithTranslation(hotelId, "es"))
                            .zipWhen(h -> cupidApiService.getHotelReviews(hotelId, 10).collectList())
                            .flatMap(tuple -> {
                                HotelDTO hotel = tuple.getT1().getT1().getT1();
                                HotelDTO hotelFr = tuple.getT1().getT1().getT2();
                                HotelDTO hotelEs = tuple.getT1().getT2();
                                List<ReviewDTO> reviews = tuple.getT2();

                                logger.info("Saving hotel data for hotel ID: {}", hotelId);

                                return Mono.fromCallable(() -> hotelService.saveHotelWithTranslation(hotel, "en", reviews))
                                        .then(Mono.fromCallable(() -> hotelService.saveHotelWithTranslation(hotelFr, "fr", reviews)))
                                        .then(Mono.fromCallable(() -> hotelService.saveHotelWithTranslation(hotelEs, "es", reviews)))
                                        .doOnSuccess(v -> logger.info("Successfully saved hotel ID: {}", hotelId));
                            });
                })
                .doOnComplete(() -> logger.info("All hotels processed successfully."))
                .doOnError(error -> logger.error("Error processing hotels: {}", error.getMessage(), error))
                .subscribe();
    }

    private List<Integer> loadHotelIdsFromFile(String fileName) {
        try {
            Path path = new ClassPathResource(fileName).getFile().toPath();
            return Files.readAllLines(path).stream()
                    .filter(line -> !line.trim().isEmpty())  // Ignore empty lines
                    .map(Integer::parseInt)                  // Convert each line to an integer
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load hotel IDs from file", e);
        }
    }

}