package apap.ti.hospitalization2206829225.service;

import java.util.UUID;

import apap.ti.hospitalization2206829225.model.Patient;


// import apap.ti.hospitalization2206829225.model.Patient;

public interface PatientService {
    int getTotalPatients();
    Patient getPatientByNik (String NIK);
    Patient addNewPatient(Patient patient);
    Patient getPatientByID (UUID patientId);

}
