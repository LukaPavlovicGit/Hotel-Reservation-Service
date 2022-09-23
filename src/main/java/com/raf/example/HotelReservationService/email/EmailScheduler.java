package com.raf.example.HotelReservationService.email;

import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.domain.Reservation;
import com.raf.example.HotelReservationService.dto.MessageDto;
import com.raf.example.HotelReservationService.dto.UserDto;
import com.raf.example.HotelReservationService.messageHelper.MessageHelper;
import com.raf.example.HotelReservationService.repository.HotelRepository;
import com.raf.example.HotelReservationService.repository.ReservationRepository;
import com.raf.example.HotelReservationService.service.HotelService;
import com.raf.example.HotelReservationService.service.ReservationService;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Component
@EnableScheduling
public class EmailScheduler {
    private ReservationService reservationService;
    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    private RestTemplate userServiceRestTemplate;
    private HotelService hotelService;
    private ReservationRepository reservationRepository;

    private HotelRepository hotelRepository;

    public EmailScheduler(ReservationService reservationService, JmsTemplate jmsTemplate, MessageHelper messageHelper,
                          RestTemplate userServiceRestTemplate, HotelService hotelService, ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.userServiceRestTemplate = userServiceRestTemplate;
        this.hotelService = hotelService;
        this.reservationRepository = reservationRepository;
    }

    @Scheduled(cron = "0 */10 * ? * *") // za slanje svakih 10 minuta
    //@Scheduled(fixedDelay = 10000) // svakih 10 sekundi
    public void sendReminderEmails() {
        List<Reservation> reservations = reservationRepository.findAll();

        for (Reservation reservation: reservations) {
            if (reservation.getSentReminder())
                continue;

            if (Period.between(LocalDate.now(), reservation.getStartDate()).getDays() <= 2) {
                reservation.setSentReminder(true);
                reservationRepository.save(reservation);
                Hotel hotel = hotelRepository.findById(reservation.getHotelId()).get();

                ResponseEntity<UserDto> userDto = userServiceRestTemplate.exchange(
                        "http://localhost:8080/api/users/get/" + reservation.getUserId(), HttpMethod.GET,
                        null, UserDto.class);

                MessageDto messageDto = new MessageDto("reservation_reminder", userDto.getBody().getFirstName(),
                        userDto.getBody().getLastName(), userDto.getBody().getEmail(), hotel.getName(), reservation.getStartDate().toString());

                String messageToSend = messageHelper.createTextMessage(messageDto);
                jmsTemplate.convertAndSend("send_mail_queue", messageToSend);
            }
        }
    }
}