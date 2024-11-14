package apap.ti.hospitalization2206829225.restdto.response;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationResponseDTO {
    private String id;
    private UUID patientId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateIn;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOut;
    
    private double totalFee;
    private UUID assignedNurse;
    private String roomId;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ReservationResponseDTO> listReservation;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date updatedAt;

    
}
