package co.crediya.dynamodb.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;


@DynamoDbBean
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteMontoEntity {

    private String tipo;
    private BigDecimal valor;


    @DynamoDbPartitionKey
    @DynamoDbAttribute("tipo")
    public String getTipo() {
        return tipo;
    }



    @DynamoDbAttribute("valor")
    public BigDecimal getValor() {
        return valor;
    }

}
