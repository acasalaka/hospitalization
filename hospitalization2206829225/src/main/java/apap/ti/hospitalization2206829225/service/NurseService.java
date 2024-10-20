package apap.ti.hospitalization2206829225.service;

import apap.ti.hospitalization2206829225.model.Nurse;
import java.util.List;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface NurseService {
    void addNurse(Nurse nurse);
    List<Nurse> getAllNurses();
    Nurse getNurseById(UUID id);
} 
