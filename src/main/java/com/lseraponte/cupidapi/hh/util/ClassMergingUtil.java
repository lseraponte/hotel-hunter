package com.lseraponte.cupidapi.hh.util;

import com.lseraponte.cupidapi.hh.model.AmenityTranslation;
import com.lseraponte.cupidapi.hh.model.BedTypeTranslation;
import com.lseraponte.cupidapi.hh.model.FacilityTranslation;
import com.lseraponte.cupidapi.hh.model.HotelTranslation;
import com.lseraponte.cupidapi.hh.model.Photo;
import com.lseraponte.cupidapi.hh.model.RoomTranslation;

public class ClassMergingUtil {

    public static HotelTranslation hotelTranslationValues (HotelTranslation newValues,
                                                           HotelTranslation currentValues) {

        currentValues.setHotelName(newValues.getHotelName());
        currentValues.setHotelType(newValues.getHotelType());
        currentValues.setChain(newValues.getChain());
        currentValues.setDescription(newValues.getDescription());
        currentValues.setImportantInfo(newValues.getImportantInfo());
        currentValues.setMarkdownDescription(newValues.getMarkdownDescription());

        return currentValues;
    }

    public static RoomTranslation roomTranslationValues (RoomTranslation newValues,
                                                         RoomTranslation currentValues) {

        currentValues.setDescription(newValues.getDescription());
        currentValues.setRoomName(newValues.getRoomName());

        return currentValues;
    }

    public static AmenityTranslation amenityTranslationValues (AmenityTranslation newValues,
                                                               AmenityTranslation currentValues) {

        currentValues.setName(newValues.getName());

        return currentValues;
    }

    public static FacilityTranslation facilityTranslationValues (FacilityTranslation newValues,
                                                                 FacilityTranslation currentValues) {

        currentValues.setFacilityName(newValues.getFacilityName());

        return currentValues;
    }

    public static BedTypeTranslation bedTypeTranslationValues (BedTypeTranslation newValues,
                                                                BedTypeTranslation currentValues) {

        currentValues.setBedTypeName(newValues.getBedTypeName());
        currentValues.setBedSize(newValues.getBedSize());

        return currentValues;
    }

    public static Photo photoValues (Photo newValues, Photo currentValues) {

        currentValues.setUrl(newValues.getUrl());
        currentValues.setHdUrl(newValues.getHdUrl());
        currentValues.setImageDescription(newValues.getImageDescription());
        currentValues.setImageClass1(newValues.getImageClass1());
        currentValues.setImageClass2(newValues.getImageClass2());
        currentValues.setMainPhoto(newValues.getMainPhoto());
        currentValues.setScore(newValues.getScore());
        currentValues.setClassId(newValues.getClassId());
        currentValues.setClassOrder(newValues.getClassOrder());

        return currentValues;
    }

}
