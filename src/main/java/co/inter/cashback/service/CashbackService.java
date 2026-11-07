package co.inter.cashback.service;

import co.inter.cashback.domain.costumer.Customer;
import co.inter.cashback.domain.releases.Releases;
import co.inter.cashback.kafka.events.PurchaseEventDTO;
import co.inter.cashback.repository.CustomerRepository;
import co.inter.cashback.repository.PurchaseRepository;
import co.inter.cashback.repository.ReleasesRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Singleton
public class CashbackService {

    @Inject
    CustomerRepository customerRepository;

    @Inject
    ReleasesRepository releasesRepository;

    @Transactional
    public void process(PurchaseEventDTO event) {

        //Validando se existe o cliente
        Customer customer = customerRepository.findById(event.costomerID())
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND
                        , "Cliente nao encontrado na base de dados"));

        //Verificando se a compra esta pendente
        if (!event.status().equals("PENDING")) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Não é possivel seguir com essa compra");
        }

        //Verifica qual a categoria para calcular o cashback
        BigDecimal cashback = BigDecimal.ZERO;
        switch (event.category().getDescricao()) {
            case "FOOD":
                cashback = event.PurchaseAmount().multiply(BigDecimal.valueOf(0.02));
                cashback.setScale(2, RoundingMode.HALF_UP);
                break;
            case "OTHER":
                cashback = event.PurchaseAmount().multiply(BigDecimal.valueOf(0.01));
                cashback.setScale(2, RoundingMode.HALF_UP);
                break;
            case "TRAVEL":
                cashback = event.PurchaseAmount().multiply(BigDecimal.valueOf(0.03));
                cashback.setScale(2, RoundingMode.HALF_UP);
                break;
        }

        customerRepository.cashbackCalculate(event.costomerID(),cashback);
        Releases realease = new Releases();
        realease.setCashbackValue(cashback);
        realease.setPurchaseId(event.purchaseID());
        realease.setCustumerId(event.costomerID());
        releasesRepository.save(realease);
    }
}
