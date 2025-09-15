package co.crediya.model.reporte_monto.gateways;

import co.crediya.model.reporte_monto.ReporteMonto;
import reactor.core.publisher.Mono;

public interface ReporteMontoRepository {
    Mono<ReporteMonto> findByTipo(String tipo);
    Mono<ReporteMonto> save(ReporteMonto reporteMonto);
}
