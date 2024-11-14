package apap.ti.hospitalization2206829225.restservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206829225.model.Facility;
import apap.ti.hospitalization2206829225.model.Nurse;
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
    private PatientDb patientDb;

    @Autowired
    private NurseDb nurseDb;

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
        reservationResponseDTO.setCreatedAt(new java.sql.Timestamp(reservation.getCreatedAt().getTime()));
        reservationResponseDTO.setUpdatedAt(new java.sql.Timestamp(reservation.getUpdatedAt().getTime()));

        

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

    // @Override
    // public ReservationResponseDTO addReservation(AddReservationRequestRestDTO reservationDTO) {
    //     if (reservationDTO.getFacilities() == null) {
    //         reservationDTO.setFacilities(new ArrayList<>());
    //     }

    //     Reservation newReservation = new Reservation();
    //     newReservation.setPatientId(reservationDTO.getPatientId());
    //     newReservation.setDateIn(reservationDTO.getDateIn());
    //     newReservation.setDateOut(reservationDTO.getDateOut());
    //     newReservation.setAssignedNurse(reservationDTO.getAssignedNurse());
    //     newReservation.setRoomId(reservationDTO.getRoomId());

    //     List<Facility> facilities = new ArrayList<>();
    //     for (UUID facilityId : reservationDTO.getFacilities()) {
    //        Facility facility = facilityDb.findById(facilityId).orElse(null);
    //        if(facility != null) {
    //            facilities.add(facility);
    //        }
    //     }
    //     newReservation.setFacilities(facilities);
    //     reservationDb.save(newReservation);
    //     return reservationToReservationResponseDTO(newReservation);
    // }


    //=======================================================================================================


    // @Override
    // public ReservationResponseDTO addReservation(AddReservationRequestRestDTO reservationDTO) {
    //     // UUID assignedNurseId = getLoggedInNurseId();
    //     // if (assignedNurseId == null) {
    //     //     throw new IllegalStateException("Only users with Nurse role can create reservations.");
    //     // }

    //     // Validasi appointment, room, facilities, dll seperti sebelumnya...
    //     if (reservationDTO.getFacilities() == null) {
    //                 reservationDTO.setFacilities(new ArrayList<>());
    //             }

    //     // Mengambil NIK pasien berdasarkan patientId
    //     Patient patient = patientDb.findById(reservationDTO.getPatientId()).orElseThrow(() ->
    //             new IllegalArgumentException("Patient not found"));

    //     String patientNik = patient.getNIK();
        
    //     // Menghitung total reservasi untuk keperluan ID
    //     int totalReservations = (int) reservationDb.count();

    //     // Menghasilkan ID reservasi
    //     String reservationId = formatReservationID(reservationDTO.getDateIn(), reservationDTO.getDateOut(), patientNik, totalReservations);

    //     // Pastikan ID unik dengan pengecekan
    //     while (reservationDb.existsById(reservationId)) {
    //         totalReservations += 1;
    //         reservationId = formatReservationID(reservationDTO.getDateIn(), reservationDTO.getDateOut(), patientNik, totalReservations);
    //     }

    //     // Buat reservasi baru dengan ID yang dihasilkan
    //     Reservation newReservation = new Reservation();
    //     newReservation.setId(reservationId);
    //     newReservation.setPatientId(reservationDTO.getPatientId());
    //     newReservation.setDateIn(reservationDTO.getDateIn());
    //     newReservation.setDateOut(reservationDTO.getDateOut());
    //     newReservation.setTotalFee(reservationDTO.getTotalFee());
    //     newReservation.setAssignedNurse(assignedNurseId);
    //     newReservation.setRoomId(reservationDTO.getRoomId());
    //     // newReservation.setFacilities(validFacilities);

    //     // Jika appointmentId null, buat data Bill di Bill Service
    //     // if (reservationDTO.getAppointmentId() == null) {
    //     //     createBillForReservation(newReservation);
    //     // }

    //     reservationDb.save(newReservation);
    //     return reservationToReservationResponseDTO(newReservation);
    // }

    // =======================================================================================================

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
    


    @Override
    public ReservationResponseDTO updateFacilities(String reservationId, List<UUID> listOfFacilities) {
        Reservation reservation = reservationDb.findById(reservationId).orElse(null);
        
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found");
        }
        
        // Validasi tanggal: hanya bisa update jika belum masuk tanggal dateOut
        if (reservation.getDateOut().before(new Date())) {
            throw new IllegalStateException("Cannot update facilities for reservation that has already ended (past dateOut)");
        }
        
        // Pengecekan fasilitas yang tidak ditemukan
        List<UUID> notFoundFacilities = new ArrayList<>();
        List<Facility> validFacilities = new ArrayList<>();
        for (UUID facilityId : listOfFacilities) {
            Facility facility = facilityDb.findById(facilityId).orElse(null);
            if (facility != null) {
                validFacilities.add(facility);
            } else {
                notFoundFacilities.add(facilityId);
            }
        }

        // Jika ada fasilitas yang tidak ditemukan, kembalikan ID-ID tersebut di response
        if (!notFoundFacilities.isEmpty()) {
            throw new IllegalArgumentException("Facilities with ID " + notFoundFacilities + " not found");
        }

        // Update facilities jika semua fasilitas valid
        reservation.setFacilities(validFacilities);
        reservation.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        reservationDb.save(reservation);

        return reservationToReservationResponseDTO(reservation);
    }

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

    @Override
    public ReservationResponseDTO addReservation(AddReservationRequestRestDTO reservationDTO) {
        if (reservationDTO.getFacilities() == null) {
            reservationDTO.setFacilities(new ArrayList<>());
        }

        Reservation newReservation = new Reservation();
        newReservation.setPatientId(reservationDTO.getPatientId());
        newReservation.setDateIn(reservationDTO.getDateIn());
        newReservation.setDateOut(reservationDTO.getDateOut());
        newReservation.setAssignedNurse(reservationDTO.getAssignedNurse());
        newReservation.setRoomId(reservationDTO.getRoomId());

        // List<Facility> facilities = new ArrayList<>();
        // for (Long facilityId : reservationDTO.getFacilities()) {
        //     Facility facility = facilityDb.findById(facilityId).orElse(null);
        //     if(facility != null) {
        //         facilities.add(facility);
        //     }
        // }
        // newReservation.setFacilities(facilities);
        reservationDb.save(newReservation);
        return reservationToReservationResponseDTO(newReservation);
    }


} 
