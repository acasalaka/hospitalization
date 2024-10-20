package apap.ti.hospitalization2206829225;

import apap.ti.hospitalization2206829225.model.Facility;
import apap.ti.hospitalization2206829225.model.Nurse;
import apap.ti.hospitalization2206829225.service.FacilityService;
import apap.ti.hospitalization2206829225.service.NurseService;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Locale;




@SpringBootApplication
public class Hospitalization2206829225Application {

	public static void main(String[] args) {
		SpringApplication.run(Hospitalization2206829225Application.class, args);
	}

	@Bean
    @Transactional
    CommandLineRunner run(NurseService nurseService, FacilityService facilityService) {
        return args -> {
            var faker = new Faker(new Locale("in-ID"));

            // Generate 5 Nurses
            for (int i = 0; i < 5; i++) {
                var nurse = new Nurse();
                var fakeName = faker.name();
                var fakeEmail = faker.internet();
                var fakeGender = faker.bool();

                nurse.setName(fakeName.fullName());
                nurse.setEmail(fakeEmail.emailAddress());
                nurse.setGender(fakeGender.bool());
                nurse.setCreatedAt(faker.date().past(365, java.util.concurrent.TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                nurse.setUpdatedAt(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());

                // Simpan Nurse ke database
                nurseService.addNurse(nurse);
            }

            // Generate 7 Facilities
            for (int i = 0; i < 7; i++) {
                var facility = new Facility();
                var fakeFacilityName = faker.commerce().productName();  // Menggunakan nama produk dari faker.commerce agar tidak berupa nama orang
                var fakeFee = faker.number().randomDouble(2, 10000, 500000);

                facility.setName(fakeFacilityName);
                facility.setFee(fakeFee);
                facility.setCreatedAt(faker.date().past(365, java.util.concurrent.TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                facility.setUpdatedAt(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());

                // Simpan Facility ke database
                facilityService.addFacility(facility);
            }

            System.out.println("5 Nurses and 7 Facilities created with fake data.");
        };
    }
}
