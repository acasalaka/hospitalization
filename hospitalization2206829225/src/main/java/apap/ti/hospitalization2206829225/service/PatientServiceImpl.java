package apap.ti.hospitalization2206829225.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import apap.ti.hospitalization2206829225.model.Patient;
import apap.ti.hospitalization2206829225.repository.PatientDb;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientDb patientDb;

    @Override
    public int getTotalPatients(){
        return (int) patientDb.count();
    }

    @Override
    public Patient getPatientByNik(String NIK) {
        return patientDb.findByNIK(NIK);
    }



    @Override
    public Patient addNewPatient(Patient patient) {
        return patientDb.save(patient);
    }

    @Override
    public Patient getPatientByID(UUID patientId) {
        return patientDb.findById(patientId).orElse(null);
    }



    

}
