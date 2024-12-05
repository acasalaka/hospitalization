package apap.ti.hospitalization2206829225.restservice;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.sql.Update;

import java.util.Date;

import apap.ti.hospitalization2206829225.model.Reservation;
import apap.ti.hospitalization2206829225.model.Room;
import apap.ti.hospitalization2206829225.restdto.request.AddReservationRequestRestDTO;
import apap.ti.hospitalization2206829225.restdto.request.UpdateReservationRequestRestDTO;
import apap.ti.hospitalization2206829225.restdto.response.FacilityResponseDTO;
import apap.ti.hospitalization2206829225.restdto.response.ReservationResponseDTO;

public interface ReservationRestService {
    List<ReservationResponseDTO> getAllReservation();
    ReservationResponseDTO getReservationById(String id);
    ReservationResponseDTO addReservation(AddReservationRequestRestDTO reservationDTO);
    void deleteReservation(String id);
    ReservationResponseDTO updateReservation(String id, UpdateReservationRequestRestDTO reservationDTO);
    String formatReservationID(Date dateIn, Date dateOut, String patientNik, int totalReservations);
    long getDateDifference(Date dateIn, Date dateOut);
    List<ReservationResponseDTO> getReservationsByPatientId(UUID patientId);
    List<ReservationResponseDTO> getReservationsByNurseId(UUID nurseId);
    List<Room> findAvailableRoomByDate(Date dateIn, Date dateOut);
    List<FacilityResponseDTO> getFacilities();

    
} 