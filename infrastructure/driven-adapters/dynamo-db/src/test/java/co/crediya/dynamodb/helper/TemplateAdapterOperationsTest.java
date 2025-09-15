package co.crediya.dynamodb.helper;

import co.crediya.dynamodb.ReporteContadorAdapter;
import co.crediya.dynamodb.entities.ReporteContadorEntity;
import co.crediya.model.reporte_contador.ReporteContador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
class ReporteContadorAdapterTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private DynamoDbAsyncTable<ReporteContadorEntity> customerTable;

    private ReporteContadorEntity reporteContadorEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(dynamoDbEnhancedAsyncClient.table("reportes", TableSchema.fromBean(ReporteContadorEntity.class)))
                .thenReturn(customerTable);

        reporteContadorEntity = new ReporteContadorEntity("CONTADOR_SOLICITUDES_APROBADAS", 1L);
    }

    @Test
    void modelEntityPropertiesMustNotBeNull() {
        ReporteContadorEntity underTest = new ReporteContadorEntity("tipo", 1L);

        assertNotNull(underTest.getTipo());
        assertNotNull(underTest.getValor());
    }

    @Test
    void testSave() {
        when(customerTable.putItem(reporteContadorEntity))
                .thenReturn(CompletableFuture.runAsync(() -> {}));

        ReporteContador domain = new ReporteContador("tipo", 1L);
        when(mapper.map(reporteContadorEntity, ReporteContador.class)).thenReturn(domain);
        when(mapper.map(domain, ReporteContadorEntity.class)).thenReturn(reporteContadorEntity);

        ReporteContadorAdapter adapter =
                new ReporteContadorAdapter(dynamoDbEnhancedAsyncClient, mapper);

        StepVerifier.create(adapter.save(domain))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void testGetById() {
        String tipo = "tipo";

        when(customerTable.getItem(
                Key.builder().partitionValue(tipo).build()))
                .thenReturn(CompletableFuture.completedFuture(reporteContadorEntity));

        ReporteContador domain = new ReporteContador("tipo", 1L);
        when(mapper.map(reporteContadorEntity, ReporteContador.class)).thenReturn(domain);

        ReporteContadorAdapter adapter =
                new ReporteContadorAdapter(dynamoDbEnhancedAsyncClient, mapper);

        StepVerifier.create(adapter.getById("tipo"))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void testDelete() {
        ReporteContador domain = new ReporteContador("tipo", 1L);
        when(mapper.map(domain, ReporteContadorEntity.class)).thenReturn(reporteContadorEntity);
        when(mapper.map(reporteContadorEntity, ReporteContador.class)).thenReturn(domain);

        when(customerTable.deleteItem(reporteContadorEntity))
                .thenReturn(CompletableFuture.completedFuture(reporteContadorEntity));

        ReporteContadorAdapter adapter =
                new ReporteContadorAdapter(dynamoDbEnhancedAsyncClient, mapper);

        StepVerifier.create(adapter.delete(domain))
                .expectNext(domain)
                .verifyComplete();
    }
}
