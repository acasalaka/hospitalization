package apap.ti.hospitalization2206829225.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class searchDTO {
    @NotBlank(message = "NIK harus diisi")
    private String nik;
}
