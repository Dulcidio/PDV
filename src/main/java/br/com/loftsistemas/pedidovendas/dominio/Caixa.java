package br.com.loftsistemas.pedidovendas.dominio;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@SuppressWarnings("serial")
@Entity
public class Caixa extends GenericDomain{

	@Column(nullable = false)
	private String descricao;
	@Column(nullable=false,precision=7,scale=2)
	private BigDecimal saldo;
	@Column(nullable = false)
	private String codNumero;
	@Column()
	private String historico;
	@OneToMany(fetch = FetchType.EAGER , mappedBy = "caixa")
	@Fetch(FetchMode.SUBSELECT)
	private List<LancamentosCaixa> lancamentocaixa;
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public String getCodNumero() {
		return codNumero;
	}
	public void setCodNumero(String codNumero) {
		this.codNumero = codNumero;
	}
	public List<LancamentosCaixa> getLancamentocaixa() {
		return lancamentocaixa;
	}
	public void setLancamentocaixa(List<LancamentosCaixa> lancamentocaixa) {
		this.lancamentocaixa = lancamentocaixa;
	}
	public String getHistorico() {
		return historico;
	}
	public void setHistorico(String historico) {
		this.historico = historico;
	}
	
	
	
}
