package br.com.service;

import br.com.model.Conta;
import br.com.model.Transferencia;
import br.com.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    @Autowired
    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    private static double applyAsDouble(Transferencia transferencia) {
        if (transferencia.getTipo().equals("DEPOSITO") || transferencia.getTipo().equals("TRANSFERENCIA")) {
            return transferencia.getValor();
        } else {
            return -transferencia.getValor();
        }
    }

    // Métodos para buscar informações sobre as contas:

    public List<Conta> buscarTodasContas() {
        return contaRepository.findAll();
    }

    public Optional<Conta> buscarContaPorId(Long id) {
        return contaRepository.findById(id);
    }

    public Optional<Conta> buscarContaPorNomeResponsavel(String nomeResponsavel) {
        return contaRepository.findByNomeResponsavel(nomeResponsavel);
    }

    // Lógica para calcular os saldos:

    public Double calcularSaldoTotalConta(Long idConta) {
        Optional<Conta> contaOptional = contaRepository.findById(idConta);
        if (contaOptional.isPresent()) {
            Conta conta = contaOptional.get();
            Double saldoTotal = conta.getTransferencias().stream()
                    .mapToDouble(ContaService::applyAsDouble)
                    .sum();
            return Math.round(saldoTotal * 100.0) / 100.0; // Arredonda para 2 casas decimais
        } else {
            throw new IllegalArgumentException("Conta não encontrada.");
        }
    }
}
