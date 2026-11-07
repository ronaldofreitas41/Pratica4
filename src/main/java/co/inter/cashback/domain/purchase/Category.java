package co.inter.cashback.domain.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {

    FOOD("Food"),
    TRAVEL("Travel"),
    OTHER("Other");

    private String descricao;
}
