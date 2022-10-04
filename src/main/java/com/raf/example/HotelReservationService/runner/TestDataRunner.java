package com.raf.example.HotelReservationService.runner;
import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.domain.Room;
import com.raf.example.HotelReservationService.domain.RoomType;
import com.raf.example.HotelReservationService.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private HotelRepository hotelRepository;
    private RoomTypeRepository roomTypeRepository;
    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;
    private ReviewRepository reviewRepository;


    public TestDataRunner(HotelRepository hotelRepository, RoomTypeRepository roomTypeRepository,
                          RoomRepository roomRepository, ReservationRepository reservationRepository, ReviewRepository reviewRepository) {
        this.hotelRepository = hotelRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        /*
        Hotel h1 = new Hotel("","hotel1","desc");
        Hotel h2 = new Hotel("","hotel2","desc");
        Hotel h3 = new Hotel("","hotel3","desc");
        Hotel h4 = new Hotel("","hotel4","desc");
        Hotel h5 = new Hotel("","hotel5","desc");
        Hotel h6 = new Hotel("","hotel6","desc");
        Hotel h7 = new Hotel("","hotel7","desc");

        hotelRepository.save(h1);
        hotelRepository.save(h2);
        hotelRepository.save(h3);
        hotelRepository.save(h4);
        hotelRepository.save(h5);
        hotelRepository.save(h6);
        hotelRepository.save(h7);

        RoomType rt1h1 = new RoomType("A", 20.0, h1.getId());
        RoomType rt2h1 = new RoomType("B", 30.0, h1.getId());
        RoomType rt3h1 = new RoomType("C", 60.0, h1.getId());

        RoomType rt1h2 = new RoomType("A", 13.0, h2.getId());
        RoomType rt2h2 = new RoomType("B", 20.0, h2.getId());
        RoomType rt3h2 = new RoomType("C", 30.0, h2.getId());

        RoomType rt1h3 = new RoomType("A", 17.0, h3.getId());
        RoomType rt2h3 = new RoomType("B", 30.0, h3.getId());
        RoomType rt3h3 = new RoomType("C", 40.0, h3.getId());

        RoomType rt1h4 = new RoomType("A", 10.0, h4.getId());
        RoomType rt2h4 = new RoomType("B", 20.0, h4.getId());
        RoomType rt3h4 = new RoomType("C", 30.0, h4.getId());

        RoomType rt1h5 = new RoomType("A", 15.0, h5.getId());
        RoomType rt2h5 = new RoomType("B", 25.0, h5.getId());
        RoomType rt3h5 = new RoomType("C", 40.0, h5.getId());

        RoomType rt1h6 = new RoomType("A", 20.0, h6.getId());
        RoomType rt2h6 = new RoomType("B", 40.0, h6.getId());
        RoomType rt3h6 = new RoomType("C", 60.0, h6.getId());

        RoomType rt1h7 = new RoomType("A", 10.0, h7.getId());
        RoomType rt2h7 = new RoomType("B", 20.0, h7.getId());
        RoomType rt3h7 = new RoomType("C", 40.0, h7.getId());

        roomTypeRepository.save(rt1h1);
        roomTypeRepository.save(rt2h1);
        roomTypeRepository.save(rt3h1);
        roomTypeRepository.save(rt1h2);
        roomTypeRepository.save(rt2h2);
        roomTypeRepository.save(rt3h2);
        roomTypeRepository.save(rt1h3);
        roomTypeRepository.save(rt2h3);
        roomTypeRepository.save(rt3h3);
        roomTypeRepository.save(rt1h4);
        roomTypeRepository.save(rt2h4);
        roomTypeRepository.save(rt3h4);
        roomTypeRepository.save(rt1h5);
        roomTypeRepository.save(rt2h5);
        roomTypeRepository.save(rt3h5);
        roomTypeRepository.save(rt1h6);
        roomTypeRepository.save(rt2h6);
        roomTypeRepository.save(rt3h6);
        roomTypeRepository.save(rt1h7);
        roomTypeRepository.save(rt2h7);
        roomTypeRepository.save(rt3h7);

        Room r1h1 = new Room(h1.getId(), 1, rt1h1);
        Room r2h1 = new Room(h1.getId(), 2, rt1h1);
        Room r3h1 = new Room(h1.getId(), 3, rt2h1);
        Room r4h1 = new Room(h1.getId(), 4, rt2h1);
        Room r5h1 = new Room(h1.getId(), 5, rt3h1);
        Room r6h1 = new Room(h1.getId(), 6, rt3h1);

        Room r1h2 = new Room(h2.getId(), 1, rt1h2);
        Room r2h2 = new Room(h2.getId(), 2, rt1h2);
        Room r3h2 = new Room(h2.getId(), 3, rt2h2);
        Room r4h2 = new Room(h2.getId(), 4, rt2h2);
        Room r5h2 = new Room(h2.getId(), 5, rt3h2);
        Room r6h2 = new Room(h2.getId(), 6, rt3h2);

        Room r1h3 = new Room(h3.getId(), 1, rt1h3);
        Room r2h3 = new Room(h3.getId(), 2, rt1h3);
        Room r3h3 = new Room(h3.getId(), 3, rt2h3);
        Room r4h3 = new Room(h3.getId(), 4, rt2h3);
        Room r5h3 = new Room(h3.getId(), 5, rt3h3);
        Room r6h3 = new Room(h3.getId(), 6, rt3h3);

        Room r1h4 = new Room(h4.getId(), 1, rt1h4);
        Room r2h4 = new Room(h4.getId(), 2, rt1h4);
        Room r3h4 = new Room(h4.getId(), 3, rt2h4);
        Room r4h4 = new Room(h4.getId(), 4, rt2h4);
        Room r5h4 = new Room(h4.getId(), 5, rt3h4);
        Room r6h4 = new Room(h4.getId(), 6, rt3h4);

        roomRepository.save(r1h1);
        roomRepository.save(r2h1);
        roomRepository.save(r3h1);
        roomRepository.save(r4h1);
        roomRepository.save(r5h1);
        roomRepository.save(r6h1);

        roomRepository.save(r1h2);
        roomRepository.save(r2h2);
        roomRepository.save(r3h2);
        roomRepository.save(r4h2);
        roomRepository.save(r5h2);
        roomRepository.save(r6h2);

        roomRepository.save(r1h3);
        roomRepository.save(r2h3);
        roomRepository.save(r3h3);
        roomRepository.save(r4h3);
        roomRepository.save(r5h3);
        roomRepository.save(r6h3);

        roomRepository.save(r1h4);
        roomRepository.save(r2h4);
        roomRepository.save(r3h4);
        roomRepository.save(r4h4);
        roomRepository.save(r5h4);
        roomRepository.save(r6h4);
    */


    }
}
