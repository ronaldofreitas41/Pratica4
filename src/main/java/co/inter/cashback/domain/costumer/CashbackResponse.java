package co.inter.cashback.domain.costumer;

import co.inter.cashback.domain.purchase.Category;
import co.inter.cashback.domain.releases.Releases;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Serdeable
public record CashbackResponse(
   UUID customerId,
   UUID PurchaseId,
   Category category,
   BigDecimal balance,
   BigDecimal purchaseAmount,
   List<Releases> realeases
) {}
