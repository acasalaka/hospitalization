package apap.ti.hospitalization2206829225.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import apap.ti.hospitalization2206829225.model.Room;

public interface RoomDb extends JpaRepository <Room, String>{
    // @Query("SELECT COUNT(*) FROM Room")
    // long countActiveRooms();
}
