package co.crediya.sqs.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record SolicitudAprobadaMessage(
        String idSolicitud,
        BigDecimal monto,
        Instant occurredOn,
        String eventName
) {}
