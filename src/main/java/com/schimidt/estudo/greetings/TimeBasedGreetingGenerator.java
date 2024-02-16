package com.schimidt.estudo.greetings;

import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class TimeBasedGreetingGenerator {

    String generateGreetingBasedOnCurrentTime() {
        final LocalTime time = LocalTime.now();

        if (time.getHour() < 12) {
            return "Bom dia";

        } else if (time.getHour() < 18) {
            return "Boa tarde";
        }

        return "Boa noite";
    }
}
