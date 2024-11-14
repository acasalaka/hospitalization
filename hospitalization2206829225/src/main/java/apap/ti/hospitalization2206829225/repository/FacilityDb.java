package apap.ti.hospitalization2206829225.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import apap.ti.hospitalization2206829225.model.Facility;
import java.util.List;
import apap.ti.hospitalization2206829225.model.Reservation;


public interface FacilityDb extends JpaRepository <Facility, UUID> {

    List<Facility> findByReservations(List<Reservation> reservations);
    List<Facility> findAll(); 
    Facility findByIdAndIsDeletedFalse(UUID id);
    List<Facility> findByIsDeletedFalse();
}
