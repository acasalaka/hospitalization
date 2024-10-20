package apap.ti.hospitalization2206829225.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import apap.ti.hospitalization2206829225.model.Facility;

public interface FacilityDb extends JpaRepository <Facility, UUID> {

}
