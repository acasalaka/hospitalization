package apap.ti.hospitalization2206829225.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date dateIn;

    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @JoinTable(name = "reservation_facility",joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    @SQLRestriction("is_deleted=FALSE")
    private List<Facility> facilities;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
