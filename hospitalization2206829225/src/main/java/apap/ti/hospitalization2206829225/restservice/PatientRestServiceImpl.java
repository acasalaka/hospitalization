package apap.ti.hospitalization2206829225.restservice;

import java.util.List;
import java.util.UUID;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import apap.ti.hospitalization2206829225.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206829225.restdto.response.PatientResponseDTO;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PatientRestServiceImpl implements PatientRestService {
    private final WebClient webClient;

    public PatientRestServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8084").build();
    }
    @Override
    public List<PatientResponseDTO> getAllPatientFromRest() {
        var response = webClient.get().uri("/api/patient/viewall").retrieve().bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<PatientResponseDTO>>>() {}).block();

        if (response == null) {
            return null;
        }

        if (response.getStatus() != 200) {
            return null;
        }

        return response.getData();
    }

    @Override
    public PatientResponseDTO getPatientByNIKFromRest(String nik) {
        var response = webClient.get().uri("/api/patient/" + nik).retrieve().bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<PatientResponseDTO>>() {}).block();

        if (response == null) {
            return null;
        }

        if (response.getStatus() != 200) {
            return null;
        }

        return response.getData();
    }
    @Override
    public PatientResponseDTO getPatientByIdFromRest(UUID id) {
        var response = webClient.get().uri("/api/patient/detail/" + id ).retrieve().bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<PatientResponseDTO>>() {}).block();

        if (response == null) {
            return null;
        }

        if (response.getStatus() != 200) {
            return null;
        }

        return response.getData();
    }

    
}
