package apap.ti.hospitalization2206829225.restcontroller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti.hospitalization2206829225.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206829225.restdto.response.ReservationResponseDTO;
import apap.ti.hospitalization2206829225.restservice.ReservationRestService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/reservations")
public class ReservationRestController {
    @Autowired
    private ReservationRestService reservationService;

    @GetMapping
    public ResponseEntity<?> getAllReservations() {

        var baseResponseDTO = new BaseResponseDTO<List<ReservationResponseDTO>>();
        List<ReservationResponseDTO> reservationList = reservationService.getAllReservation();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(reservationList);
        baseResponseDTO.setMessage("List of reservations found successfully");
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
}
