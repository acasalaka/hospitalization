package apap.ti.hospitalization2206829225.DTO;

// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRoomDTO {
    private String name;

    private int maxCapacity;

    private double pricePerDay;
    
    private String description;
}
