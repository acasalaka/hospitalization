package apap.ti.hospitalization2206829225.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import apap.ti.hospitalization2206829225.model.Nurse;
public interface NurseDb extends JpaRepository <Nurse, UUID>{

    
} 