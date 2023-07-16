package br.com.controller;

import br.com.model.Transferencia;
import br.com.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    @Autowired
    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    // Rota para buscar todas as transferências
    @GetMapping
    public ResponseEntity<List<Transferencia>> buscarTodasTransferencias() {
        List<Transferencia> transferencias = transferenciaService.buscarTodasTransferencias();
        return ResponseEntity.ok(transferencias);
    }

    // Rota para buscar transferências com filtros
    @GetMapping("/buscar")
    public ResponseEntity<List<Transferencia>> buscarTransferenciasComFiltros(
            @RequestParam(required = false) Long numeroConta,
            @RequestParam(required = false) LocalDateTime dataInicial,
            @RequestParam(required = false) LocalDateTime dataFinal,
            @RequestParam(required = false) String nomeOperador
    ) {
        List<Transferencia> transferencias = transferenciaService.buscarPorFiltros(numeroConta, dataInicial, dataFinal, nomeOperador);
        return ResponseEntity.ok(transferencias);
    }

    // Rota para buscar o saldo total de uma conta
    @GetMapping("/saldo/{numeroConta}")
    public ResponseEntity<BigDecimal> calcularSaldoTotalConta(@PathVariable Long numeroConta) {
        BigDecimal saldoTotal = transferenciaService.calcularSaldoTotalConta(numeroConta);
        return ResponseEntity.ok(saldoTotal);
    }

    // Rota para buscar o saldo total de uma conta no período
    @GetMapping("/saldo/{numeroConta}/periodo")
    public ResponseEntity<BigDecimal> calcularSaldoTotalContaNoPeriodo(
            @PathVariable Long numeroConta,
            @RequestParam(required = false) LocalDateTime dataInicial,
            @RequestParam(required = false) LocalDateTime dataFinal
    ) {
        BigDecimal saldoTotalNoPeriodo = transferenciaService.calcularSaldoTotalContaNoPeriodo(numeroConta, dataInicial, dataFinal);
        return ResponseEntity.ok(saldoTotalNoPeriodo);
    }
}
