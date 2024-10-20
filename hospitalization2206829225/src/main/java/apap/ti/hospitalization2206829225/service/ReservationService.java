package apap.ti.hospitalization2206829225.service;

import java.time.LocalDate;
import java.util.UUID;

import apap.ti.hospitalization2206829225.model.Reservation;

public interface ReservationService {
    int getTotalReservations();
    String generateReservationId(LocalDate dateIn, LocalDate dateOut, UUID patientId);
    Reservation addReservation(Reservation reservation);
    
}
