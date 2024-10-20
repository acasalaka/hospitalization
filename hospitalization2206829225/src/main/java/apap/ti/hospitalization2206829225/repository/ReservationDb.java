package apap.ti.hospitalization2206829225.repository;


import org.springframework.data.jpa.repository.JpaRepository;


import apap.ti.hospitalization2206829225.model.Reservation;

public interface ReservationDb extends JpaRepository<Reservation, String>  {

}
