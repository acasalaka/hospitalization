package apap.ti.hospitalization2206829225.restdto.response;

import java.util.Date;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientResponseDTO {
    private UUID id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;
    private boolean isDeleted;
    private String nik;
    private String birthPlace;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private int pClass;
}
