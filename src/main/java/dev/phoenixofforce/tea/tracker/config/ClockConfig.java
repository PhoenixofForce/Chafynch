package dev.phoenixofforce.tea.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfig {

    @Bean
    @SuppressWarnings("TimeZoneUsage")
    public Clock getClock() {
        return Clock.systemUTC();
    }

}
