package apap.ti.hospitalization2206829225.controller;

import apap.ti.hospitalization2206829225.restdto.response.ReservationResponseDTO;
import apap.ti.hospitalization2206829225.service.ReservationService;
import apap.ti.hospitalization2206829225.model.Reservation;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ReservationController.class) 
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllReservations() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId("RES001");
        reservation.setRoomId("RM3");
        reservation.setTotalFee(1000000.0);
        reservation.setAssignedNurse(UUID.fromString("e64fc207-19c4-4b79-8dda-a417595f657f"));
        reservation.setDateIn(new Date());
        reservation.setDateOut(new Date());
        
        List<Reservation> reservationList = Collections.singletonList(reservation);

        when(reservationService.getAllReservation()).thenReturn(reservationList);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservations")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.message").value("List of reservations found successfully"))
            .andExpect(jsonPath("$.data[0].id").value("RES001"))
            .andExpect(jsonPath("$.data[0].roomId").value("RM3"));
    }

    @Test
    public void testGetReservationById_found() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId("RES001");
        reservation.setRoomId("RM3");
        reservation.setTotalFee(1000000.0);
        reservation.setAssignedNurse(UUID.fromString("e64fc207-19c4-4b79-8dda-a417595f657f"));
        reservation.setDateIn(new Date());
        reservation.setDateOut(new Date());

        when(reservationService.getReservationById("RES001")).thenReturn(reservation);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservations/RES001")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.message").value("Reservation with ID RES001 found successfully"))
            .andExpect(jsonPath("$.data.id").value("RES001"))
            .andExpect(jsonPath("$.data.roomId").value("RM3"));
    }


}
