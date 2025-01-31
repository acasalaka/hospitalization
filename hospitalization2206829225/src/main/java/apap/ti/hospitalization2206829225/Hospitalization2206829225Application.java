package apap.ti.hospitalization2206829225;

import apap.ti.hospitalization2206829225.model.Facility;
import apap.ti.hospitalization2206829225.model.Nurse;
import apap.ti.hospitalization2206829225.model.Patient;
import apap.ti.hospitalization2206829225.model.Reservation;
import apap.ti.hospitalization2206829225.model.Room;
import apap.ti.hospitalization2206829225.service.FacilityService;
import apap.ti.hospitalization2206829225.service.NurseService;
import apap.ti.hospitalization2206829225.service.PatientService;
import apap.ti.hospitalization2206829225.service.ReservationService;
import apap.ti.hospitalization2206829225.service.RoomService;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.Date;

@SpringBootApplication
public class Hospitalization2206829225Application {

    public static void main(String[] args) {
        SpringApplication.run(Hospitalization2206829225Application.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner run(NurseService nurseService, FacilityService facilityService, RoomService roomService, PatientService patientService, ReservationService reservationService) {
        return args -> {
            var faker = new Faker(new Locale("in-ID"));

            // Generate Nurses
            List<Nurse> nurseList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                var nurse = new Nurse();
                nurse.setName(faker.name().fullName());
                nurse.setEmail(faker.internet().emailAddress());
                nurse.setGender(faker.bool().bool());
                nurse.setCreatedAt(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                nurse.setUpdatedAt(faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());

                nurseService.addNurse(nurse);
                nurseList.add(nurse);
            }

            // Generate Facilities
            List<Facility> facilityList = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                var facility = new Facility();
                facility.setName(faker.commerce().productName());
                facility.setFee(faker.number().randomDouble(2, 10000, 500000));
                facility.setCreatedAt(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                facility.setUpdatedAt(faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());

                facilityService.addFacility(facility);
                facilityList.add(facility);
            }

            // Generate Rooms
            List<Room> roomList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                var room = new Room();
                room.setId("RM" + (i + 1));
                room.setName("Room " + (i + 1));
                room.setDescription(faker.lorem().sentence(10));
                room.setMaxCapacity(faker.number().numberBetween(1, 5));
                room.setPricePerDay(faker.number().randomDouble(2, 200000, 1000000));
                room.setCreatedAt(Date.from(faker.date().past(365, TimeUnit.DAYS).toInstant()));
                room.setUpdatedAt(Date.from(faker.date().past(30, TimeUnit.DAYS).toInstant()));

                roomService.saveRoom(room);
                roomList.add(room);
            }

            // Generate Patients
            List<Patient> patientList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                var patient = new Patient();
                patient.setNIK(faker.number().digits(16));
                patient.setName(faker.name().fullName());
                patient.setEmail(faker.internet().emailAddress());
                patient.setGender(faker.bool().bool());
                patient.setBirthDate(faker.date().birthday(18, 80));
                patient.setCreatedDate(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                patient.setUpdatedDate(faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());

                patientService.addNewPatient(patient);
                patientList.add(patient);
            }

            // Generate Reservations
            for (int i = 0; i < 3; i++) {
                var reservation = new Reservation();
                var dateIn = faker.date().future(5, TimeUnit.DAYS);
                var dateOut = faker.date().future(20, TimeUnit.DAYS, dateIn);

                var patient = patientList.get(faker.number().numberBetween(0, patientList.size()));
                var room = roomList.get(faker.number().numberBetween(0, roomList.size()));
                var nurse = nurseList.get(faker.number().numberBetween(0, nurseList.size()));


                int totalReservations = reservationService.getTotalReservations(); 
                String reservationId = formatReservationID(dateIn, dateOut, patient.getNIK(), totalReservations + 1);
                reservation.setId(reservationId);

                reservation.setDateIn(dateIn);
                reservation.setDateOut(dateOut);
                reservation.setPatientId(patient.getId());
                reservation.setAssignedNurse(nurse.getId());
                reservation.setRoomId(room.getId());

                long duration = TimeUnit.DAYS.convert(dateOut.getTime() - dateIn.getTime(), TimeUnit.MILLISECONDS);
                reservation.setTotalFee(room.getPricePerDay() * duration);

                Collections.shuffle(facilityList);
                List<Facility> randomFacilities = facilityList.subList(0, faker.number().numberBetween(1, 3));
                reservation.setFacilities(randomFacilities);

                reservation.setCreatedAt(Date.from(faker.date().past(30, TimeUnit.DAYS).toInstant()));
                reservation.setUpdatedAt(Date.from(faker.date().past(10, TimeUnit.DAYS).toInstant()));

                reservationService.addReservation(reservation);
            }

            System.out.println("Data created: 5 Rooms, 5 Patients, 5 Nurses, 7 Facilities, and 3 Reservations.");
        };
    }

    public String formatReservationID(Date dateIn, Date dateOut, String patientNik, int totalReservations) {
        StringBuilder reservationID = new StringBuilder("RES");

        long dateDifference = getDateDifference(dateIn, dateOut);
        String dateDiffStr = String.format("%02d", dateDifference % 100);
        reservationID.append(dateDiffStr);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);
        String dayOfWeek = dayFormat.format(new Date()).toUpperCase();
        reservationID.append(dayOfWeek);

        String nikSuffix = patientNik.substring(patientNik.length() - 4);
        reservationID.append(nikSuffix);

        String totalResStr = String.format("%04d", totalReservations);
        reservationID.append(totalResStr);

        return reservationID.toString();
    }

    public long getDateDifference(Date dateIn, Date dateOut) {
        long diffInMillis = dateOut.getTime() - dateIn.getTime();
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1;
    }
}
