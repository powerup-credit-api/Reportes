package co.crediya.api.rest;

import co.crediya.api.config.ReportePath;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {


    private final ReportePath reportePath;

    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/reportes/total",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "obtenerTotalReportes",
                            summary = "Obtener total de solicitudes aprobadas",
                            description = """
        Retorna el valor total del contador de solicitudes aprobadas.
        No requiere parametros ni cuerpo en la peticion.
        """,
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Total obtenido correctamente",
                                            content = @Content(schema = @Schema(type = "integer", example = "42"))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Ocurrio un error inesperado"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/reportes/monto",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "obtenerMontoTotal",
                            summary = "Obtener monto total aprobado",
                            description = """
        Retorna el monto acumulado de todas las solicitudes aprobadas.
        No requiere parametros ni cuerpo en la peticion.
        """,
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Monto total obtenido correctamente",
                                            content = @Content(schema = @Schema(type = "number", format = "bigdecimal", example = "12345.67"))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Ocurrio un error inesperado"
                                    )
                            }
                    )
            )
    })

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET(reportePath.getReportesContador()), handler::listenObtenerContador)
                .andRoute(GET(reportePath.getReportesMonto()), handler::listenObtenerMontoTotal);

    }
}
