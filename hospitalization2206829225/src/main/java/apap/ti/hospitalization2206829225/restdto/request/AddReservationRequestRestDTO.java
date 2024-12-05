package apap.ti.hospitalization2206829225.restdto.request;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class AddReservationRequestRestDTO{


    private Date dateIn;

    private Date dateOut;

    @NotEmpty(message = "Patient ID tidak boleh kosong")
    private UUID patientId;

    // @NotEmpty(message = "Nurse tidak boleh kosong")
    // private UUID assignedNurse;
    
    @NotEmpty(message = "ID Ruangan tidak boleh kosong")
    private String roomId;

    @NotEmpty(message = "List fasilitas tidak boleh kosong")
    private List<UUID> facilities;

    @Nullable
    private String appointmentId;
    

    private Boolean isDeleted = false;
}