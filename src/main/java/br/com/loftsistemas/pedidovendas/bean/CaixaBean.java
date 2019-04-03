package br.com.loftsistemas.pedidovendas.bean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.loftsistemas.pedidovendas.dao.CaixaDao;
import br.com.loftsistemas.pedidovendas.dominio.Caixa;
import br.com.loftsistemas.pedidovendas.util.HibernateUtil;
import br.com.loftsistemas.pedidovendas.util.UtilRelatorios;

 


@SuppressWarnings("serial")
@ManagedBean (name="caixaBean")
@SessionScoped
public class CaixaBean implements Serializable{
	
	private Caixa caixa = new Caixa();
	private CaixaDao caixaDao = new CaixaDao();
	private List<Caixa> caixas;
	private List totalSaldo;
	private String valor="0";
	
	
	public void listar() {
		try{
			 caixas=caixaDao.listar();
			 
		}catch(Exception e){
			Messages.addGlobalError("Erro ao carregar os caixas");
			e.printStackTrace();
		}
	}
	 
	public void valores() {
		
		somarSaldo(totalSaldo);
	 //System.out.println("O valor total é: "+totalSaldo);
	}
	
	public void salvar() {
		try{
			caixaDao.merge(caixa);
			caixa = new Caixa();
			caixas=caixaDao.listar();
			mensagemOk();
       	    retornarClean();
			
		}catch(Exception e){
			Messages.addGlobalError("Erro ao cadastrar caixa");
			e.printStackTrace();
		}
		
	}
	
	/*Metodo que evita o reenvio do form apos salvar caso se pressione f5 */
    public void  retornarClean() {
    	 
    	try {
    		 
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaCaixa.xhtml");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*Metodo para permitir que as mensagens de confirmação seja apresentadas*/
    public String mensagemOk() {
    	  FacesContext.getCurrentInstance().addMessage(
    	        null, new FacesMessage("Caixa cadastrado!"));
    	 
    	  FacesContext.getCurrentInstance()
    	      .getExternalContext()
    	      .getFlash().setKeepMessages(true);
    	 
    	  return "listar/listaCaixa.xhtml?faces-redirect=true";
    	}
	
    public void somarSaldo(List total) {
    	try {
    		total=caixaDao.totalCaixas(total);
    		totalSaldo=total;
    		List <Caixa> alterar;
    		for(Object al: totalSaldo) {
    			String nv=String.valueOf(al);
    			  valor=nv;
    			 
    		}
    		 
    	}catch(Exception e){
    		
    	}
    }
    
    public void editar(ActionEvent evento){
    	try{
    		 
    	caixa=(Caixa)evento.getComponent().getAttributes().get("caixaSelecionado");
    	}catch(RuntimeException erros){
    		Messages.addGlobalError("Erro ao selecionar caixa");
    	}
    }
    
    public void imprimir(){
      	 
		HashMap parametros = new HashMap();
		 
		Connection conexao = HibernateUtil.getConexao();
		UtilRelatorios.imprimirRelatorio("listaCaixas", parametros, conexao);
		
	 
}
	public Caixa getCaixa() {
		return caixa;
	}
	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}
	public CaixaDao getCaixaDao() {
		return caixaDao;
	}
	public void setCaixaDao(CaixaDao caixaDao) {
		this.caixaDao = caixaDao;
	}
	public List<Caixa> getCaixas() {
		return caixas;
	}
	public void setCaixas(List<Caixa> caixas) {
		this.caixas = caixas;
	}

	public List getTotalSaldo() {
		return totalSaldo;
	}

	public void setTotalSaldo(List totalSaldo) {
		this.totalSaldo = totalSaldo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	 
	
	

}
