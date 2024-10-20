package apap.ti.hospitalization2206829225.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "facility")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nama", nullable = false)
    private String name;

    @Column(name = "fee", nullable = false)
    private double fee;

    @ManyToMany(mappedBy = "facilities")
    private List<Reservation> reservations;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(name = "isDeleted")
    private Boolean isDeleted = false;
}
