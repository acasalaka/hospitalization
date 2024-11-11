package apap.ti.hospitalization2206829225.DTO;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShowPatientDTO extends AddPatientDTO {
    private UUID id;

    
}
