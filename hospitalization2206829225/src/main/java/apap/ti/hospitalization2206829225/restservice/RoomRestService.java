package apap.ti.hospitalization2206829225.restservice;

import java.util.Date;
import java.util.List;

import apap.ti.hospitalization2206829225.restdto.response.RoomResponseDTO;

public interface RoomRestService {
    List<RoomResponseDTO> getAllRoom();
    List<RoomResponseDTO> getAllRoomFilter(Date dateIn, Date dateOut);
}
