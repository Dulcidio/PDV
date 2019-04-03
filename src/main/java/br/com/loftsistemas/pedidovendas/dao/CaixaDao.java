package br.com.loftsistemas.pedidovendas.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
 
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import br.com.loftsistemas.pedidovendas.dominio.Caixa;
import br.com.loftsistemas.pedidovendas.util.HibernateUtil;

 

public class CaixaDao extends GenericDAO<Caixa> {
	
	public List totalCaixas(List total) {
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		
		try{
			Criteria consulta = sessao.createCriteria(Caixa.class);
			ProjectionList projs = Projections.projectionList();
			projs.add(Projections.sum("saldo"));
			
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

}
