package apap.ti.hospitalization2206829225.restservice;

import java.util.List;
import java.util.UUID;
import java.util.Date;

import apap.ti.hospitalization2206829225.restdto.request.AddReservationRequestRestDTO;
import apap.ti.hospitalization2206829225.restdto.request.UpdateReservationRequestRestDTO;
import apap.ti.hospitalization2206829225.restdto.response.ReservationResponseDTO;

public interface ReservationRestService {
    List<ReservationResponseDTO> getAllReservation();
    // List<ReservationResponseDTO> getReservationByPatientId(UUID patientId);
    // List<ReservationResponseDTO> getReservationByNurseId(UUID nurseId);
    ReservationResponseDTO getReservationById(String id);
    ReservationResponseDTO addReservation(AddReservationRequestRestDTO reservationDTO);
    void deleteReservation(String id);
    ReservationResponseDTO updateReservation(String id, UpdateReservationRequestRestDTO reservationDTO);
    ReservationResponseDTO updateFacilities(String reservationId, List<UUID> listOfFacilities);
    String formatReservationID(Date dateIn, Date dateOut, String patientNik, int totalReservations);
    long getDateDifference(Date dateIn, Date dateOut);
    List<ReservationResponseDTO> getReservationsByPatientId(UUID patientId);
    List<ReservationResponseDTO> getReservationsByNurseId(UUID nurseId);


    
} 