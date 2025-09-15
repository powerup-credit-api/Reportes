package co.crediya.sqs.listener;

import co.crediya.sqs.config.SQSSenderProperties;
import co.crediya.sqs.dtos.SolicitudAprobadaMessage;
import co.crediya.usecase.reporte_contador.ReporteContadorUseCase;

import co.crediya.usecase.reporte_monto.ReporteMontoUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.math.BigDecimal;


@Component
@RequiredArgsConstructor
@Log
public class SqsListener {

    private final SqsAsyncClient sqsAsyncClient;
    private final SQSSenderProperties sqsProperties;
    private final ReporteContadorUseCase reporteContadorUseCase;
    private final ReporteMontoUseCase reporteMontoUseCase;
    private final ObjectMapper objectMapper;

    public void startListening() {
        pollQueue()
                .repeat()
                .subscribe();
    }

    private Mono<Void> pollQueue() {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(sqsProperties.solicitudAprobada())
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .build();

        return Mono.fromCompletionStage(() -> sqsAsyncClient.receiveMessage(request))
                .flatMapMany(response -> Flux.fromIterable(response.messages()))
                .switchIfEmpty(Mono.fromRunnable(() -> log.info("No hay mensajes en la cola")))
                .flatMap(this::processMessage)
                .then();
    }
    private Mono<Void> processMessage(Message message) {
        return Mono.fromCallable(() -> objectMapper.readValue(message.body(), SolicitudAprobadaMessage.class))
                .flatMap(event -> {
                    BigDecimal monto = event.monto();
                    log.info("Monto recibido desde el evento: " + monto);

                    return reporteContadorUseCase.incrementarContador("CONTADOR_SOLICITUDES_APROBADAS")
                            .then(reporteMontoUseCase.sumarMontoAprobado("MONTO_TOTAL_SOLICITUDES_APROBADAS", monto));
                })
                .then(Mono.defer(() -> {
                    DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                            .queueUrl(sqsProperties.solicitudAprobada())
                            .receiptHandle(message.receiptHandle())
                            .build();
                    return Mono.fromCompletionStage(() -> sqsAsyncClient.deleteMessage(deleteRequest)).then();
                }))
                .onErrorResume(e -> {
                    log.severe("Error procesando mensaje: " + e.getMessage());
                    return Mono.empty();
                });
    }

}
