package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    Optional<Photo> findByUrl(String url);

}
