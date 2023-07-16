package br.com.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conta")
    private Long id;

    @Column(name = "nome_responsavel")
    private String nomeResponsavel;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transferencia> transferencias;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public List<Transferencia> getTransferencias() {
        return transferencias;
    }

    public void setTransferencias(List<Transferencia> transferencias) {
        this.transferencias = transferencias;
    }

    // Método para calcular o saldo total da conta
    public BigDecimal getSaldoTotal() {
        BigDecimal saldoTotal = BigDecimal.ZERO;

        if (transferencias != null) {
            for (Transferencia transferencia : transferencias) {
                if (transferencia.getTipo().equals("DEPOSITO") || transferencia.getTipo().equals("TRANSFERENCIA")) {
                    saldoTotal = saldoTotal.add(transferencia.getValor());
                } else {
                    saldoTotal = saldoTotal.subtract(transferencia.getValor());
                }
            }
        }

        return saldoTotal;
    }
}
