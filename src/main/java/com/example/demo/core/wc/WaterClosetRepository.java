package com.example.demo.core.wc;


import com.example.demo.core.wc.model.WaterCloset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface WaterClosetRepository extends JpaRepository<WaterCloset, UUID> {

    @Query(value = """
        SELECT wc.* FROM water_closet wc
        WHERE ST_DWithin(
            wc.location,
            ST_SetSRID(ST_Point(:lon, :lat), 4326)::geography,
            :radius
        )
        """, nativeQuery = true)
    List<WaterCloset> findWaterClosetsWithinRadius(@Param("lon") double lon,
                                                   @Param("lat") double lat,
                                                   @Param("radius") double radiusMeters);
}
