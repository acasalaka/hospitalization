package apap.ti.hospitalization2206829225.service;

import java.util.List;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206829225.model.Nurse;
import apap.ti.hospitalization2206829225.repository.NurseDb;

@Service
public class NurseServiceImpl implements NurseService{

    @Autowired
    private NurseDb nurseDb;
    @Override
    public void addNurse(Nurse nurse) {
        nurseDb.save(nurse);
    }

    @Override
    public List<Nurse> getAllNurses() {
       return nurseDb.findAll();
    }

    @Override
    public Nurse getNurseById(UUID id) {
       return nurseDb.findById(id).orElse(null);
    }
    
}
