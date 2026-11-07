package co.inter.cashback.service;

import co.inter.cashback.domain.costumer.CashbackResponse;
import co.inter.cashback.domain.costumer.Customer;
import co.inter.cashback.domain.costumer.CustomerRequestDTO;
import co.inter.cashback.domain.costumer.CustomerResponseDTO;
import co.inter.cashback.domain.releases.Releases;
import co.inter.cashback.repository.CustomerRepository;
import co.inter.cashback.repository.ReleasesRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/*
 *-----------------------------------------------------------------------*
 * @Singleton registra a classe como um bean do Micronaut com uma única  *
 * instância compartilhada por toda a aplicação. É o que permite injetar *
 * este service no controller (via @Inject ou construtor). Por ser       *
 * compartilhado, não guarde estado de requisição em campos da classe.   *
 *-----------------------------------------------------------------------*
 */
@Singleton
public class CustomerService {

    /*
     *-------------------------------------------------------------------------*
     * @Inject: o Micronaut cria o repository e o injeta neste campo.          *
     * O campo não pode ser private nem final (o Micronaut evita reflection).  *
     * Alternativa: receber o repository pelo construtor (permite usar final). *
     *-------------------------------------------------------------------------*
     */
    @Inject
    CustomerRepository customerRepository;

    @Inject
    ReleasesRepository releasesRepository;

    /*
     *--------------------------------------------------------------------*
     * Metodo: getCostumerById                                            *
     *--------------------------------------------------------------------*
     * Descrição: Função para buscar uma Costumer pelo seu id se ele      *
     *            existir.                                                *
     *--------------------------------------------------------------------*
     * Params:  UUID id                                                   *
     * Returns: CostumerResponseDTO                                       *
     *--------------------------------------------------------------------*
     */

    public CustomerResponseDTO getCostumerById(UUID id) {

        // Busca a entidade no banco de dados via id e caso a mesma não seja encontrada
        // retornará um Status 404 com a mensagem de dados nao encontrado para aquele id
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND,
                        "Dado não encontrado para o id: " + id));

        //Retorna a conversão da Entity em um ResponseDTO
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getBalance()
        );
    }

    /*
     *--------------------------------------------------------------------*
     * Metodo: getAllCustomerData                                         *
     *--------------------------------------------------------------------*
     * Descrição: Função para buscar uma Costumer pelo seu id se ele      *
     *            existir.                                                *
     *--------------------------------------------------------------------*
     * Params:  UUID id                                                   *
     * Returns: CostumerResponseDTO                                       *
     *--------------------------------------------------------------------*
     */

    public CashbackResponse getAllCustomerReleases(UUID id) {

        // Busca a entidade no banco de dados via id e caso a mesma não seja encontrada
        // retornará um Status 404 com a mensagem de dados nao encontrado para aquele id
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND,
                        "Dado não encontrado para o id: " + id));

        List<Releases> releases = releasesRepository.getAllCostuerReleases(customer.getId());

        //Retorna a conversão da Entity em um ResponseDTO
        return new CashbackResponse(
                customer.getId(),
                customer.getBalance(),
                 releases
                );
    }

    /*
     *--------------------------------------------------------------------*
     * Metodo: getAllCostumer                                             *
     *--------------------------------------------------------------------*
     * Descrição: Função para buscar uma lista com todas as Costumers     *
     *            presentes no banco de dados.                            *
     *--------------------------------------------------------------------*
     * Params:  None                                                      *
     * Returns: List<CostumerResponseDTO>                                 *
     *--------------------------------------------------------------------*
     */

    public List<CustomerResponseDTO> getAllCostumer() {

        //Busca todas as Entities presentes no banco
        List<Customer> customers = customerRepository.findAll();

        //Retorna a conversão da Lista de Entities em uma Lista de ResponseDTO
        return customers.stream().map(
                customer -> new CustomerResponseDTO(
                        customer.getId(),
                        customer.getName(),
                        customer.getBalance()
                )).toList();
    }


    /*
     *--------------------------------------------------------------------*
     * Metodo: createCostumer                                             *
     *--------------------------------------------------------------------*
     * Descrição: Função para criar uma Costumer com os dados passados    *
     *            via body da requisição                                  *
     *--------------------------------------------------------------------*
     * Params:  CostumerRequestDTO body                                   *
     * Returns: CostumerResponseDTO                                       *
     *--------------------------------------------------------------------*
     */

    public CustomerResponseDTO createCostumer(CustomerRequestDTO body) {

        //Converte o Body que é um RequestDTO em uma Entity
        Customer customer = new Customer();

        //Sets com os dados da Entidade Costumer
        customer.setName(body.name());
        customer.setBalance(BigDecimal.ZERO);


        // Salvando Entity no banco
        customerRepository.save(customer);

        //Retorna a conversão da RequestDTO em um ResponseDTO
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getBalance()
        );
    }

    /*
     *--------------------------------------------------------------------*
     * Metodo: updateCostumer                                             *
     *--------------------------------------------------------------------*
     * Descrição: Função para buscar uma Costumer pelo seu id se ele      *
     *            existir. E se ela existir editar com os dados passados  *
     *            via body da requisição                                  *
     *--------------------------------------------------------------------*
     * Params:  UUID id, CostumerRequestDTO body                      *
     * Returns: CostumerResponseDTO                                       *
     *--------------------------------------------------------------------*
     */

    public CustomerResponseDTO updateCostumer(UUID id, CustomerRequestDTO body) {

        // Busca a entidade no banco de dados via id e caso a mesma não seja encontrada
        // retornará um Status 404 com a mensagem de dados nao encontrado para aquele id
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND,
                        "Dado não encontrado para o id: " + id));

        customer.setBalance(body.balance());
        customer.setName(body.name());

        // Atualizando Entity referente ao id passado no banco
        customerRepository.update(customer);

        //Retorna a conversão da RequestDTO em um ResponseDTO
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getBalance()
        );
    }

    /*
     *--------------------------------------------------------------------*
     * Metodo: deleteCostumer                                             *
     *--------------------------------------------------------------------*
     * Descrição: Função para buscar uma Costumer pelo seu id se ele      *
     *            existir. E se ela existir ela será excluída             *
     *--------------------------------------------------------------------*
     * Params:  UUID id                                               *
     * Returns: None                                                      *
     *--------------------------------------------------------------------*
     */

    public void deleteCostumer(UUID id) {

        // Busca a entidade no banco de dados via id e caso a mesma não seja encontrada
        // retornará um Status 404 com a mensagem de dados nao encontrado para aquele id
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND,
                        "Dado não encontrado para o id: " + id));

        // Excluindo Entity referente ao id passado no banco se ela existir
        customerRepository.delete(customer);
    }
}