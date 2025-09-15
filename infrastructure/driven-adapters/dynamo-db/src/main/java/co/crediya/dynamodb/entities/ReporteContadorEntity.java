package co.crediya.dynamodb.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;


@DynamoDbBean
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteContadorEntity {

    private String tipo;
    private Long valor;


    @DynamoDbPartitionKey
    @DynamoDbAttribute("tipo")
    public String getTipo() {
        return tipo;
    }



    @DynamoDbAttribute("valor")
    public Long getValor() {
        return valor;
    }

}
