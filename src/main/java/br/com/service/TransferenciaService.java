package br.com.service;

import br.com.model.Transferencia;
import br.com.repository.TransferenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Double calcularSaldoTotalConta(Long numeroConta) {
        List<Transferencia> transferencias = transferenciaRepository.findByContaId(numeroConta);
        Double saldoTotal = 0.0;

        for (Transferencia transferencia : transferencias) {
            if (transferencia.getTipo().equals("DEPOSITO") || transferencia.getTipo().equals("TRANSFERENCIA")) {
                saldoTotal += transferencia.getValor();
            } else {
                saldoTotal -= transferencia.getValor();
            }
        }

        return Math.round(saldoTotal * 100.0) / 100.0; // Arredonda para 2 casas decimais
    }

    // Método para calcular o saldo total da conta no período
    public Double calcularSaldoTotalContaNoPeriodo(Long numeroConta, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        List<Transferencia> transferencias = transferenciaRepository.findByContaIdAndDataTransferenciaBetween(numeroConta, dataInicial, dataFinal);
        Double saldoTotalNoPeriodo = 0.0;

        for (Transferencia transferencia : transferencias) {
            if (transferencia.getTipo().equals("DEPOSITO") || transferencia.getTipo().equals("TRANSFERENCIA")) {
                saldoTotalNoPeriodo += transferencia.getValor();
            } else {
                saldoTotalNoPeriodo -= transferencia.getValor();
            }
        }

        return Math.round(saldoTotalNoPeriodo * 100.0) / 100.0; // Arredonda para 2 casas decimais
    }
}
