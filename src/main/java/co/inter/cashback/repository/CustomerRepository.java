package co.inter.cashback.repository;

import co.inter.cashback.domain.costumer.Customer;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.annotation.Repository;

import java.math.BigDecimal;
import java.util.UUID;

/*
 *--------------------------------------------------------------------------*
 * @Repository: marca a interface como um repository do Micronaut Data.     *
 * A implementação é gerada em tempo de compilação, então basta declarar    *
 * a interface. Os métodos do JpaRepository (save, findById, findAll,       *
 * update, delete...) já vêm prontos, e métodos novos são criados pelo nome *
 * (ex.: findByNome(String nome) gera a consulta sozinho).                  *
 *--------------------------------------------------------------------------*
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query("UPDATE Customer c SET c.balance + :amount"+
            "WHERE c.id = :customerID")
    BigDecimal cashbackCalculate(UUID customerId, BigDecimal amount);
}
