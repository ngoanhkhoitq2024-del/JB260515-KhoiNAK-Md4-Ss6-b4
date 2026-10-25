package re.edu.appointmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import re.edu.appointmentservice.dto.AppointmentResponse;
import re.edu.appointmentservice.dto.CreateAppointmentRequest;
import re.edu.appointmentservice.entity.Appointment;
import re.edu.appointmentservice.entity.AppointmentStatus;
import re.edu.appointmentservice.repository.AppointmentRepository;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final RestTemplate restTemplate;

    public AppointmentResponse createAppointment(
            CreateAppointmentRequest request) {

        // 1. Kiểm tra bệnh nhân có tồn tại không
        checkPatientExists(request.patientId());

        // 2. Kiểm tra bác sĩ có tồn tại không
        checkDoctorExists(request.doctorId());

        // 3. Tạo lịch hẹn
        Appointment appointment = new Appointment();
        appointment.setPatientId(request.patientId());
        appointment.setDoctorId(request.doctorId());
        appointment.setAppointmentDate(request.appointmentDate());
        appointment.setReason(request.reason());
        appointment.setStatus(AppointmentStatus.PENDING);

        // 4. Lưu lịch hẹn vào database
        Appointment saved = appointmentRepository.save(appointment);

        // 5. Trả về DTO
        return new AppointmentResponse(
                saved.getId(),
                saved.getPatientId(),
                saved.getDoctorId(),
                saved.getAppointmentDate(),
                saved.getReason(),
                saved.getStatus()
        );
    }

    private void checkPatientExists(Long patientId) {
        try {
            restTemplate.getForEntity(
                    "http://PATIENT-SERVICE/api/v1/patients/{id}",
                    Object.class,
                    patientId
            );
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new IllegalArgumentException(
                        "Patient not found: " + patientId);
            }
            throw ex;
        }
    }

    private void checkDoctorExists(Long doctorId) {
        try {
            restTemplate.getForEntity(
                    "http://DOCTOR-SERVICE/api/v1/doctors/{id}",
                    Object.class,
                    doctorId
            );
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new IllegalArgumentException(
                        "Doctor not found: " + doctorId);
            }
            throw ex;
        }
    }
}
