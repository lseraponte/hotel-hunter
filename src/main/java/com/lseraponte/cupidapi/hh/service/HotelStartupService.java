package com.lseraponte.cupidapi.hh.service;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelStartupService {

    private static final Logger logger = LoggerFactory.getLogger(HotelStartupService.class);
    private final CupidApiService cupidApiService;
    private final HotelService hotelService;

    @PostConstruct
    public void fetchHotelsOnStartup() {
//        List<Integer> hotelIds = loadHotelIdsFromFile("hotel_ids.txt");
        List<Integer> hotelIds = loadHotelIdsFromFile("partial_hotel_ids.txt");

        hotelIds.forEach(hotelId -> {
            logger.info("Fetching data for hotel ID: {}", hotelId);
            try {
                HotelDTO hotelDTO = cupidApiService.getHotelByIdWithTranslation(hotelId, "en");
                HotelDTO hotelFr = cupidApiService.getHotelByIdWithTranslation(hotelId, "fr");
                HotelDTO hotelEs = cupidApiService.getHotelByIdWithTranslation(hotelId, "es");
                List<ReviewDTO> reviews = cupidApiService.getHotelReviews(hotelId, 10);

                if (hotelDTO != null && reviews != null) {
                    hotelService.saveHotelWithTranslation(hotelDTO, "en", reviews);
                    hotelService.updateHotel(hotelFr, reviews, "fr");
                    hotelService.updateHotel(hotelEs, reviews, "es");
                    logger.info("Successfully saved hotel ID: {}", hotelId);
                }
            } catch (Exception e) {
                logger.error("Failed to process hotel ID: {}", hotelId, e);
            }
        });

        logger.info("All hotels processed successfully.");
    }

    private List<Integer> loadHotelIdsFromFile(String fileName) {
        try {
            Path path = new ClassPathResource(fileName).getFile().toPath();
            return Files.readAllLines(path).stream()
                    .filter(line -> !line.trim().isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load hotel IDs from file", e);
        }
    }
}