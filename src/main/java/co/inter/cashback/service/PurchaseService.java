package co.inter.cashback.service;

import co.inter.cashback.domain.purchase.Purchase;
import co.inter.cashback.domain.purchase.PurchaseRequestDTO;
import co.inter.cashback.domain.purchase.PurchaseResponseDTO;
import co.inter.cashback.kafka.events.PurchaseEventDTO;
import co.inter.cashback.kafka.producer.PurchaseProducer;
import co.inter.cashback.repository.PurchaseRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/*
 *-----------------------------------------------------------------------*
 * @Singleton registra a classe como um bean do Micronaut com uma única  *
 * instância compartilhada por toda a aplicação. É o que permite injetar *
 * este service no controller (via @Inject ou construtor). Por ser       *
 * compartilhado, não guarde estado de requisição em campos da classe.   *
 *-----------------------------------------------------------------------*
 */
@Singleton
public class PurchaseService {

    /*
     *-------------------------------------------------------------------------*
     * @Inject: o Micronaut cria o repository e o injeta neste campo.          *
     * O campo não pode ser private nem final (o Micronaut evita reflection).  *
     * Alternativa: receber o repository pelo construtor (permite usar final). *
     *-------------------------------------------------------------------------*
     */
    @Inject
    PurchaseRepository purchaseRepository;

    @Inject
    PurchaseProducer purchaseProducer;

    /*
     *--------------------------------------------------------------------*
     * Metodo: createPurchase                                             *
     *--------------------------------------------------------------------*
     * Descrição: Função para criar uma Purchase com os dados passados    *
     *            via body da requisição                                  *
     *--------------------------------------------------------------------*
     * Params:  PurchaseRequestDTO body                                   *
     * Returns: PurchaseResponseDTO                                       *
     *--------------------------------------------------------------------*
     */

    public PurchaseResponseDTO createPurchase(PurchaseRequestDTO body) {

        //Converte o Body que é um RequestDTO em uma Entity
        Purchase purchase = new Purchase();


        PurchaseEventDTO event = new PurchaseEventDTO(
                purchase.getId(),
                body.costumerId(),
                body.ammount(),
                body.category(),
                "PENDING"
        );

        purchaseProducer.send(body.costumerId(),event);

        //Retorna a conversão da RequestDTO em um ResponseDTO
        return new PurchaseResponseDTO(
                purchase.getId(),
                "RECIEVED"
        );
    }
}
