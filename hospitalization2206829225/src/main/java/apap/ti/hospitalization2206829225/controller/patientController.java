package apap.ti.hospitalization2206829225.controller;

import apap.ti.hospitalization2206829225.DTO.AddPatientDTO;
import apap.ti.hospitalization2206829225.DTO.AddReservationDTO;
import apap.ti.hospitalization2206829225.DTO.searchDTO;
import apap.ti.hospitalization2206829225.model.Nurse;
import apap.ti.hospitalization2206829225.model.Patient;
import apap.ti.hospitalization2206829225.service.NurseService;
import apap.ti.hospitalization2206829225.service.PatientService;
import apap.ti.hospitalization2206829225.service.ReservationService;
import apap.ti.hospitalization2206829225.service.RoomService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class patientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private RoomService roomService;


    @Autowired
    private NurseService nurseService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/")
    public String homePage(Model model) {
        int totalReservations = reservationService.getTotalReservations();
        int totalRooms = roomService.getTotalRooms();
        int totalPatients = patientService.getTotalPatients();

        model.addAttribute("totalReservations", totalReservations);
        model.addAttribute("totalRooms", totalRooms);
        model.addAttribute("totalPatients", totalPatients);
        model.addAttribute("activePage", "home");

        return "home";
    }

    @GetMapping("/patients/create")
    public String createPatient(Model model) {
        model.addAttribute("patientDTO", new AddPatientDTO());
        return "form-add-patient";
    }

    @PostMapping("/patients/create")
    public String createPatientForm(@ModelAttribute AddPatientDTO patientDTO, Model model) {
        var patient = new Patient();
        List<Nurse> nurses = nurseService.getAllNurses();

        patient.setName(patientDTO.getNama());
        patient.setEmail(patientDTO.getEmail());
        patient.setGender(patientDTO.isGender());
        patient.setBirthDate(patientDTO.getBirthDate());
        patient.setCreatedDate(java.time.LocalDateTime.now());
        patient.setNIK(patientDTO.getNIK());

        patientService.addNewPatient(patient);
        

        model.addAttribute("reservationDTO", new AddReservationDTO());
        model.addAttribute("nurses", nurses);



        return "redirect:/reservations/validateDate/" + patient.getId(); 
        
    }
    
}
