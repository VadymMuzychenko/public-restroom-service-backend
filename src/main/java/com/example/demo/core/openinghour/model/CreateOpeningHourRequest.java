package com.example.demo.core.openinghour.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOpeningHourRequest {

    private Short dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
}
