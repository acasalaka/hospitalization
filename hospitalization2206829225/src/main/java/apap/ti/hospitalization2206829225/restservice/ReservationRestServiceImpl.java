package apap.ti.hospitalization2206829225.restservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206829225.model.Reservation;
import apap.ti.hospitalization2206829225.repository.ReservationDb;
import apap.ti.hospitalization2206829225.restdto.response.ReservationResponseDTO;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class ReservationRestServiceImpl implements ReservationRestService {

    @Autowired
    ReservationDb reservationDb;
    @Override
    public List<ReservationResponseDTO> getAllReservation() {
        var listReservation = reservationDb.findAll();
        var listReservationResponseDTO = new ArrayList<ReservationResponseDTO>();
        listReservation.forEach(reservation -> {
            var reservationResponseDTO =reservationToReservationResponseDTO(reservation);
            listReservationResponseDTO.add(reservationResponseDTO);
        });

        return listReservationResponseDTO;
    }
    
     private ReservationResponseDTO reservationToReservationResponseDTO(Reservation reservation) {
        var reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(reservation.getId());
        reservationResponseDTO.setPatientId(reservation.getPatientId());
        reservationResponseDTO.setDateIn(reservation.getDateIn());
        reservationResponseDTO.setDateOut(reservation.getDateOut());
        reservationResponseDTO.setTotalFee(reservation.getTotalFee());
        reservationResponseDTO.setAssignedNurse(reservation.getAssignedNurse());
        reservationResponseDTO.setRoomId(reservation.getRoomId());
        reservationResponseDTO.setCreatedAt(new java.sql.Timestamp(reservation.getCreatedAt().getTime()));
        reservationResponseDTO.setUpdatedAt(new java.sql.Timestamp(reservation.getUpdatedAt().getTime()));

        return reservationResponseDTO;
    }
} 
