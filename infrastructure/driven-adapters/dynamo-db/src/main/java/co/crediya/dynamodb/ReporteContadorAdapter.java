package co.crediya.dynamodb;

import co.crediya.dynamodb.entities.ReporteContadorEntity;
import co.crediya.dynamodb.helper.TemplateAdapterOperations;
import co.crediya.model.reporte_contador.ReporteContador;
import co.crediya.model.reporte_contador.gateways.ReporteContadorRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;


@Repository
public class ReporteContadorAdapter extends TemplateAdapterOperations<ReporteContador, String, ReporteContadorEntity > implements ReporteContadorRepository {

    public ReporteContadorAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {

        super(connectionFactory, mapper, d -> mapper.map(d, ReporteContador.class), "reportes");
    }


    @Override
    public Mono<ReporteContador> findByTipo(String tipo) {
        return getById(tipo).cast(ReporteContador.class);
    }

    @Override
    public Mono<ReporteContador> save(ReporteContador reporteContador) {
        return super.save(reporteContador).cast(ReporteContador.class);
    }
}
