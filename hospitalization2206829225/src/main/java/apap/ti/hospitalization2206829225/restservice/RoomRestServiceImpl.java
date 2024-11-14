package apap.ti.hospitalization2206829225.restservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206829225.model.Reservation;
import apap.ti.hospitalization2206829225.model.Room;
import apap.ti.hospitalization2206829225.repository.ReservationDb;
import apap.ti.hospitalization2206829225.repository.RoomDb;
import apap.ti.hospitalization2206829225.restdto.response.RoomResponseDTO;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoomRestServiceImpl implements RoomRestService {

    @Autowired
    private RoomDb roomDb;

    @Autowired
    private ReservationDb reservationDb;
    @Override
    public List<RoomResponseDTO> getAllRoom() {
        var listRoom = roomDb.findAll();
        var listRoomResponseDTO = new ArrayList<RoomResponseDTO>();
        listRoom.forEach(room -> {
            var roomResponseDTO = roomToRoomResponseDTO(room);
            listRoomResponseDTO.add(roomResponseDTO);
        });
        return listRoomResponseDTO;
    }

    private boolean datesOverlap(Date existingDateIn, Date existingDateOut, Date newDateIn, Date newDateOut) {
        return !existingDateOut.before(newDateIn) && !existingDateIn.after(newDateOut);
    }
    
    

    @Override
    public List<RoomResponseDTO> getAllRoomFilter(Date dateIn, Date dateOut) {
        if (dateIn == null || dateOut == null) {
            // Jika filter tanggal tidak digunakan, kembalikan semua kamar yang belum dihapus
            return roomDb.findAll()
                        .stream()
                        .map(this::roomToRoomResponseDTO)
                        .collect(Collectors.toList());
        }

        // Step 1: Ambil semua reservasi yang bertabrakan dengan filter tanggal
        List<Reservation> overlappingReservations = reservationDb.findAll()
            .stream()
            .filter(reservation -> datesOverlap(reservation.getDateIn(), reservation.getDateOut(), dateIn, dateOut))
            .collect(Collectors.toList());

        // Step 2: Ekstrak roomId dari reservasi yang bertabrakan
        Set<String> overlappingRoomIds = overlappingReservations
            .stream()
            .map(Reservation::getRoomId)
            .collect(Collectors.toSet());

        // Step 3: Ambil semua kamar yang memiliki reservasi dalam rentang tanggal tersebut
        List<Room> reservedRooms = roomDb.findAll()
            .stream()
            .filter(room -> overlappingRoomIds.contains(room.getId()))
            .collect(Collectors.toList());

        // Kembalikan hasil dalam bentuk DTO
        return reservedRooms.stream()
                            .map(this::roomToRoomResponseDTO)
                            .collect(Collectors.toList());
    }


    
    private RoomResponseDTO roomToRoomResponseDTO(Room room) {

        RoomResponseDTO roomResponseDTO = new RoomResponseDTO();
        roomResponseDTO.setId(room.getId());
        roomResponseDTO.setName(room.getName());
        roomResponseDTO.setDescription(room.getDescription());
        roomResponseDTO.setMaxCapacity(room.getMaxCapacity());
        roomResponseDTO.setPricePerDay(room.getPricePerDay());
        roomResponseDTO.setCreatedAt(new java.sql.Timestamp(room.getCreatedAt().getTime()));
        roomResponseDTO.setUpdatedAt(new java.sql.Timestamp(room.getUpdatedAt().getTime()));
        roomResponseDTO.setDeletedAt(room.getDeletedAt());

        // Tambahkan daftar ID reservation jika diperlukan
        List<String> reservationIds = new ArrayList<>();
        for (Reservation reservation : room.getReservations()) {
            reservationIds.add(reservation.getId());
        }
        roomResponseDTO.setReservations(reservationIds);

        return roomResponseDTO;
        
    }
}
