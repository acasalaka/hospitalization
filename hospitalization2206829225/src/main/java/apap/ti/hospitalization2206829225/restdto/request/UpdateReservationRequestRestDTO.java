package apap.ti.hospitalization2206829225.restdto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateReservationRequestRestDTO extends AddReservationRequestRestDTO{
    @NotNull(message = "Date in tidak boleh kosong")
    private Date dateIn;

    @NotNull(message = "Date out tidak boleh kosong")
    private Date dateOut;

    @NotNull(message = "ID Ruangan tidak boleh kosong")
    private String roomId;

    
}
