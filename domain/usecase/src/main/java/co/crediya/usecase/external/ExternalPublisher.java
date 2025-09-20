package co.crediya.usecase.external;

import reactor.core.publisher.Mono;

public interface ExternalPublisher {

    Mono<String> sendReporteDiario(Object payload);

}
