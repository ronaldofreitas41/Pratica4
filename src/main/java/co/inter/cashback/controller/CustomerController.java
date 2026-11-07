package co.inter.cashback.controller;

import co.inter.cashback.domain.costumer.CustomerRequestDTO;
import co.inter.cashback.domain.costumer.CustomerResponseDTO;
import co.inter.cashback.service.CustomerService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.List;
import java.util.UUID;

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

@Controller("/api/costumers")
@ExecuteOn(TaskExecutors.BLOCKING)
public class CustomerController {

    /*
     *-----------------------------------------------------------------------------*
     * @Inject: o Micronaut cria o service e o injeta neste campo automaticamente. *
     * O campo não pode ser private nem final (o Micronaut evita usar reflection). *
     * Alternativa: receber o service pelo construtor (permite usar final).        *
     *-----------------------------------------------------------------------------*
     */

    @Inject
    CustomerService costumerService;

    /*
     *----------------------------------------------------------------------*
     * Metodo: GET                                                          *
     * Path: /api/costumer                                            *
     *----------------------------------------------------------------------*
     * Descrição: Função para buscar todos os Costumers presentes no banco. *
     *----------------------------------------------------------------------*
     * Params:  None                                                        *
     * Returns: List<CostumerResponseDTO>                                   *
     *----------------------------------------------------------------------*
     */

    @Get
    public HttpResponse<List<CustomerResponseDTO>> getAllCostumer() {
        return HttpResponse.ok(costumerService.getAllCostumer());
    }

    /*
     *-----------------------------------------------------------------------------------*
     * Metodo: GET                                                                       *
     * Path: /api/costumer/{id}                                                    *
     *-----------------------------------------------------------------------------------*
     * Descrição: Função para buscar no banco o registro que tenha o id passado no Path. *
     *-----------------------------------------------------------------------------------*
     * Params:  UUID id                                                              *
     * Returns: CostumerResponseDTO                                                      *
     *-----------------------------------------------------------------------------------*
     */

    @Get("/{id}")
    public HttpResponse<CustomerResponseDTO> getCostumerById(@PathVariable UUID id) {
        return HttpResponse.ok(costumerService.getCostumerById(id));
    }

    /*
     *-----------------------------------------------------------------------------------*
     * Metodo: GET                                                                       *
     * Path: /api/costumer/{id}                                                    *
     *-----------------------------------------------------------------------------------*
     * Descrição: Função para buscar no banco o registro que tenha o id passado no Path. *
     *-----------------------------------------------------------------------------------*
     * Params:  UUID id                                                              *
     * Returns: CostumerResponseDTO                                                      *
     *-----------------------------------------------------------------------------------*
     */
/*
    @Get("/{id}/cashback")
    public HttpResponse<CustomerResponseDTO> getCashbacks(@PathVariable UUID id) {
        return HttpResponse.ok(costumerService.getCashbacksById(id));
    }
*/
    /*
     *--------------------------------------------------------------------*
     * Metodo: POST                                                       *
     * Path: /api/costumer                                          *
     *--------------------------------------------------------------------*
     * Descrição: Criar um registro novo referente a Entidade Costumer.   *
     *--------------------------------------------------------------------*
     * Params:  CostumerRequestDTO body                                   *
     * Returns: CostumerResponseDTO                                       *
     *--------------------------------------------------------------------*
     */

    @Post
    public HttpResponse<CustomerResponseDTO> createCostumer(@Valid @Body CustomerRequestDTO body) {
        return HttpResponse.created(costumerService.createCostumer(body));
    }

    /*
     *-----------------------------------------------------------------------------------*
     * Metodo: PUT                                                                       *
     * Path: /api/costumer/{id}                                                    *
     *-----------------------------------------------------------------------------------*
     * Descrição: Função para buscar no banco o registro que tenha o id passado no Path, *
     *            e editá-lo conforme o body passado.                                    *
     *-----------------------------------------------------------------------------------*
     * Params:  UUID id, CostumerRequestDTO body                                     *
     * Returns: CostumerResponseDTO                                                      *
     *-----------------------------------------------------------------------------------*
     */
    @Put("/{id}")
    public HttpResponse<CustomerResponseDTO> updateCostumer(@PathVariable UUID id, @Valid @Body CustomerRequestDTO body) {
        return HttpResponse.ok(costumerService.updateCostumer(id, body));
    }

    /*
     *-----------------------------------------------------------------------------------*
     * Metodo: DELETE                                                                    *
     * Path: /api/costumer/{id}                                                    *
     *-----------------------------------------------------------------------------------*
     * Descrição: Função para buscar no banco o registro que tenha o id passado no Path, *
     *            e excluir o mesmo                                                      *
     *-----------------------------------------------------------------------------------*
     * Params:  UUID id                                                              *
     * Returns: apenas o status                                                          *
     *-----------------------------------------------------------------------------------*
     */
    @Delete("/{id}")
    public HttpResponse<Void> deleteCostumer(@PathVariable UUID id) {
        costumerService.deleteCostumer(id);
        return HttpResponse.noContent();
    }
}
