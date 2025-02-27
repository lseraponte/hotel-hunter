package com.lseraponte.cupidapi.hh.util;

import com.lseraponte.cupidapi.hh.model.AmenityTranslation;
import com.lseraponte.cupidapi.hh.model.FacilityTranslation;
import com.lseraponte.cupidapi.hh.model.HotelTranslation;
import com.lseraponte.cupidapi.hh.model.Room;
import com.lseraponte.cupidapi.hh.model.RoomTranslation;

public class TranslationsUtil {

    public static HotelTranslation hotelTranslationValues (HotelTranslation newTranslation,
                                                           HotelTranslation currentTranslation) {

        currentTranslation.setHotelName(newTranslation.getHotelName());
        currentTranslation.setHotelType(newTranslation.getHotelType());
        currentTranslation.setChain(newTranslation.getChain());
        currentTranslation.setDescription(newTranslation.getDescription());
        currentTranslation.setImportantInfo(newTranslation.getImportantInfo());
        currentTranslation.setMarkdownDescription(newTranslation.getMarkdownDescription());

        return currentTranslation;
    }

    public static RoomTranslation roomTranslationValues (RoomTranslation newTranslation,
                                                         RoomTranslation currentTranslation) {

        currentTranslation.setDescription(newTranslation.getDescription());
        currentTranslation.setRoomName(newTranslation.getRoomName());

        return currentTranslation;
    }

    public static AmenityTranslation amenityTranslationValues (AmenityTranslation newTranslation,
                                                               AmenityTranslation currentTranslation) {

        currentTranslation.setName(newTranslation.getName());

        return currentTranslation;
    }

    public static FacilityTranslation facilityTranslationValues (FacilityTranslation newTranslation,
                                                                 FacilityTranslation currentTranslation) {

        currentTranslation.setFacilityName(newTranslation.getFacilityName());

        return currentTranslation;
    }

}
