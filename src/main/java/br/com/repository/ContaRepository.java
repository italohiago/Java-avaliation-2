package br.com.repository;

import br.com.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByNomeResponsavel(String nomeResponsavel);

    // Método para buscar a conta pelo número da conta
    Conta findByNumeroConta(Long numeroConta);

}
