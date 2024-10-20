package apap.ti.hospitalization2206829225.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "nama", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "maxCapacity", nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private double pricePerDay;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @OneToMany(mappedBy = "roomId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}
