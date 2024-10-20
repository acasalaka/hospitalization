package apap.ti.hospitalization2206829225.service;


import java.util.Date;
import java.util.List;

// import apap.ti.hospitalization2206829225.model.Reservation;
import apap.ti.hospitalization2206829225.model.Room;

public interface RoomService {
    int getTotalRooms();
    List<Room> getAllRoom();
    String generateRoomId();
    Room saveRoom(Room room);
    Room getRoomByID (String roomId);
    Room updateRoom(Room room);
    // List<Reservation> getFilteredReservationsByRoomId(String roomId, Date dateIn, Date dateOut);
    void deleteRoomById(Room room);
    // List<Room> findAllByIsDeletedFalse();
    // List<Room> findAllAvailableRoom(Date dateIn, Date dateOut);
    List<Room> getAvailableRooms(Date dateIn, Date dateOut);
    

}
