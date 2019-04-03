/**
 * @author Dulcidio Sobrinho
 * 27/01/2019
 */
package br.com.loftsistemas.pedidovendas.dominio;

/**
 * @author Dulcidio Sobrinho
 *
 */
public enum SituacaoOrcamento {
	
	OK("Venda Ok"),
	CANCELADO("Venda Cancelada");
	
	private String situacao;
	
	SituacaoOrcamento(String situacao){
		this.situacao=situacao;
	}

	 public String getSituacao(){
		 return situacao;
	 }
}
