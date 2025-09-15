package co.crediya.usecase.reporte_monto;

import co.crediya.model.reporte_monto.ReporteMonto;
import co.crediya.model.reporte_monto.gateways.ReporteMontoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteMontoUseCaseTest {
    @Mock
    private ReporteMontoRepository reporteMontoRepository;

    @InjectMocks
    private ReporteMontoUseCase reporteMontoUseCase;

    @Test
    void sumarMontoAprobado_existente_sumaAlValor() {
        String tipo = "MONTO_SOLICITUDES_APROBADAS";
        ReporteMonto existente = new ReporteMonto(tipo, new BigDecimal("100.00"));
        ReporteMonto actualizado = new ReporteMonto(tipo, new BigDecimal("150.00"));

        when(reporteMontoRepository.findByTipo(tipo)).thenReturn(Mono.just(existente));
        when(reporteMontoRepository.save(any())).thenReturn(Mono.just(actualizado));

        Mono<ReporteMonto> result = reporteMontoUseCase.sumarMontoAprobado(tipo, new BigDecimal("50.00"));

        StepVerifier.create(result)
                .expectNextMatches(r -> r.getValor().compareTo(new BigDecimal("150.00")) == 0)
                .verifyComplete();

        verify(reporteMontoRepository).findByTipo(tipo);
        verify(reporteMontoRepository).save(any(ReporteMonto.class));
    }

    @Test
    void sumarMontoAprobado_inexistente_creaNuevo() {
        String tipo = "NUEVO_MONTO";
        ReporteMonto nuevo = new ReporteMonto(tipo, new BigDecimal("75.00"));

        when(reporteMontoRepository.findByTipo(tipo)).thenReturn(Mono.empty());
        when(reporteMontoRepository.save(any())).thenReturn(Mono.just(nuevo));

        Mono<ReporteMonto> result = reporteMontoUseCase.sumarMontoAprobado(tipo, new BigDecimal("75.00"));

        StepVerifier.create(result)
                .expectNextMatches(r -> r.getValor().compareTo(new BigDecimal("75.00")) == 0 && r.getTipo().equals(tipo))
                .verifyComplete();

        verify(reporteMontoRepository).findByTipo(tipo);
        verify(reporteMontoRepository).save(any(ReporteMonto.class));
    }

    @Test
    void obtenerMontoTotal_existente_devuelveValor() {
        String tipo = "MONTO_EXISTENTE";
        ReporteMonto existente = new ReporteMonto(tipo, new BigDecimal("200.00"));

        when(reporteMontoRepository.findByTipo(tipo)).thenReturn(Mono.just(existente));

        Mono<BigDecimal> result = reporteMontoUseCase.obtenerMontoTotal(tipo);

        StepVerifier.create(result)
                .expectNext(new BigDecimal("200.00"))
                .verifyComplete();

        verify(reporteMontoRepository).findByTipo(tipo);
    }

    @Test
    void obtenerMontoTotal_inexistente_devuelveCero() {
        String tipo = "SIN_MONTO";
        when(reporteMontoRepository.findByTipo(tipo)).thenReturn(Mono.empty());

        Mono<BigDecimal> result = reporteMontoUseCase.obtenerMontoTotal(tipo);

        StepVerifier.create(result)
                .expectNext(BigDecimal.ZERO)
                .verifyComplete();

        verify(reporteMontoRepository).findByTipo(tipo);
    }

}
