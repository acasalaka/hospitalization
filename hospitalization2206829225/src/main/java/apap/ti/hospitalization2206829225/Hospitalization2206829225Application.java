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

            List<Nurse> nurseList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                var nurse = new Nurse();
                var fakeName = faker.name();
                var fakeEmail = faker.internet();
                var fakeGender = faker.bool();

                nurse.setName(fakeName.fullName());
                nurse.setEmail(fakeEmail.emailAddress());
                nurse.setGender(fakeGender.bool());
                nurse.setCreatedAt(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                nurse.setUpdatedAt(faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());

                nurseService.addNurse(nurse);
                nurseList.add(nurse);
            }

            List<Facility> facilityList = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                var facility = new Facility();
                var fakeFacilityName = faker.commerce().productName();
                var fakeFee = faker.number().randomDouble(2, 10000, 500000);

                facility.setName(fakeFacilityName);
                facility.setFee(fakeFee);
                facility.setCreatedAt(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                facility.setUpdatedAt(faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());

                facilityService.addFacility(facility);
                facilityList.add(facility);
            }

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


            List<Patient> patientList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                var patient = new Patient();
                var fakeName = faker.name();
                var fakeEmail = faker.internet();
                var fakeNIK = faker.number().digits(16);
                var fakeGender = faker.bool();

                patient.setNIK(fakeNIK);
                patient.setName(fakeName.fullName());
                patient.setEmail(fakeEmail.emailAddress());
                patient.setGender(fakeGender.bool());
                patient.setBirthDate(faker.date().birthday(18, 80));
                patient.setCreatedDate(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                patient.setUpdatedDate(faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());

                patientService.addNewPatient(patient);
                patientList.add(patient);
            }
            for (int i = 0; i < 12; i++) {
                var reservation = new Reservation();
                var dateIn = faker.date().future(5, TimeUnit.DAYS);
                var dateOut = faker.date().future(2000, TimeUnit.DAYS, dateIn);

                reservation.setId("RES" + (i + 1));
                reservation.setDateIn(dateIn);
                reservation.setDateOut(dateOut);

                var patient = patientList.get(faker.number().numberBetween(0, patientList.size()));
                var nurse = nurseList.get(faker.number().numberBetween(0, nurseList.size()));

                reservation.setPatientId(patient.getId());
                reservation.setAssignedNurse(nurse.getId());

                var room = roomList.get(faker.number().numberBetween(0, roomList.size()));
                reservation.setRoomId(room.getId());

                long duration = TimeUnit.DAYS.convert(dateOut.getTime() - dateIn.getTime(), TimeUnit.MILLISECONDS);
                double totalFee = room.getPricePerDay() * duration;
                reservation.setTotalFee(totalFee);

                Collections.shuffle(facilityList);
                List<Facility> randomFacilities = facilityList.subList(0, faker.number().numberBetween(1, 3));
                reservation.setFacilities(randomFacilities);

                reservation.setCreatedAt(Date.from(faker.date().past(30, TimeUnit.DAYS).toInstant()));
                reservation.setUpdatedAt(Date.from(faker.date().past(10, TimeUnit.DAYS).toInstant()));

                reservationService.addReservation(reservation);
            }

            System.out.println("Data created: 5 Rooms, 5 Patients, 5 Nurses, 7 Facilities, and 12 Reservations.");
        };
    }
}
