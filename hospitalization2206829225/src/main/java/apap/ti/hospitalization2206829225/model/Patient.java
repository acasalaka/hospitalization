package apap.ti.hospitalization2206829225.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "NIK", unique = true, nullable = false)
    private String NIK;

    @Column(name = "nama", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "gender",nullable = false)
    private Boolean gender; 

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", updatable = false, nullable = false)
    private LocalDateTime updatedDate;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

}
