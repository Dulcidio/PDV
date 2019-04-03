/**
 * @author Dulcidio Sobrinho
 * 16/01/2019
 */
package br.com.loftsistemas.pedidovendas.dominio;

/**
 * @author Dulcidio Sobrinho
 *
 */
public enum Perfil {

	ADMINISTRADOR("administrador"),
	PADRAO ("padr�o"),
	COMUM("comum");
	
	private String perfil;
	
	Perfil(String perfil){
		this.perfil = perfil;
	}

	public String getPerfil() {
		return perfil;
	}
	
	
}
