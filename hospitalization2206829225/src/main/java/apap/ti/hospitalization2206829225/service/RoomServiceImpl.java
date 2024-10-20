package apap.ti.hospitalization2206829225.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206829225.model.Reservation;
import apap.ti.hospitalization2206829225.model.Room;
import apap.ti.hospitalization2206829225.repository.ReservationDb;
import apap.ti.hospitalization2206829225.repository.RoomDb;


@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomDb roomDb;

    @Autowired
    private ReservationDb reservationDb;
    @Override
    public int getTotalRooms() {
        return (int) roomDb.count();
    }
    @Override
    public List<Room> getAllRoom() {
        return roomDb.findAll();
    }
    @Override
    public Room saveRoom(Room room) {
       return roomDb.save(room);
    }
    // @Override
    // public String generateRoomId() {
    //     int roomCount = (int) roomDb.count() + 1;
    //     return String.format("RM%04d", roomCount);
    // }
    @Override
    public Room getRoomByID(String roomId) {
        return roomDb.findById(roomId).orElse(null);
    }
    @Override
    public String generateRoomId() {
        List<Room> rooms = roomDb.findAll();
        int maxId = rooms.stream()
                        .map(room -> room.getId().substring(2))
                        .mapToInt(Integer::parseInt)
                        .max()
                        .orElse(0); 
        int nextIdValue = maxId + 1;
        return String.format("RM%04d", nextIdValue);
    }

    // @Override
    // public String generateRoomId() {
    //     long activeRoomCount = roomDb.countActiveRooms();
    //     long nextIdValue = activeRoomCount + 1;
    //     return String.format("RM%04d", nextIdValue);
    // }



    @Override
    public Room updateRoom(Room room) {
        Room getRoom = getRoomByID(room.getId());
        if(getRoom != null){
            getRoom.setId(room.getId());
            getRoom.setDescription(room.getDescription());
            getRoom.setMaxCapacity(room.getMaxCapacity());
            getRoom.setName(room.getName());
            getRoom.setPricePerDay(room.getPricePerDay());
            roomDb.save(getRoom);

            return getRoom;
        }
        return null;
    }

    @Override
    public void deleteRoomById(Room room) {
       roomDb.delete(room);
    }

    
    // @Override
    // public List<Room> findAllAvailableRoom(Date dateIn, Date dateOut) {
    //     List<Room> allRooms = roomDb.findAll(); // Mendapatkan semua ruangan
    //     List<Room> availableRooms = new ArrayList<>();
        
    //     for (Room room : allRooms) {
    //         List<Reservation> reservations = reservationDb.findReservationsWithinDateRange(room.getId(), dateIn, dateOut);

    //         // Cek apakah jumlah reservasi melebihi kapasitas
    //         int totalPatients = reservations.size();
            
    //         if (totalPatients < room.getMaxCapacity()) {
    //             availableRooms.add(room);
    //         }
    //     }

    //     return availableRooms;
    // }


    
    
}
