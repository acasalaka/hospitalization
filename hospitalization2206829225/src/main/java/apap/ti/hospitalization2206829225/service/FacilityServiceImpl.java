package apap.ti.hospitalization2206829225.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206829225.model.Facility;
import apap.ti.hospitalization2206829225.repository.FacilityDb;

@Service
public class FacilityServiceImpl implements FacilityService {
    @Autowired
    private FacilityDb facilityDb;

    @Override
    public Facility addFacility(Facility facility) {
       return facilityDb.save(facility);
    }

    @Override
    public List<Facility> getAllFacilities() {
        return facilityDb.findAll();
    }

    @Override
    public List<Facility> getSelectedFacilitiesByIds(List<UUID> facilityIds) {
        return facilityDb.findAllById(facilityIds);
    }

    
}
    