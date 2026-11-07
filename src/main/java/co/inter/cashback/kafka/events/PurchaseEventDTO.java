package co.inter.cashback.kafka.events;

import co.inter.cashback.domain.purchase.Category;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.util.UUID;

@Serdeable
public record PurchaseEventDTO(
      UUID purchaseID,
      UUID costomerID,
      BigDecimal PurchaseAmount,
      Category category,
      String status
) {}
