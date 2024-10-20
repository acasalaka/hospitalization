package apap.ti.hospitalization2206829225.repository;

import apap.ti.hospitalization2206829225.model.Patient;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PatientDb extends JpaRepository<Patient, UUID> {
    Patient findByNIK(String NIK);
    
}

