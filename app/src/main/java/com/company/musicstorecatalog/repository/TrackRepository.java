package com.company.musicstorecatalog.repository;

import com.company.musicstorecatalog.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    public List<Track> findAllByAlbumId(long id);
}
