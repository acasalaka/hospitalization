package apap.ti.hospitalization2206829225.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import apap.ti.hospitalization2206829225.DTO.AddRoomDTO;
import apap.ti.hospitalization2206829225.DTO.UpdateRoomDTO;
import apap.ti.hospitalization2206829225.model.Reservation;
import apap.ti.hospitalization2206829225.model.Room;
import apap.ti.hospitalization2206829225.service.ReservationService;
import apap.ti.hospitalization2206829225.service.RoomService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;






@Controller
public class roomController {
    
    @Autowired
    private RoomService roomService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/rooms")
    public String viewallRoom(Model model) {
        var listRoom = roomService.getAllRoom();
        
        model.addAttribute("listRoom", listRoom);
        model.addAttribute("activePage", "rooms");
        return "viewall-room";
    }


    @GetMapping("/rooms/create")
    public String createRoom(Model model) {
        model.addAttribute("roomDTO", new AddRoomDTO());
        return "form-add-room";
    }
    
    @PostMapping("/rooms/create")
    public String createRoomForm(@ModelAttribute AddRoomDTO roomDTO, Model model) {
        var room = new Room();
        room.setName(roomDTO.getName());
        room.setMaxCapacity(roomDTO.getMaxCapacity());
        room.setPricePerDay(roomDTO.getPricePerDay());
        room.setDescription(roomDTO.getDescription());

        String generatedRoomId = roomService.generateRoomId();
        room.setId(generatedRoomId);

        roomService.saveRoom(room);

        model.addAttribute("responseMessage", 
            String.format("Room baru dengan nama '%s' berhasil ditambahkan.", room.getName()));



        return "feedback-add-room"; 
        
    }


    @GetMapping("/rooms/{roomId}/update")
    public String updateRoom(@PathVariable("roomId") String roomId, Model model) {
        var existingRoom = roomService.getRoomByID(roomId);
        var roomDTO = new UpdateRoomDTO();

        roomDTO.setId(existingRoom.getId());
        roomDTO.setDescription(existingRoom.getDescription());
        roomDTO.setMaxCapacity(existingRoom.getMaxCapacity());
        roomDTO.setName(existingRoom.getName());
        roomDTO.setPricePerDay(existingRoom.getPricePerDay());

        model.addAttribute("roomDTO", roomDTO);
        return "form-update-room";
    }

    @PostMapping("/rooms/{roomId}/update")
    public String updateRoom(@PathVariable("roomId") String roomId,@Valid @ModelAttribute UpdateRoomDTO roomDTO, Model model) {
        var room = roomService.getRoomByID(roomDTO.getId());
        room.setId(roomDTO.getId());
        room.setDescription(roomDTO.getDescription());
        room.setMaxCapacity(roomDTO.getMaxCapacity());
        room.setName(roomDTO.getName());
        room.setPricePerDay(roomDTO.getPricePerDay());
        roomService.updateRoom(room);

        model.addAttribute("responseMessage", 
            String.format("Room %s dengan ID %s berhasil diupdate.", room.getName(), room.getId()));
        
        return "feedback-update-room";
    }

    @GetMapping("/rooms/{roomId}")
    public String viewRoomDetail(@PathVariable("roomId") String roomId, Model model) {
        var room = roomService.getRoomByID(roomId);
        if (room == null) {
            model.addAttribute("errorMessage", "Room not found.");
            return "error-page"; 
        }
        model.addAttribute("room", room);
        model.addAttribute("activePage", room);


        return "view-room";
    }
    
    @PostMapping("/rooms/{roomId}/delete")
    public String deleteRoom(@PathVariable("roomId") String roomId, Model model) {
        var room = roomService.getRoomByID(roomId);
        roomService.deleteRoomById(room);
        
        model.addAttribute("responseMessage", String.format("Berhasil menghapus ruangan dengan ID %s.", roomId));
        return "feedback-delete-room";
    }
    
    
}
