package br.com.loftsistemas.pedidovendas.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.fasterxml.jackson.databind.ser.std.ArraySerializerBase;

import br.com.loftsistemas.pedidovendas.dominio.Caixa;
import br.com.loftsistemas.pedidovendas.dominio.LancamentosCaixa;
import br.com.loftsistemas.pedidovendas.dominio.Orcamento;
import br.com.loftsistemas.pedidovendas.util.HibernateUtil;
 

public class LancamentosCaixaDao extends GenericDAO<LancamentosCaixa>{
	private BigDecimal totalB;
	
	public List somaLancamentos(List total ) {
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		
		try{
			Criteria consulta = sessao.createCriteria(LancamentosCaixa.class);
			consulta.add(Restrictions.eq("tipo", "Saída"));
			ProjectionList projs = Projections.projectionList();
			
			projs.add(Projections.sum("valor"));
			
			consulta.setProjection(projs);
			
			List resultado =consulta.list();
			 
			total=resultado;  
			
		   return total;
			
		}catch (RuntimeException erro){
			throw erro;
		}finally{
			sessao.close();
		}
	}
	
	public List somarEntradas(List total ) {
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		
		try{
			Criteria consulta = sessao.createCriteria(LancamentosCaixa.class);
			consulta.add(Restrictions.eq("tipo", "Entrada"));
			ProjectionList projs = Projections.projectionList();
			
			projs.add(Projections.sum("valor"));
			
			consulta.setProjection(projs);
			
			List resultado =consulta.list();
			 
			total=resultado;  
			return total;
			
		}catch (RuntimeException erro){
			throw erro;
		}finally{
			sessao.close();
		}
	}

	public void salvarLancamento(LancamentosCaixa lancamento) {
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao =null;
		String tip = null;
		try{
			
			transacao = sessao.beginTransaction();
			sessao.save(lancamento);
			
			Caixa caixa = lancamento.getCaixa();
			tip=lancamento.getTipo();
			
			if(tip.equals("Entrada")) {
			caixa.setSaldo(lancamento.getValor().add((caixa.getSaldo())));
			sessao.update(caixa);
			tip=null;
			}
			else if(tip.equals("Saída")) {
				caixa.setSaldo(caixa.getSaldo().subtract((lancamento.getValor())));
				sessao.update(caixa);	
				tip=null;
			}
			
			
			transacao.commit();
		    
		}catch(RuntimeException erro){
			if(transacao!=null){
				transacao.rollback();
			}
			throw erro;
		}finally{
			sessao.close();
		}
	}
 

	public BigDecimal getTotalB() {
		return totalB;
	}

	public void setTotalB(BigDecimal totalB) {
		this.totalB = totalB;
	}
	
	 
	 
}
