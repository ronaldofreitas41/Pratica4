package co.inter.cashback.controller;

import co.inter.cashback.domain.purchase.PurchaseRequestDTO;
import co.inter.cashback.domain.purchase.PurchaseResponseDTO;
import co.inter.cashback.service.PurchaseService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.List;

/*
 *-------------------------------------------------------------------------*
 * @Controller marca a classe como um controller HTTP (bean do Micronaut). *
 * O valor passado (ex.: "/api/produtos") é o caminho base de todos os     *
 * endpoints da classe. O path de cada método (ex.: @Get("/{id}")) é       *
 * somado a ele, resultando em /api/produtos/{id}.                         *
 *-------------------------------------------------------------------------*
 * @ExecuteOn(TaskExecutors.BLOCKING) faz os métodos deste controller      *
 * rodarem no pool de threads para tarefas bloqueantes, e não no event     *
 * loop do Netty (padrão do Micronaut).                                    *
 *-------------------------------------------------------------------------*
 * Importância: o event loop usa poucas threads, que atendem todas as      *
 * requisições. Se uma delas ficar travada esperando banco (JDBC/JPA),     *
 * arquivo ou outra API, o servidor inteiro fica lento ou para.            *
 * Com o BLOCKING, essas chamadas rodam em outro pool e o event loop       *
 * continua livre para receber novas requisições.                          *
 *-------------------------------------------------------------------------*
 * Usar sempre que: O controller fizer chamadas bloqueantes (o caso comum  *
 * com repositories). Não é necessário em métodos reativos/assíncronos.    *
 *-------------------------------------------------------------------------*
 */

@Controller("/api/purchase")
@ExecuteOn(TaskExecutors.BLOCKING)
public class PurchaseController {

    /*
     *-----------------------------------------------------------------------------*
     * @Inject: o Micronaut cria o service e o injeta neste campo automaticamente. *
     * O campo não pode ser private nem final (o Micronaut evita usar reflection). *
     * Alternativa: receber o service pelo construtor (permite usar final).        *
     *-----------------------------------------------------------------------------*
     */

    @Inject
    PurchaseService purchaseService;

    /*
     *--------------------------------------------------------------------*
     * Metodo: POST                                                       *
     * Path: /api/purchase                                          *
     *--------------------------------------------------------------------*
     * Descrição: Criar um registro novo referente a Entidade Purchase.   *
     *--------------------------------------------------------------------*
     * Params:  PurchaseRequestDTO body                                   *
     * Returns: PurchaseResponseDTO                                       *
     *--------------------------------------------------------------------*
     */

    @Post
    public HttpResponse<PurchaseResponseDTO> createPurchase(@Valid @Body PurchaseRequestDTO body) {
        return HttpResponse.created(purchaseService.createPurchase(body));
    }

    /*
     *-----------------------------------------------------------------------------------*
     * Metodo: PUT                                                                       *
     * Path: /api/purchase/{id}                                                    *
     *-----------------------------------------------------------------------------------*
     * Descrição: Função para buscar no banco o registro que tenha o id passado no Path, *
     *            e editá-lo conforme o body passado.                                    *
     *-----------------------------------------------------------------------------------*
     * Params:  UUID id, PurchaseRequestDTO body                                     *
     * Returns: PurchaseResponseDTO                                                      *
     *-----------------------------------------------------------------------------------*
     */

}