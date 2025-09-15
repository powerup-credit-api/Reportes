package co.crediya.usecase.reporte_monto;

import co.crediya.model.reporte_contador.ReporteContador;
import co.crediya.model.reporte_contador.gateways.ReporteContadorRepository;
import co.crediya.usecase.reporte_contador.ReporteContadorUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteContadorUseCaseTest {
    @Mock
    private ReporteContadorRepository reporteContadorRepository;

    @InjectMocks
    private ReporteContadorUseCase reporteContadorUseCase;

    @Test
    void incrementarContador_existente_incrementaValor() {

        String tipo = "CONTADOR_SOLICITUDES_APROBADAS";
        ReporteContador existente = new ReporteContador(tipo, 5L);
        ReporteContador actualizado = new ReporteContador(tipo, 6L);

        when(reporteContadorRepository.findByTipo(tipo)).thenReturn(Mono.just(existente));
        when(reporteContadorRepository.save(any())).thenReturn(Mono.just(actualizado));


        Mono<ReporteContador> result = reporteContadorUseCase.incrementarContador(tipo);

        StepVerifier.create(result)
                .expectNextMatches(r -> r.getValor() == 6L)
                .verifyComplete();

        verify(reporteContadorRepository).findByTipo(tipo);
        verify(reporteContadorRepository).save(any(ReporteContador.class));
    }

    @Test
    void incrementarContador_inexistente_creaNuevo() {

        String tipo = "NUEVO_CONTADOR";
        ReporteContador nuevo = new ReporteContador(tipo, 1L);

        when(reporteContadorRepository.findByTipo(tipo)).thenReturn(Mono.empty());
        when(reporteContadorRepository.save(any())).thenReturn(Mono.just(nuevo));


        Mono<ReporteContador> result = reporteContadorUseCase.incrementarContador(tipo);


        StepVerifier.create(result)
                .expectNextMatches(r -> r.getValor() == 1L && r.getTipo().equals(tipo))
                .verifyComplete();

        verify(reporteContadorRepository).findByTipo(tipo);
        verify(reporteContadorRepository).save(any(ReporteContador.class));
    }

    @Test
    void obtenerValorContador_existente_devuelveValor() {

        String tipo = "CONTADOR_EXISTENTE";
        ReporteContador existente = new ReporteContador(tipo, 10L);

        when(reporteContadorRepository.findByTipo(tipo)).thenReturn(Mono.just(existente));


        Mono<Long> result = reporteContadorUseCase.obtenerValorContador(tipo);


        StepVerifier.create(result)
                .expectNext(10L)
                .verifyComplete();

        verify(reporteContadorRepository).findByTipo(tipo);
    }

    @Test
    void obtenerValorContador_inexistente_devuelveCero() {

        String tipo = "INEXISTENTE";
        when(reporteContadorRepository.findByTipo(tipo)).thenReturn(Mono.empty());


        Mono<Long> result = reporteContadorUseCase.obtenerValorContador(tipo);


        StepVerifier.create(result)
                .expectNext(0L)
                .verifyComplete();

        verify(reporteContadorRepository).findByTipo(tipo);
    }
}
