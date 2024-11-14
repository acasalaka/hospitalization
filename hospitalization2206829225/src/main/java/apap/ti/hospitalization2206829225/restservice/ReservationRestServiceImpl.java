package apap.ti.hospitalization2206829225.restservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206829225.model.Facility;
import apap.ti.hospitalization2206829225.model.Patient;
import apap.ti.hospitalization2206829225.model.Reservation;
import apap.ti.hospitalization2206829225.model.Room;
import apap.ti.hospitalization2206829225.repository.FacilityDb;
import apap.ti.hospitalization2206829225.repository.NurseDb;
import apap.ti.hospitalization2206829225.repository.PatientDb;
import apap.ti.hospitalization2206829225.repository.ReservationDb;
import apap.ti.hospitalization2206829225.repository.RoomDb;
import apap.ti.hospitalization2206829225.restdto.request.AddReservationRequestRestDTO;
import apap.ti.hospitalization2206829225.restdto.request.UpdateReservationRequestRestDTO;
import apap.ti.hospitalization2206829225.restdto.response.ReservationResponseDTO;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class ReservationRestServiceImpl implements ReservationRestService {

    @Autowired
    ReservationDb reservationDb;

    @Autowired
    FacilityDb facilityDb;

    @Autowired
    PatientDb patientDb;


    @Autowired
    private RoomDb roomDb;

    @Override
    public List<ReservationResponseDTO> getAllReservation() {
        var listReservation = reservationDb.findAllActiveReservations();
        var listReservationResponseDTO = new ArrayList<ReservationResponseDTO>();
        listReservation.forEach(reservation -> {
            var reservationResponseDTO =reservationToReservationResponseDTO(reservation);
            listReservationResponseDTO.add(reservationResponseDTO);
        });

        return listReservationResponseDTO;
    }

    @Override
    public List<ReservationResponseDTO> getReservationsByPatientId(UUID patientId) {
        var listReservation = reservationDb.findByPatientId(patientId);
        var listReservationResponseDTO = new ArrayList<ReservationResponseDTO>();
        listReservation.forEach(reservation -> {
            var reservationResponseDTO = reservationToReservationResponseDTO(reservation);
            listReservationResponseDTO.add(reservationResponseDTO);
        });
        return listReservationResponseDTO;
    }

    @Override
    public List<ReservationResponseDTO> getReservationsByNurseId(UUID nurseId) {
        var listReservation = reservationDb.findByAssignedNurse(nurseId);
        var listReservationResponseDTO = new ArrayList<ReservationResponseDTO>();
        listReservation.forEach(reservation -> {
            var reservationResponseDTO = reservationToReservationResponseDTO(reservation);
            listReservationResponseDTO.add(reservationResponseDTO);
        });
        return listReservationResponseDTO;
    }

    
     private ReservationResponseDTO reservationToReservationResponseDTO(Reservation reservation) {
        var reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(reservation.getId());
        reservationResponseDTO.setPatientId(reservation.getPatientId());
        reservationResponseDTO.setDateIn(reservation.getDateIn());
        reservationResponseDTO.setDateOut(reservation.getDateOut());
        reservationResponseDTO.setTotalFee(reservation.getTotalFee());
        reservationResponseDTO.setAssignedNurse(reservation.getAssignedNurse());
        reservationResponseDTO.setRoomId(reservation.getRoomId());
        // reservationResponseDTO.setCreatedAt(new java.sql.Timestamp(reservation.getCreatedAt().getTime()));
        // reservationResponseDTO.setUpdatedAt(new java.sql.Timestamp(reservation.getUpdatedAt().getTime()));
        reservationResponseDTO.setCreatedAt(reservation.getCreatedAt());
        reservationResponseDTO.setUpdatedAt(reservation.getUpdatedAt());

        

        return reservationResponseDTO;
    }

    @Override
    public void deleteReservation(String id) {
        Reservation reservation = reservationDb.findById(id).orElse(null);

        if (reservation == null) {
            throw new IllegalArgumentException("Reservation with ID " + id + " not found!");
        }
        if (reservation.getDateIn().before(new Date())) {
            throw new IllegalArgumentException("Reservation with ID " + id + " cannot be deleted because it has passed the check-in date!");
        }
        reservation.setIsDeleted(true);
        reservationDb.save(reservation);
    }

    @Override
    public ReservationResponseDTO getReservationById(String id) {
        Reservation reservation = reservationDb.findById(id).orElse(null);
        if(reservation != null && !reservation.getIsDeleted()) {
            return reservationToReservationResponseDTO(reservation);
        }
        return null;
    }

    @Override
    public ReservationResponseDTO addReservation(AddReservationRequestRestDTO reservationDTO) {
        // Step 1: Validate Room Capacity
        Room room = roomDb.findById(reservationDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room with ID " + reservationDTO.getRoomId() + " not found"));
        
        long existingReservations = reservationDb.countByRoomIdAndDateRange(reservationDTO.getRoomId(), reservationDTO.getDateIn(), reservationDTO.getDateOut());
        if (existingReservations >= room.getMaxCapacity()) {
            throw new IllegalStateException("Room is fully booked for the selected date range");
        }

        // Step 2: Validate Appointment ID (If Provided)
        if (reservationDTO.getAppointmentId() != null && !mockCheckAppointment(reservationDTO.getAppointmentId())) {
            throw new IllegalArgumentException("Appointment with ID " + reservationDTO.getAppointmentId() + " is not available or does not exist");
        }

        // Step 3: Validate Facilities
        List<UUID> facilityIds = reservationDTO.getFacilities();
        if (facilityIds != null) {
            Set<UUID> uniqueFacilities = new HashSet<>(facilityIds);
            if (uniqueFacilities.size() != facilityIds.size()) {
                throw new IllegalArgumentException("Duplicate facility IDs are not allowed");
            }

            List<UUID> missingFacilities = new ArrayList<>();
            for (UUID facilityId : facilityIds) {
                if (!facilityDb.existsById(facilityId)) {
                    missingFacilities.add(facilityId);
                }
            }
            if (!missingFacilities.isEmpty()) {
                throw new IllegalArgumentException("The following facilities do not exist: " + missingFacilities);
            }
        }

        Patient patient = patientDb.findById(reservationDTO.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + reservationDTO.getPatientId()));
        
        // Mock Assigned Nurse ID, assuming nurse session is available
        UUID assignedNurseId = getMockNurseIdFromSession();
        if (assignedNurseId == null) {
            throw new IllegalArgumentException("Only users with Nurse role can create a reservation");
        }

        int totalReservations = reservationDb.countAllReservations();
        String reservationID = formatReservationID(reservationDTO.getDateIn(), reservationDTO.getDateOut(), patient.getNIK(), totalReservations);

        double totalFee = room.getPricePerDay() * getDateDifference(reservationDTO.getDateIn(), reservationDTO.getDateOut());

        Reservation reservation = new Reservation();
        reservation.setId(reservationID);
        reservation.setDateIn(reservationDTO.getDateIn());
        reservation.setDateOut(reservationDTO.getDateOut());
        reservation.setTotalFee(totalFee);
        reservation.setPatientId(patient.getId());
        reservation.setAssignedNurse(assignedNurseId);
        reservation.setRoomId(reservationDTO.getRoomId());
        reservation.setIsDeleted(false);

        reservationDb.save(reservation);

        return reservationToReservationResponseDTO(reservation);
    }

    private boolean mockCheckAppointment(String appointmentId) {
        return appointmentId.length() == 8; 
    }
    private UUID getMockNurseIdFromSession() {
        return UUID.fromString("e64fc207-19c4-4b79-8dda-a417595f657f"); 
    }
    

    @Override
    public ReservationResponseDTO updateReservation(String id, UpdateReservationRequestRestDTO reservationDTO) {
        Reservation reservation = reservationDb.findById(id).orElse(null);
        
        if (reservation == null || reservation.getIsDeleted()) {
            throw new IllegalArgumentException("Reservation not found");
        }
        
        if (reservation.getDateIn().before(new Date())) {
            throw new IllegalStateException("Cannot update reservation that is already started (past dateIn)");
        }
        
        String roomId = reservationDTO.getRoomId();
        if (!isRoomAvailable(roomId, reservationDTO.getDateIn(), reservationDTO.getDateOut())) {
            throw new IllegalStateException("Room with ID " + roomId + " is fully booked for the selected dates.");
        }

        reservation.setDateIn(reservationDTO.getDateIn());
        reservation.setDateOut(reservationDTO.getDateOut());
        reservation.setRoomId(roomId);
        reservation.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

        reservationDb.save(reservation);
        return reservationToReservationResponseDTO(reservation);
    }


    private boolean isRoomAvailable(String roomId, Date dateIn, Date dateOut) {
        Room room = roomDb.findById(roomId).orElse(null);
        
        if (room == null) {
            throw new IllegalArgumentException("Room not found with ID: " + roomId);
        }
    
        List<Reservation> reservations = reservationDb.findByRoomIdAndDateRange(roomId, dateIn, dateOut);
    
        return reservations.size() < room.getMaxCapacity();
    }
    


    // @Override
    // public ReservationResponseDTO updateFacilities(String reservationId, List<UUID> listOfFacilities) throws Exception {
    //     Reservation reservation = reservationDb.findById(reservationId).orElse(null);

    //     // Validasi: Cek apakah reservasi ditemukan
    //     if (reservation == null) {
    //         throw new IllegalArgumentException("Reservation with ID " + reservationId + " not found!");
    //     }

    //     // Validasi: Cek apakah dateOut belum tercapai
    //     if (reservation.getDateOut().before(new Date())) {
    //         throw new IllegalStateException("Facilities cannot be updated as the reservation has passed the check-out date.");
    //     }

    //     // Validasi: Cek apakah semua fasilitas yang di-request tersedia
    //     List<UUID> unavailableFacilityIds = new ArrayList<>();
    //     List<Facility> validFacilities = new ArrayList<>();

    //     for (UUID facilityId : listOfFacilities) {
    //         Optional<Facility> facilityOpt = facilityDb.findById(facilityId);
    //         if (facilityOpt.isPresent()) {
    //             validFacilities.add(facilityOpt.get());
    //         } else {
    //             unavailableFacilityIds.add(facilityId);
    //         }
    //     }

    //     // Jika ada fasilitas yang tidak ditemukan, kembalikan error dengan daftar ID fasilitas yang tidak tersedia
    //     if (!unavailableFacilityIds.isEmpty()) {
    //         throw new Exception("The following facility IDs were not found: " + unavailableFacilityIds);
    //     }

    //     // Validasi: Cek duplikasi ID fasilitas dalam payload
    //     Set<UUID> uniqueFacilities = new HashSet<>(listOfFacilities);
    //     if (uniqueFacilities.size() < listOfFacilities.size()) {
    //         throw new IllegalStateException("Duplicate facility IDs found in the request.");
    //     }

    //     // Update fasilitas reservasi
    //     reservation.setFacilities(validFacilities);
    //     reservationDb.save(reservation);

    //     // Konversi ke DTO untuk respon
    //     return reservationToReservationResponseDTO(reservation);
    // }

    public String formatReservationID(Date dateIn, Date dateOut, String patientNik, int totalReservations) {
        StringBuilder reservationID = new StringBuilder("RES");

        long dateDifference = getDateDifference(dateIn, dateOut);
        String dateDiffStr = String.format("%02d", dateDifference % 100);
        reservationID.append(dateDiffStr);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);
        String dayOfWeek = dayFormat.format(new Date()).toUpperCase();
        reservationID.append(dayOfWeek);

        String nikSuffix = patientNik.substring(patientNik.length() - 4);
        reservationID.append(nikSuffix);

        String totalResStr = String.format("%04d", totalReservations);
        reservationID.append(totalResStr);

        return reservationID.toString();
    }

    public long getDateDifference(Date dateIn, Date dateOut) {
        long diffInMillis = dateOut.getTime() - dateIn.getTime();
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1;
    }

    


} 
