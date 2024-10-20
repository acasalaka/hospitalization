package apap.ti.hospitalization2206829225.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import apap.ti.hospitalization2206829225.DTO.AddReservationDTO;
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


    @GetMapping("/reservations")
    public String searchPatientForm(Model model) {
        model.addAttribute("searchDTO", new searchDTO());
        return "search-patient";  
    }
    
    @PostMapping("/reservations")
    public String searchPatient(@RequestParam("NIK") String NIK, Model model) {
        var patient = patientService.getPatientByNik(NIK);
        if (patient == null) {
            model.addAttribute("message", "Patient Not Found");
            return "patient-not-found";
        }
        model.addAttribute("patient", patient);
        model.addAttribute("patientId", patient.getId());  
        return "patient-found";
    }


   @GetMapping("/reservations/validateDate")
    public String validateDateAndFindRooms(Model model) {
        var reservationDTO = new AddReservationDTO();

        
        List<Nurse> nurses = nurseService.getAllNurses();
        List<Room> availableRooms = roomService.getAllRoom();



        model.addAttribute("name", reservationDTO.getPatientId());
        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("nurses", nurses);
        model.addAttribute("reservationDTO", reservationDTO);
        
        return "find-room";
    }
    


}

