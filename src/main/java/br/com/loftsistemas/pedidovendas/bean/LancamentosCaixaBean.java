package br.com.loftsistemas.pedidovendas.bean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.loftsistemas.pedidovendas.dao.LancamentosCaixaDao;
import br.com.loftsistemas.pedidovendas.dominio.LancamentosCaixa;
import br.com.loftsistemas.pedidovendas.util.HibernateUtil;
import br.com.loftsistemas.pedidovendas.util.UtilRelatorios;
 


@SuppressWarnings("serial")
@ManagedBean (name="lancamentosCaixa")
@SessionScoped
public class LancamentosCaixaBean implements Serializable {
	
	private LancamentosCaixa lancamento = new LancamentosCaixa();
	private LancamentosCaixaDao lancamentosDao = new LancamentosCaixaDao();
	private List<LancamentosCaixa> lancamentos;
	private String valor ;
	private List totalLanca;
	private String valorEntrada ;
	private List totalEntrada;
	private String entrada ;
	private String saida;
	private Date dataGeral;
	private Date inicioData;
	private Date finalDate;
	public String getEntrada() {
		return entrada;
	}

	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}

	public String getSaida() {
		return saida;
	}

	public void setSaida(String saida) {
		this.saida = saida;
	}

	public void listar() {
		try{
			 lancamentos=lancamentosDao.listar();
			
		}catch(Exception e){
			Messages.addGlobalError("Erro ao carregar lançamentos");
			e.printStackTrace();
		}
	}
	 
	public void carregarLanca() {
		  somarLanca(totalLanca);
		  somarEntradas(totalEntrada);
		
		 
	}
	public void salvar() {
		try{
			 
			 lancamentosDao.salvarLancamento(lancamento);
			 lancamento = new LancamentosCaixa();
			 lancamentos=lancamentosDao.listar();
			  
			 mensagemOk();
	       	 retornarClean();
			
		}catch(Exception e){
			Messages.addGlobalError("Erro ao salvar lançamento");
			e.printStackTrace();
		}
		
	}
	
	public void salvarPdv() {
		try{
			 
			 lancamentosDao.salvarLancamento(lancamento);
			 lancamento = new LancamentosCaixa();
			 lancamentos=lancamentosDao.listar();
			  
			 mensagemOk();
	       	 retornarClean();
			
		}catch(Exception e){
			Messages.addGlobalError("Erro ao salvar lançamento");
			e.printStackTrace();
		}
		
	}
	public void somarLanca(List lanc) {
		lanc=lancamentosDao.somaLancamentos(lanc);
		totalLanca=lanc;
		 
		 for(Object tl:totalLanca) {
			 String nv=String.valueOf(tl);
			  saida=nv;
		      valor=saida;
		}
		 
		
	}
	
	public void somarEntradas(List lanc) {
		lanc=lancamentosDao.somarEntradas(lanc);
		totalEntrada=lanc;
		
		for(Object tl:totalEntrada) {
			  String nv=String.valueOf(tl);
			  valorEntrada=nv;
			  entrada = valorEntrada; 
			   
		}
		 
	}
	
	public void editar(ActionEvent evento){
    	try{
    		 
    	lancamento=(LancamentosCaixa)evento.getComponent().getAttributes().get("lancamentoSelecionado");
    	}catch(RuntimeException erros){
    		Messages.addGlobalError("Erro ao selecionar lançamento");
    	}
    }
	
	/*Metodo que evita o reenvio do form apos salvar caso se pressione f5 */
    public void  retornarClean() {
    	 
    	try {
    		 
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaLancamentos.xhtml");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*Metodo para permitir que as mensagens de confirmação seja apresentadas*/
    public String mensagemOk() {
    	  FacesContext.getCurrentInstance().addMessage(
    	        null, new FacesMessage("Lançamento Salvo!"));
    	 
    	  FacesContext.getCurrentInstance()
    	      .getExternalContext()
    	      .getFlash().setKeepMessages(true);
    	 
    	  return "listar/listaLancamentos.xhtml?faces-redirect=true";
    	}
    
    public void imprimirGeral(){
   	 
		HashMap parametros = new HashMap();
		 
		Connection conexao = HibernateUtil.getConexao();
		UtilRelatorios.imprimirRelatorio("lancamentos", parametros, conexao);
		
	 
}
    public void imprimirData(){
    	dataGeral=lancamento.getData();
      	System.out.println(dataGeral); 
		HashMap parametros = new HashMap();
		parametros.put("LANCA_DATA", dataGeral); 
		Connection conexao = HibernateUtil.getConexao();
		UtilRelatorios.imprimirRelatorio("lancamentosLista", parametros, conexao);
		
	 
}
    
    public void imprimirIntervalo(){
    	 
		HashMap parametros = new HashMap();
		parametros.put("DATA_INICIO", inicioData); 
		parametros.put("DATA_FINAL", finalDate);
		Connection conexao = HibernateUtil.getConexao();
		UtilRelatorios.imprimirRelatorio("lancamentosIntervalo", parametros, conexao);
		
	 
}
    
    public void imprimirPorCaixa(){
    	 
      	HashMap parametros = new HashMap();
		parametros.put("NOME_CAIXA", lancamento.getCaixa().getDescricao()); 
		Connection conexao = HibernateUtil.getConexao();
		UtilRelatorios.imprimirRelatorio("lancamentosPorCaixa", parametros, conexao);
		
	 
}
    
	
	public LancamentosCaixa getLancamento() {
		return lancamento;
	}
	public void setLancamento(LancamentosCaixa lancamento) {
		this.lancamento = lancamento;
	}
	public LancamentosCaixaDao getLancamentosDao() {
		return lancamentosDao;
	}
	public void setLancamentosDao(LancamentosCaixaDao lancamentosDao) {
		this.lancamentosDao = lancamentosDao;
	}

	public List<LancamentosCaixa> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<LancamentosCaixa> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public List getTotalLanca() {
		return totalLanca;
	}

	public void setTotalLanca(List totalLanca) {
		this.totalLanca = totalLanca;
	}
    
	

	public String getValorEntrada() {
		return valorEntrada;
	}

	public void setValorEntrada(String valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public List getTotalEntrada() {
		return totalEntrada;
	}

	public void setTotalEntrada(List totalEntrada) {
		this.totalEntrada = totalEntrada;
	}

	@Override
	public String toString() {
		return "LancamentosCaixaBean valorEntrada=" + valorEntrada + "";
	}

	public Date getDataGeral() {
		return dataGeral;
	}

	public void setDataGeral(Date dataGeral) {
		this.dataGeral = dataGeral;
	}

	public Date getInicioData() {
		return inicioData;
	}

	public void setInicioData(Date inicioData) {
		this.inicioData = inicioData;
	}

	public Date getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}

	

}
