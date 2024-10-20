package apap.ti.hospitalization2206829225.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(nullable = false)
    private Date dateIn;

    @Column(nullable = false)
    private Date dateOut;

    @Column(nullable = false)
    private double totalFee;

    @Column(name = "patientId", nullable = false)
    private UUID patientId;

    @Column(name = "assignedNurse", nullable = false)
    private UUID assignedNurse;
    
    @Column(name = "roomId", nullable = false)
    private String roomId;

    @ManyToMany
    @JoinTable(
        name = "reservation_facility",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private List<Facility> facilities;

    private Date createdAt;
    private Date updatedAt;

    private Boolean isDeleted = false;
}
