package apap.ti.hospitalization2206829225.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.UUID;

import apap.ti.hospitalization2206829225.model.Patient;
import apap.ti.hospitalization2206829225.model.Reservation;
import apap.ti.hospitalization2206829225.repository.ReservationDb;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDb reservationDb;

    @Autowired
    private PatientService patientService;
    

    @Override
    public int getTotalReservations() {
        return (int) reservationDb.count();
    }




    @Override
    public String generateReservationId(LocalDate dateIn, LocalDate dateOut, UUID patientId) {
        StringBuilder idBuilder = new StringBuilder("RES");

        long dateDifference = java.time.temporal.ChronoUnit.DAYS.between(dateIn, dateOut);
        String dateDiffString = String.format("%02d", Math.min(Math.max(dateDifference, 0), 99));
        idBuilder.append(dateDiffString);

        LocalDate today = LocalDate.now();
        String dayOfWeek = today.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
        idBuilder.append(dayOfWeek);


        Patient patient = patientService.getPatientByID(patientId);
        String patientNik = patient.getNIK();
        String lastFourNik = patientNik.substring(patientNik.length() - 4);
        idBuilder.append(lastFourNik);

        long reservationCount = reservationDb.count() + 1; // Menambahkan 1 agar ID dimulai dari 0001
        String reservationCountString = String.format("%04d", reservationCount);
        idBuilder.append(reservationCountString);

        return idBuilder.toString();
    }


    @Override
    public Reservation addReservation(Reservation reservation) {
        return reservationDb.save(reservation);
    }
    
    
}
