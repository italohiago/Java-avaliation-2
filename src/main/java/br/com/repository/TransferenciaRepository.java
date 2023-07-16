package br.com.repository;

import br.com.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    // Método para buscar transferências por número da conta
    List<Transferencia> findByContaId(Long numeroConta);

    // Método para buscar transferências por período de tempo
    List<Transferencia> findByDataTransferenciaBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);

    // Método para buscar transferências por nome do operador
    List<Transferencia> findByNomeOperadorTransacao(String nomeOperador);

    // Método para buscar transferências com filtros (número da conta, período e nome do operador)
    @Query("SELECT t FROM Transferencia t " +
            "WHERE (:numeroConta IS NULL OR t.conta.id = :numeroConta) " +
            "AND (:dataInicial IS NULL OR t.dataTransferencia >= :dataInicial) " +
            "AND (:dataFinal IS NULL OR t.dataTransferencia <= :dataFinal) " +
            "AND (:nomeOperador IS NULL OR t.nomeOperadorTransacao = :nomeOperador)")
    List<Transferencia> findByContaIdAndDataTransferenciaBetweenAndNomeOperadorTransacao(
            Long numeroConta, LocalDateTime dataInicial, LocalDateTime dataFinal, String nomeOperador
    );

    // Método para buscar transferências por número da conta e período de tempo
    List<Transferencia> findByContaIdAndDataTransferenciaBetween(Long numeroConta, LocalDateTime dataInicial, LocalDateTime dataFinal);
}
