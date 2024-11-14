package apap.ti.hospitalization2206829225.restcontroller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti.hospitalization2206829225.restdto.request.AddReservationRequestRestDTO;
import apap.ti.hospitalization2206829225.restdto.request.UpdateReservationRequestRestDTO;
import apap.ti.hospitalization2206829225.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206829225.restdto.response.ReservationResponseDTO;
import apap.ti.hospitalization2206829225.restservice.ReservationRestService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/reservations")
public class ReservationRestController {
    @Autowired
    private ReservationRestService reservationService;

    @Autowired
    private ReservationRestService reservationRestService;

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

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getReservationsByPatient(@PathVariable("patientId") UUID patientId) {
        var baseResponseDTO = new BaseResponseDTO<List<ReservationResponseDTO>>();
        List<ReservationResponseDTO> reservationList = reservationService.getReservationsByPatientId(patientId);

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(reservationList);
        baseResponseDTO.setMessage("List of reservations for patient found successfully");
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/nurse/{nurseId}")
    public ResponseEntity<?> getReservationsByNurse(@PathVariable("nurseId") UUID nurseId) {
        var baseResponseDTO = new BaseResponseDTO<List<ReservationResponseDTO>>();
        List<ReservationResponseDTO> reservationList = reservationService.getReservationsByNurseId(nurseId);

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(reservationList);
        baseResponseDTO.setMessage("List of reservations assigned to nurse found successfully");
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<BaseResponseDTO> deleteReservation(@PathVariable("id") String id) {
        ReservationResponseDTO reservationResponse = reservationRestService.getReservationById(id);

        if (reservationResponse != null) {
            reservationRestService.deleteReservation(id);
            var response = new BaseResponseDTO<>();
            response.setStatus(200);
            response.setMessage("Reservation with ID " + id + " has been deleted successfully");
            response.setTimestamp(new Date());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            var response = new BaseResponseDTO<>();
            response.setStatus(404);
            response.setMessage("Reservation data not found");
            response.setTimestamp(new Date());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable("id") String id) {
        var baseResponseDTO = new BaseResponseDTO<ReservationResponseDTO>();
        ReservationResponseDTO reservationResponse = reservationService.getReservationById(id);

        if (reservationResponse != null) {
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(reservationResponse);
            baseResponseDTO.setMessage("Reservation with ID " + id + " found successfully");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } else {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setData(null);
            baseResponseDTO.setMessage("Reservation with ID " + id + " not found");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReservation(@RequestBody AddReservationRequestRestDTO reservationDTO) {
        try{
            ReservationResponseDTO reservationResponse = reservationService.addReservation(reservationDTO);
            var baseResponseDTO = new BaseResponseDTO<ReservationResponseDTO>();
            baseResponseDTO.setStatus(HttpStatus.CREATED.value());
            baseResponseDTO.setMessage("Reservation added successfully");
            baseResponseDTO.setData(reservationResponse);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            var baseResponseDTO = new BaseResponseDTO<>();
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage("Failed to add reservation");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateReservation( @PathVariable("id") String id, @RequestBody UpdateReservationRequestRestDTO reservationDTO) {
        var baseResponseDTO = new BaseResponseDTO<>();

        try {
            ReservationResponseDTO reservationResponse = reservationService.updateReservation(id, reservationDTO);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Reservation updated successfully");
            baseResponseDTO.setData(reservationResponse);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Reservation not found");
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);

        } catch (IllegalStateException e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Failed to update reservation");
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @PutMapping("/{id}/update-facilities")
    // public ResponseEntity<?> updateFacilities(
    //         @PathVariable("id") String id,
    //         @RequestBody List<UUID> listOfFacilities) {
    //     var baseResponseDTO = new BaseResponseDTO<>();

    //     try {
    //         ReservationResponseDTO reservationResponse = reservationService.updateFacilities(id, listOfFacilities);
    //         baseResponseDTO.setStatus(HttpStatus.OK.value());
    //         baseResponseDTO.setMessage("Facilities updated successfully");
    //         baseResponseDTO.setData(reservationResponse);
    //         baseResponseDTO.setTimestamp(new Date());
    //         return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);

    //     } catch (IllegalArgumentException e) {
    //         baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
    //         baseResponseDTO.setMessage("Reservation not found");
    //         baseResponseDTO.setData(null);
    //         baseResponseDTO.setTimestamp(new Date());
    //         return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);

    //     } catch (IllegalStateException e) {
    //         baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
    //         baseResponseDTO.setMessage(e.getMessage());
    //         baseResponseDTO.setData(null);
    //         baseResponseDTO.setTimestamp(new Date());
    //         return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
    //     } catch (Exception e) {
    //         baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    //         baseResponseDTO.setMessage("Failed to update facilities");
    //         baseResponseDTO.setData(null);
    //         baseResponseDTO.setTimestamp(new Date());
    //         return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }


}