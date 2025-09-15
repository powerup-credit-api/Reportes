package co.crediya.usecase.reporte_contador;

import co.crediya.model.reporte_contador.ReporteContador;
import co.crediya.model.reporte_contador.gateways.ReporteContadorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log
public class ReporteContadorUseCase {

    private final ReporteContadorRepository reporteContadorRepository;

    public Mono<ReporteContador> incrementarContador(String tipo) {
        return reporteContadorRepository.findByTipo(tipo)
                .switchIfEmpty(Mono.just(new ReporteContador(tipo, 0L)))
                .flatMap(reporte -> {
                    reporte.setValor(reporte.getValor() + 1);
                    return reporteContadorRepository.save(reporte);
                })
                .doOnError(e -> log.severe("Error incrementando contador: " + e.getMessage()))
                .doOnSuccess(r -> log.info("Contador incrementado correctamente"));
    }

    public Mono<Long> obtenerValorContador(String tipo) {
        return reporteContadorRepository.findByTipo(tipo)
                .map(ReporteContador::getValor)
                .defaultIfEmpty(0L);
    }
}
