package br.com.service;

import br.com.model.Transferencia;
import br.com.repository.TransferenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;

    @Autowired
    public TransferenciaService(TransferenciaRepository transferenciaRepository) {
        this.transferenciaRepository = transferenciaRepository;
    }

    // Métodos para buscar as transferências de acordo com os diferentes filtros:

    public List<Transferencia> buscarPorNumeroConta(Long numeroConta) {
        return transferenciaRepository.findByContaId(numeroConta);
    }

    public List<Transferencia> buscarPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return transferenciaRepository.findByDataTransferenciaBetween(dataInicial, dataFinal);
    }

    public List<Transferencia> buscarPorNomeOperador(String nomeOperador) {
        return transferenciaRepository.findByNomeOperadorTransacao(nomeOperador);
    }

    public List<Transferencia> buscarPorFiltros(Long numeroConta, LocalDateTime dataInicial,
                                                LocalDateTime dataFinal, String nomeOperador) {
        // Validação do número da conta
        if (numeroConta == null) {
            throw new IllegalArgumentException("Número da conta bancária não pode ser nulo.");
        }

        // Validação do período de tempo
        if (dataInicial != null && dataFinal != null && dataInicial.isAfter(dataFinal)) {
            throw new IllegalArgumentException("O período de tempo é inválido. A data inicial deve ser anterior ou igual à data final.");
        }

        // Validação do nome do operador
        if (nomeOperador != null) {
            List<Transferencia> transferenciasPorNomeOperador = transferenciaRepository.findByNomeOperadorTransacao(nomeOperador);
            if (transferenciasPorNomeOperador.isEmpty()) {
                throw new IllegalArgumentException("Não foram encontradas transferências para o operador informado.");
            }
        }

        return transferenciaRepository.findByContaIdAndDataTransferenciaBetweenAndNomeOperadorTransacao(
                numeroConta, dataInicial, dataFinal, nomeOperador
        );
    }

    // Método para buscar todas as transferências
    public List<Transferencia> buscarTodasTransferencias() {
        return transferenciaRepository.findAll();
    }

    // Método para calcular o saldo total da conta
    public BigDecimal calcularSaldoTotalConta(Long numeroConta) {
        List<Transferencia> transferencias = transferenciaRepository.findByContaId(numeroConta);
        BigDecimal saldoTotal = BigDecimal.ZERO;

        for (Transferencia transferencia : transferencias) {
            if (transferencia.getTipo().equals("DEPOSITO") || transferencia.getTipo().equals("TRANSFERENCIA")) {
                saldoTotal = saldoTotal.add(transferencia.getValor());
            } else {
                saldoTotal = saldoTotal.subtract(transferencia.getValor());
            }
        }

        return saldoTotal.setScale(2, RoundingMode.HALF_EVEN);
    }

    // Método para calcular o saldo total da conta no período
    public BigDecimal calcularSaldoTotalContaNoPeriodo(Long numeroConta, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        List<Transferencia> transferencias = transferenciaRepository.findByContaIdAndDataTransferenciaBetween(numeroConta, dataInicial, dataFinal);
        BigDecimal saldoTotalNoPeriodo = BigDecimal.ZERO;

        for (Transferencia transferencia : transferencias) {
            if (transferencia.getTipo().equals("DEPOSITO") || transferencia.getTipo().equals("TRANSFERENCIA")) {
                saldoTotalNoPeriodo = saldoTotalNoPeriodo.add(transferencia.getValor());
            } else {
                saldoTotalNoPeriodo = saldoTotalNoPeriodo.subtract(transferencia.getValor());
            }
        }

        return saldoTotalNoPeriodo.setScale(2, RoundingMode.HALF_EVEN);
    }
}
