package apap.ti.hospitalization2206829225.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import apap.ti.hospitalization2206829225.model.Patient;
import apap.ti.hospitalization2206829225.model.Reservation;
import apap.ti.hospitalization2206829225.repository.ReservationDb;
import apap.ti.hospitalization2206829225.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206829225.restdto.response.ReservationResponseDTO;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDb reservationDb;

    @Autowired
    private PatientService patientService;

    private final WebClient webClient;

    public ReservationServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl("http://localhost:8080/api")
            .build();
    }
    

    @Override
    public int getTotalReservations() {
        return (int) reservationDb.count();
    }

    @Override
    public List<ReservationResponseDTO> getAllReservationFromRest() throws Exception {
        var response = webClient
            .get()
            .uri("/pekerja/viewall")
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<ReservationResponseDTO>>>() {})
            .block();

        if (response == null) {
            throw new Exception("Failed consume API getAllPekerja");
        }

        if (response.getStatus() != 200) {
            throw new Exception(response.getMessage());
        }

        return response.getData();
    }



    @Override
    public String generateReservationId(Date dateIn, Date dateOut, UUID patientId) {
        StringBuilder idBuilder = new StringBuilder("RES");

        long dateDifference = ChronoUnit.DAYS.between(
                dateIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                dateOut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        
        String dateDiffString = String.format("%02d", Math.min(Math.max(dateDifference, 0), 99));
        idBuilder.append(dateDiffString);

        LocalDate today = LocalDate.now();
        String dayOfWeek = today.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
        idBuilder.append(dayOfWeek);

        Patient patient = patientService.getPatientByID(patientId);
        String patientNik = patient.getNIK();
        String lastFourNik = patientNik.substring(patientNik.length() - 4);
        idBuilder.append(lastFourNik);

        long reservationCount = reservationDb.count() + 1; 
        String reservationCountString = String.format("%04d", reservationCount);
        idBuilder.append(reservationCountString);

        return idBuilder.toString();
    }



    @Override
    public Reservation addReservation(Reservation reservation) {
        return reservationDb.save(reservation);
    }




	@Override
	public List<Reservation> getAllReservation() {
		return reservationDb.findAll();
	}


    @Override
    public Reservation getReservationById(String id) {
        return reservationDb.findById(id).orElse(null);
    }


    @Override
    public void deleteReservation(String id) {
        Reservation reservation = getReservationById(id);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found.");
        }
        reservation.setIsDeleted(true);
        reservation.setUpdatedAt(new Date());
        reservationDb.save(reservation);
    }
    
    
}
