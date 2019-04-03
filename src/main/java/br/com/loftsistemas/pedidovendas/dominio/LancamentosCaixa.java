package br.com.loftsistemas.pedidovendas.dominio;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
public class LancamentosCaixa extends GenericDomain {

	@Column()
	@Temporal(TemporalType.DATE)
	private Date data;
	
	@Column(nullable=false , precision = 7 , scale=2)
    private BigDecimal valor;
	 
	
	@Column()
	private String historicoLancamento;
	
	@Column()
	private String tipo;
	
	@Column()
	private String formapgto;
	
	@ManyToOne
	@JoinColumn(nullable=true)
	private Caixa caixa;
	
	@ManyToOne
	@JoinColumn()
	private Orcamento orcamento;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
 

	public String getHistoricoLancamento() {
		return historicoLancamento;
	}

	public void setHistoricoLancamento(String historicoLancamento) {
		this.historicoLancamento = historicoLancamento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFormapgto() {
		return formapgto;
	}

	public void setFormapgto(String formapgto) {
		this.formapgto = formapgto;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}
	
	
	
	
	
}
