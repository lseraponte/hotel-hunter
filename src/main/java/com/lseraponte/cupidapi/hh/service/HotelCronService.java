package com.lseraponte.cupidapi.hh.service;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelCronService {

    private static final Logger logger = LoggerFactory.getLogger(HotelCronService.class);
    private final CupidApiService cupidApiService;
    private final HotelService hotelService;
    private final HotelRepository hotelRepository;

    @Scheduled(cron = "0 0 * * * *") // Runs every hour
    public void fetchHotelsPeriodically() {
        logger.info("Starting hotel update cron job");

        List<Integer> hotelIds = hotelRepository.findAllHotelIds();

        hotelIds.forEach(hotelId -> {
            logger.info("Fetching data for hotel ID: {}", hotelId);

            try {
                HotelDTO hotelDTO = cupidApiService.getHotelByIdWithTranslation(hotelId, "en");
                HotelDTO hotelFr = cupidApiService.getHotelByIdWithTranslation(hotelId, "fr");
                HotelDTO hotelEs = cupidApiService.getHotelByIdWithTranslation(hotelId, "es");
                List<ReviewDTO> reviews = cupidApiService.getHotelReviews(hotelId, 10);

                if (hotelDTO != null && reviews != null) {
                    hotelService.updateHotel(hotelDTO, reviews, "en");
                    hotelService.updateHotel(hotelFr, reviews, "fr");
                    hotelService.updateHotel(hotelEs, reviews, "es");
                    logger.info("Successfully updated hotel ID: {}", hotelId);
                }
            } catch (Exception e) {
                logger.error("Failed to process hotel ID: {}", hotelId, e);
            }
        });

        logger.info("Hotel update cron job completed");
    }
}