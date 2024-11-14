package apap.ti.hospitalization2206829225.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "room")
@SQLDelete(sql = "UPDATE room SET deleted_at = NOW() WHERE id=?")
@SQLRestriction("deleted_at IS NULL")
public class Room {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "nama", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "maxCapacity", nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private double pricePerDay;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @OneToMany(mappedBy = "roomId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @SQLRestriction("is_deleted=FALSE")
    private List<Reservation> reservations;

    @Column(name = "is_deleted")
    private boolean isDeleted;

}
