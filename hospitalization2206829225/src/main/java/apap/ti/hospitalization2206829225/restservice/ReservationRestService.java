package apap.ti.hospitalization2206829225.restservice;

import java.util.List;

import apap.ti.hospitalization2206829225.restdto.response.ReservationResponseDTO;

public interface ReservationRestService {
    List<ReservationResponseDTO> getAllReservation();
    
} 