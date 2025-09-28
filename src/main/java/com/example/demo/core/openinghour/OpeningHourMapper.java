package com.example.demo.core.openinghour;

import com.example.demo.core.openinghour.model.OpeningHour;
import com.example.demo.core.openinghour.model.OpeningHourResponse;

public class OpeningHourMapper {

    public static OpeningHourResponse toOpeningHourResponse(OpeningHour openingHour) {
        OpeningHourResponse openingHourResponse = new OpeningHourResponse();
        openingHourResponse.setId(openingHour.getId());
        openingHourResponse.setDayOfWeek(openingHour.getDayOfWeek());
        openingHourResponse.setOpenTime(openingHour.getOpenTime());
        openingHourResponse.setCloseTime(openingHour.getCloseTime());
        return openingHourResponse;
    }
}
