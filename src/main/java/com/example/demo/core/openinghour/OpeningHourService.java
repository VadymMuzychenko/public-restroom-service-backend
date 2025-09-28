package com.example.demo.core.openinghour;

import com.example.demo.core.openinghour.model.CreateOpeningHourRequest;
import com.example.demo.core.openinghour.model.OpeningHour;
import com.example.demo.core.openinghour.model.OpeningHourResponse;
import com.example.demo.core.wc.model.WaterCloset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpeningHourService {

    private final OpeningHourRepository openingHourRepository;

    public Set<OpeningHourResponse> addOpeningHours(Set<CreateOpeningHourRequest> set, WaterCloset waterCloset){
        return set.stream().map(request -> addOpeningHour(request, waterCloset)).collect(Collectors.toSet());
    }

    private OpeningHourResponse addOpeningHour(CreateOpeningHourRequest request, WaterCloset waterCloset){
        OpeningHour openingHour = new OpeningHour();
        openingHour.setDayOfWeek(request.getDayOfWeek());
        openingHour.setOpenTime(request.getOpenTime());
        openingHour.setCloseTime(request.getCloseTime());
        openingHour.setWaterCloset(waterCloset);
        OpeningHour saved = openingHourRepository.save(openingHour);
        return OpeningHourMapper.toOpeningHourResponse(saved);
    }

    public Set<OpeningHourResponse> getOpeningHours(UUID wcUuid) {
        return openingHourRepository.findAllByWaterCloset_Id(wcUuid).stream().map(OpeningHourMapper::toOpeningHourResponse).collect(Collectors.toSet());
    }

}
