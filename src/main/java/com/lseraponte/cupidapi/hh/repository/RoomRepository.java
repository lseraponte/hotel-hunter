package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.Amenity;
import com.lseraponte.cupidapi.hh.model.Photo;
import com.lseraponte.cupidapi.hh.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT a FROM Room r JOIN r.roomAmenities a WHERE r.id = :id")
    List<Amenity> findAmenityByRoomId(@Param("id") Integer id);

    @Query("SELECT p FROM Room r JOIN r.photos p WHERE r.id = :id")
    List<Photo> findPhotosByRoomId(@Param("id") Integer id);
}
