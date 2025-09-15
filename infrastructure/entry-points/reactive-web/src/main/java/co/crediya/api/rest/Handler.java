package co.crediya.api.rest;

import co.crediya.usecase.reporte_contador.ReporteContadorUseCase;
import co.crediya.usecase.reporte_monto.ReporteMontoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ReporteContadorUseCase reporteContadorUseCase;
    private final ReporteMontoUseCase reporteMontoUseCase;


    public Mono<ServerResponse> listenObtenerContador(ServerRequest serverRequest) {
        return reporteContadorUseCase.obtenerValorContador("CONTADOR_SOLICITUDES_APROBADAS")
                .flatMap(valor -> ServerResponse.ok().bodyValue(valor))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listenObtenerMontoTotal(ServerRequest serverRequest) {

        return reporteMontoUseCase.obtenerMontoTotal("MONTO_TOTAL_SOLICITUDES_APROBADAS")
                .flatMap(valor -> ServerResponse.ok().bodyValue(valor))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
