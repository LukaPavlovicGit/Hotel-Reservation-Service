package com.raf.example.HotelReservationService.runner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {

    }
}
