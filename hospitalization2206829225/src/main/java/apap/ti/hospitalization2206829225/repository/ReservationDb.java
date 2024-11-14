package apap.ti.hospitalization2206829225.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import apap.ti.hospitalization2206829225.model.Reservation;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface ReservationDb extends JpaRepository<Reservation, String>  {
    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND " +
           "((r.dateIn BETWEEN :dateIn AND :dateOut) OR (r.dateOut BETWEEN :dateIn AND :dateOut) OR " +
           "(r.dateIn <= :dateIn AND r.dateOut >= :dateOut))")
    List<Reservation> findByRoomIdAndDateRange(@Param("roomId") String roomId,
                                               @Param("dateIn") Date dateIn,
                                               @Param("dateOut") Date dateOut);

       List<Reservation> findByPatientId(UUID patientId);
       List<Reservation> findByAssignedNurse(UUID nurseId);

       @Query("SELECT r FROM Reservation r WHERE r.isDeleted = false")
       List<Reservation> findAllActiveReservations();
       

       
}
