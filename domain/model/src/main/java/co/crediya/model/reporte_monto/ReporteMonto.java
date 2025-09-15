package co.crediya.model.reporte_monto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReporteMonto {

    private String tipo;
    private BigDecimal valor;

}
