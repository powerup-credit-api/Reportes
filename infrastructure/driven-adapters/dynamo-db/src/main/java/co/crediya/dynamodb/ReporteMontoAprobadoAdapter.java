package co.crediya.dynamodb;

import co.crediya.dynamodb.entities.ReporteMontoEntity;
import co.crediya.dynamodb.helper.TemplateAdapterOperations;

import co.crediya.model.reporte_monto.ReporteMonto;
import co.crediya.model.reporte_monto.gateways.ReporteMontoRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;


@Repository
public class ReporteMontoAprobadoAdapter extends TemplateAdapterOperations<ReporteMonto, String, ReporteMontoEntity> implements ReporteMontoRepository {

    public ReporteMontoAprobadoAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        super(connectionFactory, mapper, d -> mapper.map(d, ReporteMonto.class), "reportes");
    }

    @Override
    public Mono<ReporteMonto> findByTipo(String tipo) {
        return getById(tipo).cast(ReporteMonto.class);
    }

    @Override
    public Mono<ReporteMonto> save(ReporteMonto reporteMonto) {
        return super.save(reporteMonto).cast(ReporteMonto.class);
    }
}
