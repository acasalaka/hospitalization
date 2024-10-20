package apap.ti.hospitalization2206829225.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206829225.model.Facility;

@Service
public interface FacilityService {
    Facility addFacility (Facility facility);
    List<Facility> getAllFacilities();
    List<Facility> getSelectedFacilitiesByIds(List<UUID> facilityIds);
    Facility getFacilityById(UUID id);
}
