package co.crediya.sqs.sender;

import co.crediya.sqs.config.SQSSenderProperties;
import co.crediya.usecase.external.ExternalPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements ExternalPublisher {

    private final SqsAsyncClient client;
    private final ObjectMapper objectMapper;
    private final SQSSenderProperties properties;

    @Override
    public Mono<String> sendReporteDiario(Object payload) {
        return send(payload, properties.reporteDiario());
    }


    public Mono<String> send(Object payload, String queueUrl) {
        return Mono.fromCallable(() -> {
                    String messageBody = objectMapper.writeValueAsString(payload);
                    return buildRequest(messageBody, queueUrl);
                })
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {} to {}", response.messageId(), queueUrl))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message, String queueUrl) {
        return SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();
    }
}
