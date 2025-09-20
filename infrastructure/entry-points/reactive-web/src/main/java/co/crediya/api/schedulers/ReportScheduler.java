package co.crediya.api.schedulers;

import co.crediya.api.dto.ReporteDto;
import co.crediya.usecase.external.ExternalPublisher;
import co.crediya.usecase.reporte_contador.ReporteContadorUseCase;
import co.crediya.usecase.reporte_monto.ReporteMontoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Log4j2
public class ReportScheduler {

    private final ExternalPublisher publisher;
    private final ReporteContadorUseCase reporteContadorUseCase;
    private final ReporteMontoUseCase reporteMontoUseCase;


    @Scheduled(cron = "*/50 * * * * *")
    public void enviarReporteDiario() {
        Mono.zip(
                        reporteContadorUseCase.obtenerValorContador("CONTADOR_SOLICITUDES_APROBADAS"),
                        reporteMontoUseCase.obtenerMontoTotal("MONTO_TOTAL_SOLICITUDES_APROBADAS")
                ).flatMap(tuple -> {
                    Long contador = tuple.getT1();
                    BigDecimal montoTotal = tuple.getT2();

                    ReporteDto reporte = new ReporteDto(contador, montoTotal);

                    return publisher.sendReporteDiario(reporte);
                })
                .doOnSuccess(v -> log.info("Reporte diario enviado exitosamente"))
                .doOnError(e -> log.error("Error al enviar el reporte diario: {}", e.getMessage(), e))
                .onErrorResume(e -> {
                    log.warn("Fallo el envío de reporte diario, se continuará sin interrumpir la app");
                    return Mono.empty();
                })
                .subscribe();
    }





}
