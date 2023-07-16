package br.com.repository;

import br.com.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    List<Transferencia> findByContaId(Long contaId);

    List<Transferencia> findByDataTransferenciaBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);

    List<Transferencia> findByNomeOperadorTransacao(String nomeOperadorTransacao);

    List<Transferencia> findByContaIdAndDataTransferenciaBetweenAndNomeOperadorTransacao(
            Long contaId, LocalDateTime dataInicial, LocalDateTime dataFinal, String nomeOperadorTransacao
    );

}