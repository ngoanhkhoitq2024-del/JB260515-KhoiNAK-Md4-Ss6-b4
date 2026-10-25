package re.edu.appointmentservice.dto;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentDate,
        String reason
) {
}
