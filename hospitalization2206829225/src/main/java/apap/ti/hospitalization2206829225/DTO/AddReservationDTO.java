package apap.ti.hospitalization2206829225.DTO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import apap.ti.hospitalization2206829225.model.Facility;
// import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class AddReservationDTO {
    private String roomId;
    private UUID assignedNurseId;
    private String roomName;
    private UUID patientId;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateIn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOut;

    private List<Facility> listFacilities;
    
    private double totalFee;
}
