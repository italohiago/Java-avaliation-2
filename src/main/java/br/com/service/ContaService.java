package br.com.service;

import br.com.model.Conta;
import br.com.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    @Autowired
    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    // Método para buscar a conta pelo número da conta
    public Conta buscarPorNumeroConta(Long numeroConta) {
        return contaRepository.findByNumeroConta(numeroConta);
    }

    // Método para calcular o saldo total da conta
    public BigDecimal calcularSaldoTotalConta(Long numeroConta) {
        Conta conta = contaRepository.findByNumeroConta(numeroConta);
        BigDecimal saldoTotalConta = conta.getSaldoTotal();

        if (saldoTotalConta == null) {
            saldoTotalConta = BigDecimal.ZERO;
        }

        return saldoTotalConta.setScale(2, RoundingMode.HALF_EVEN);
    }
}
