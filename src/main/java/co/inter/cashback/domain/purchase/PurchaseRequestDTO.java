package co.inter.cashback.domain.purchase;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

/*
 *-------------------------------------------------------------------------*
 * @Serdeable: o Micronaut Serialization gera, em tempo de compilação, o   *
 * código que converte JSON em objeto (e vice-versa). Sem ele, o Micronaut *
 * não consegue ler o body da requisição nem devolver este DTO como JSON.  *
 *-------------------------------------------------------------------------*
 */
@Serdeable
public record PurchaseRequestDTO(

        @NotNull(message = "Favor informar o id do cliente")
        UUID costumerId,


        Category category,

        @NotNull(message = "Favor informar o valor")
        @Positive(message = "Informe um valor maior que 0")
        @Digits(message ="Informe um numero com no maximo 2 casas decimais",integer = 20,fraction = 2)
        BigDecimal ammount
        ) {}
