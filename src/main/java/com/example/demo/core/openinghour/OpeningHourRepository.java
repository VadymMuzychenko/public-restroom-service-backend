package com.example.demo.core.openinghour;

import com.example.demo.core.openinghour.model.OpeningHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OpeningHourRepository extends JpaRepository<OpeningHour, UUID> {
    List<OpeningHour> findAllByWaterCloset_Id(UUID waterClosetId);
}
