package com.raf.example.HotelReservationService.runner;
import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.repository.HotelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private HotelRepository hotelRepository;

    public TestDataRunner(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Hotel h1 = new Hotel();
        h1.setManagerId(4L);
        h1.setCity("Cacak");
        h1.setNumberOfRooms(400);
        h1.setDescription("desc");
        h1.setName("h1");

        Hotel h2 = new Hotel();
        h2.setManagerId(5L);
        h2.setCity("Beograd");
        h2.setNumberOfRooms(156);
        h2.setDescription("desc");
        h2.setName("h2");

        hotelRepository.save(h1);
        hotelRepository.save(h2);
    }
}
