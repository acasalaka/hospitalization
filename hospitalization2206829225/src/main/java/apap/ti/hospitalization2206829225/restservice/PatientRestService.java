package apap.ti.hospitalization2206829225.restservice;

import java.util.List;
import java.util.UUID;

import apap.ti.hospitalization2206829225.restdto.response.PatientResponseDTO;

public interface PatientRestService {
    List<PatientResponseDTO> getAllPatientFromRest();
    PatientResponseDTO getPatientByNIKFromRest(String nik);
    PatientResponseDTO getPatientByIdFromRest (UUID id);
}
