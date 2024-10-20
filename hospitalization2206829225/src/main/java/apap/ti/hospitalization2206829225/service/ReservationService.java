package apap.ti.hospitalization2206829225.service;


import java.util.Date;
import java.util.List;
import java.util.UUID;

import apap.ti.hospitalization2206829225.model.Reservation;
import apap.ti.hospitalization2206829225.restdto.response.ReservationResponseDTO;

public interface ReservationService {
    int getTotalReservations();
    String generateReservationId(Date dateIn, Date dateOut, UUID patientId);
    Reservation addReservation(Reservation reservation);
    List<Reservation> getAllReservation();
    List<ReservationResponseDTO> getAllReservationFromRest() throws Exception;
    Reservation getReservationById(String id);
    void deleteReservation(String id);
}
