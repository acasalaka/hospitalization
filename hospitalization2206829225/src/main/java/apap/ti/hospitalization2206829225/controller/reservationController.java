package apap.ti.hospitalization2206829225.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import apap.ti.hospitalization2206829225.DTO.AddReservationDTO;
import apap.ti.hospitalization2206829225.DTO.ShowPatientDTO;
import apap.ti.hospitalization2206829225.DTO.searchDTO;
import apap.ti.hospitalization2206829225.model.Facility;
import apap.ti.hospitalization2206829225.model.Nurse;
import apap.ti.hospitalization2206829225.model.Patient;
import apap.ti.hospitalization2206829225.model.Reservation;
import apap.ti.hospitalization2206829225.model.Room;
import apap.ti.hospitalization2206829225.service.FacilityService;
import apap.ti.hospitalization2206829225.service.NurseService;
import apap.ti.hospitalization2206829225.service.PatientService;
import apap.ti.hospitalization2206829225.service.ReservationService;
import apap.ti.hospitalization2206829225.service.RoomService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class reservationController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private ReservationService reservationService; 


    @GetMapping("/searchPatient")
    public String searchPatientForm(Model model) {
        model.addAttribute("searchDTO", new searchDTO());
        return "search-patient";  
    }

    @PostMapping("/searchPatient")
    public String searchPatient(@ModelAttribute("searchDTO") searchDTO searchDTO, Model model ){
        String nik = searchDTO.getNik();
        Patient patient = patientService.getPatientByNik(nik);

        if (patient == null){
            model.addAttribute("message", "Patient Not Found");
            return "patient-not-found";
        }

        ShowPatientDTO patientDTO = new ShowPatientDTO();
        patientDTO.setId(patient.getId());
        patientDTO.setNama(patient.getName());
        patientDTO.setNIK(patient.getNIK());
        patientDTO.setGender(patient.getGender());
        patientDTO.setBirthDate(patient.getBirthDate());
        patientDTO.setEmail(patient.getEmail());

        model.addAttribute("patientDTO", patientDTO);
        return "patient-found";
    }
    

   @GetMapping("/reservations/validateDate/{patientId}")
    public String validateDateAndFindRooms(@PathVariable ("patientId") UUID patientId,Model model) {
        
        Patient patient = patientService.getPatientByID(patientId);
        model.addAttribute("patientName", patient.getName());
        model.addAttribute("patientId", patientId);
        model.addAttribute("nurseList", nurseService.getAllNurses());
        
        AddReservationDTO reservationDTO = new AddReservationDTO();
        reservationDTO.setPatientId(patientId);
        model.addAttribute("reservationDTO", reservationDTO);
        
        return "find-room";
    }

    @PostMapping("/reservations/validateDate/{patientId}")
    public String processRoomSelection(@PathVariable("patientId") UUID patientId, @ModelAttribute("reservationDTO") AddReservationDTO reservationDTO, Model model){
        Patient patient = patientService.getPatientByID(patientId);
        model.addAttribute("patientName", patient.getName());
        model.addAttribute("patientId", patient.getId());
        model.addAttribute("nurseList", nurseService.getAllNurses());

        if (reservationDTO.getDateIn().compareTo(reservationDTO.getDateOut()) > 0) {
            model.addAttribute("isDateChoosed", false);
            model.addAttribute("errorMessage", "Tanggal masuk harus kurang atau sama dengan tanggal keluar.");
            return "find-room";
        }
        
        List<Room> availableRooms = roomService.getAvailableRooms(reservationDTO.getDateIn(), reservationDTO.getDateOut());
        model.addAttribute("roomList", availableRooms );
        model.addAttribute("isDateChoosed", true);
        return "find-room";

    }

    // @PostMapping("/reservations/validateDate/{patientId}")
    // public String processRoomSelection(@PathVariable("patientId") UUID patientId, @ModelAttribute AddReservationDTO reservationDTO, Model model) {
    //     List<Nurse> nurses = nurseService.getAllNurses();
    //     reservationDTO.setPatientId(patientId);

    //     if (reservationDTO.getDateIn().compareTo(reservationDTO.getDateOut()) > 0) {
    //         model.addAttribute("errorMessage", "Tanggal masuk harus kurang atau sama dengan tanggal keluar.");
    //         model.addAttribute("nurses", nurses);
    //         return "find-room"; 
    //     }

    //     List<Room> availableRooms = roomService.getAvailableRooms(reservationDTO.getDateIn(), reservationDTO.getDateOut());

    //     if (availableRooms.isEmpty()) {
    //         model.addAttribute("errorMessage", "Tidak ada ruangan tersedia untuk tanggal tersebut.");
    //         model.addAttribute("nurses", nurses);
    //         return "find-room"; 
    //     }

    //     model.addAttribute("availableRooms", availableRooms);
    //     model.addAttribute("reservationDTO", reservationDTO); 
    //     model.addAttribute("dateIn", reservationDTO.getDateIn());
    //     model.addAttribute("dateOut", reservationDTO.getDateOut());
        
    //     return "find-room"; 
    // }

    @GetMapping("/reservations/addFacilities")
    public String addFacilitiesPage(@Valid @ModelAttribute AddReservationDTO reservationDTO, Model model) {
        List<Facility> facilities = facilityService.getAllFacilities();
        model.addAttribute("facilities", facilities);
        model.addAttribute("reservationDTO", reservationDTO);
        return "add-facilities";
    }

    // @GetMapping("/reservations/addFacilities/{patientId}")
    // public String addFacilitiesPage(
    //         @PathVariable("patientId") UUID patientId,
    //         @RequestParam("roomId") String roomId, 
    //         @RequestParam("dateIn") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateIn,
    //         @RequestParam("dateOut") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOut,
    //         Model model, 
    //         @ModelAttribute AddReservationDTO reservationDTO) {

    //     Room selectedRoom = roomService.getRoomByID(roomId);
    //     reservationDTO.setRoomId(roomId);
    //     reservationDTO.setDateIn(dateIn);
    //     reservationDTO.setDateOut(dateOut);
    //     reservationDTO.setRoomName(selectedRoom.getName());
    //     reservationDTO.setPatientId(patientId);

    //     List<Facility> facilities = facilityService.getAllFacilities();

    //     model.addAttribute("room", selectedRoom);
    //     model.addAttribute("facilities", facilities);
    //     model.addAttribute("reservationDTO", reservationDTO);
        


    //     return "add-facilities";
    // }


    @PostMapping("/reservations/addFacilities/{patientId}")
    public String submitReservation(
            @PathVariable("patientId") UUID patientId,
            @RequestParam("dateIn") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateIn,
            @RequestParam("dateOut") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOut,
            @RequestParam("roomId") String roomId,
            @ModelAttribute AddReservationDTO reservationDTO, 
            Model model) 
            {

                
        reservationDTO.setDateIn(dateIn);
        reservationDTO.setDateOut(dateOut);
        reservationDTO.setRoomId(roomId);

        System.out.println("Patient ID: " + patientId);
        System.out.println("DateIn : " + reservationDTO.getDateIn());
        System.out.println("DateOut : " + reservationDTO.getDateOut());
        System.out.println("Room ID: " + reservationDTO.getRoomId());
        System.out.println("Patient ID: " + patientId);
        System.out.println("DateIn : " + reservationDTO.getDateIn());
        System.out.println("DateOut : " + reservationDTO.getDateOut());
        System.out.println("Room ID: " + reservationDTO.getRoomId());
        System.out.println("Patient ID: " + patientId);
        System.out.println("DateIn : " + reservationDTO.getDateIn());
        System.out.println("DateOut : " + reservationDTO.getDateOut());
        System.out.println("Room ID: " + reservationDTO.getRoomId());
        System.out.println("Patient ID: " + patientId);
        System.out.println("DateIn : " + reservationDTO.getDateIn());
        System.out.println("DateOut : " + reservationDTO.getDateOut());
        System.out.println("Room ID: " + reservationDTO.getRoomId());
        System.out.println("Patient ID: " + patientId);
        System.out.println("DateIn : " + reservationDTO.getDateIn());
        System.out.println("DateOut : " + reservationDTO.getDateOut());
        System.out.println("Room ID: " + reservationDTO.getRoomId());
        System.out.println("Patient ID: " + patientId);
        System.out.println("DateIn : " + reservationDTO.getDateIn());
        System.out.println("DateOut : " + reservationDTO.getDateOut());
        System.out.println("Room ID: " + reservationDTO.getRoomId());

       long duration = TimeUnit.DAYS.convert(
        reservationDTO.getDateOut().getTime() - reservationDTO.getDateIn().getTime(),
        TimeUnit.MILLISECONDS);
        

        double roomPrice = roomService.getRoomByID(roomId).getPricePerDay();
        double totalFee = roomPrice * duration;
        
        List<Facility> selectedFacilities = reservationDTO.getListFacilities();
        double facilityFee = selectedFacilities.stream().mapToDouble(Facility::getFee).sum();
        totalFee += facilityFee;

        var reservation = new Reservation();
        reservation.setRoomId(reservationDTO.getRoomId());
        reservation.setDateIn(reservationDTO.getDateIn());
        reservation.setDateOut(reservationDTO.getDateOut());
        reservation.setAssignedNurse(reservationDTO.getAssignedNurseId());
        reservation.setPatientId(patientId);
        reservation.setTotalFee(totalFee);
        reservation.setFacilities(selectedFacilities);

        String reservationId = reservationService.generateReservationId(reservationDTO.getDateIn(), reservationDTO.getDateOut(), patientId);
        reservation.setId(reservationId);

        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Reservation ID: " + reservationId);


        reservationService.addReservation(reservation);

        model.addAttribute("message", "Reservation successfully created with ID: " + reservation.getId());
        return "feedback-add-reservation"; 
    }


     @GetMapping("/reservations")
    public String viewAllReservations(Model model) {
        List<Reservation> listReservation = reservationService.getAllReservation();

        Map<String, String> roomNames = new HashMap<>();
        Map<String, String> patientNames = new HashMap<>();

        for (Reservation reservation : listReservation) {
            Room room = roomService.getRoomByID(reservation.getRoomId());
            Patient patient = patientService.getPatientByID(reservation.getPatientId());

            roomNames.put(reservation.getId(), room.getName());
            patientNames.put(reservation.getId(), patient.getName());
        }

        model.addAttribute("listReservation", listReservation);
        model.addAttribute("roomNames", roomNames);  
        model.addAttribute("patientNames", patientNames);  
        model.addAttribute("activePage", "reservation");
        return "viewall-reservation";
    }   


    @GetMapping("/reservations/{reservationId}")
    public String viewDetailReservation(@PathVariable("reservationId") String reservationId, Model model) {
        var reservation = reservationService.getReservationById(reservationId);

        Room room = roomService.getRoomByID(reservation.getRoomId());
        Patient patient = patientService.getPatientByID(reservation.getPatientId());
        Nurse nurse = nurseService.getNurseById(reservation.getAssignedNurse());

        List<Facility> facilities = reservation.getFacilities();

        long diffDays = ChronoUnit.DAYS.between(reservation.getDateIn().toInstant(), reservation.getDateOut().toInstant());

        double roomTotal = room.getPricePerDay() * diffDays;

        model.addAttribute("reservation", reservation);
        model.addAttribute("room", room);
        model.addAttribute("patient", patient);
        model.addAttribute("nurse", nurse);
        model.addAttribute("facilities", facilities);
        model.addAttribute("roomTotal", roomTotal);
        model.addAttribute("diffDays", diffDays); 
        model.addAttribute("activePage", "reservation");

        return "view-detail-reservation";
    }


    @GetMapping("/reservations/{reservationId}/update-facilities")
    public String showUpdateFacilitiesForm(@PathVariable("reservationId") String reservationId, Model model) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        List<Facility> allFacilities = facilityService.getAllFacilities();
        
        List<UUID> selectedFacilityIds = reservation.getFacilities().stream()
                .map(Facility::getId)
                .collect(Collectors.toList());

        model.addAttribute("reservation", reservation);
        model.addAttribute("allFacilities", allFacilities);
        model.addAttribute("selectedFacilityIds", selectedFacilityIds); 

        return "form-update-facilities";
    }



    @PostMapping("/reservations/{reservationId}/update-facilities")
    public String updateFacilities(@PathVariable("reservationId") String reservationId,
                                @RequestParam List<UUID> selectedFacilityIds, Model model) {
        Reservation reservation = reservationService.getReservationById(reservationId);

        List<Facility> selectedFacilities = new ArrayList<>();
        for (UUID facilityId : selectedFacilityIds) {
            Facility facility = facilityService.getFacilityById(facilityId);
            selectedFacilities.add(facility);
        }

        reservation.setFacilities(selectedFacilities);

        Room room = roomService.getRoomByID(reservation.getRoomId());
        double roomPrice = room.getPricePerDay();
        long days = ChronoUnit.DAYS.between(reservation.getDateIn().toInstant(), reservation.getDateOut().toInstant());
        double facilitiesFee = selectedFacilities.stream().mapToDouble(Facility::getFee).sum();

        double totalFee = roomPrice * days + facilitiesFee;
        reservation.setTotalFee(totalFee);

        reservationService.addReservation(reservation);

        model.addAttribute("responseMessage", "Reservation facilities updated successfully!");
        return "feedback-update-facilities";
    }

    @GetMapping("/reservations/{reservationId}/delete")
    public String showDeleteConfirmation(@PathVariable String reservationId, Model model) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation == null) {
            model.addAttribute("errorMessage", "Reservation not found");
            return "error-page"; 
        }
        model.addAttribute("reservationId", reservationId);
        return "feedback-delete-reservations"; 
    }

    @PostMapping("/reservations/{reservationId}/delete")
    public String deleteReservation(@PathVariable String reservationId, Model model) {
        try {
            reservationService.deleteReservation(reservationId);
            model.addAttribute("message", "Berhasil menghapus reservasi dengan ID :  " + reservationId);
            return "feedback-delete-reservation";  
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";  
        }
    }


}

