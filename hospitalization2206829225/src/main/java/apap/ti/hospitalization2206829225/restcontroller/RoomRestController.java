package apap.ti.hospitalization2206829225.restcontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti.hospitalization2206829225.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206829225.restdto.response.RoomResponseDTO;
import apap.ti.hospitalization2206829225.restservice.RoomRestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Date;


@RestController
@RequestMapping("/api/rooms")
public class RoomRestController {

    @Autowired
    private RoomRestService roomService;
    
    @GetMapping("/filter")
    public ResponseEntity<?> getAllRoomFilter(
        @RequestParam("dateIn") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateIn,
        @RequestParam("dateOut") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOut){
        var baseResponseDTO = new BaseResponseDTO<List<RoomResponseDTO>>();
        List<RoomResponseDTO> roomList = roomService.getAllRoomFilter(dateIn, dateOut);

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(roomList);
        baseResponseDTO.setMessage("List of rooms found successfully");
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
    @GetMapping("/viewall")
    public ResponseEntity<?> getAllRooms(){
        var baseResponseDTO = new BaseResponseDTO<List<RoomResponseDTO>>();
        List<RoomResponseDTO> rommList = roomService.getAllRoom();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(rommList);
        baseResponseDTO.setMessage("List of rooms found successfully");
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
    
}
