package co.crediya.model.reporte_contador.gateways;

import co.crediya.model.reporte_contador.ReporteContador;
import reactor.core.publisher.Mono;

public interface ReporteContadorRepository {

    Mono<ReporteContador> findByTipo(String tipo);
    Mono<ReporteContador> save(ReporteContador reporteContador);
}
