package apap.ti.hospitalization2206829225.DTO;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@NoArgsConstructor
@AllArgsConstructor

public class AddPatientDTO {
    private String nama;
    private String email;
    private String NIK;
    private boolean gender;   

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

}
