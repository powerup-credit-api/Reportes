package co.crediya.usecase.reporte_monto;

import co.crediya.model.reporte_contador.ReporteContador;
import co.crediya.model.reporte_contador.gateways.ReporteContadorRepository;
import co.crediya.model.reporte_monto.ReporteMonto;
import co.crediya.model.reporte_monto.gateways.ReporteMontoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Log
public class ReporteMontoUseCase {
    private final ReporteMontoRepository reporteMontoRepository;

    public Mono<ReporteMonto> sumarMontoAprobado(String tipo, BigDecimal monto) {
        return reporteMontoRepository.findByTipo(tipo)
                .switchIfEmpty(Mono.just(new ReporteMonto(tipo, BigDecimal.ZERO)))
                .flatMap(reporte -> {
                    BigDecimal valorActual = reporte.getValor();
                    reporte.setValor(valorActual.add(monto));

                    return reporteMontoRepository.save(reporte);
                })
                .doOnError(e -> log.severe("Error al sumar monto aprobado: " + e.getMessage()))
                .doOnSuccess(r -> log.info("Monto aprobado sumado correctamente"));

    }

    public Mono<BigDecimal> obtenerMontoTotal(String tipo) {
        return reporteMontoRepository.findByTipo(tipo)
                .map(ReporteMonto::getValor)
                .defaultIfEmpty(BigDecimal.ZERO);
    }

}
