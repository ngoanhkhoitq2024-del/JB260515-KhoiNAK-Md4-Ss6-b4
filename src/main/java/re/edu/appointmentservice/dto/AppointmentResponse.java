package re.edu.appointmentservice.dto;

import re.edu.appointmentservice.entity.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentDate,
        String reason,
        AppointmentStatus status
) {
}