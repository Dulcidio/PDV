/**
 * @author Dulcidio Sobrinho
 * 21/01/2019
 */
package br.com.loftsistemas.pedidovendas.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.loftsistemas.pedidovendas.dominio.Orcamento;
import br.com.loftsistemas.pedidovendas.dominio.Produto;
import br.com.loftsistemas.pedidovendas.util.HibernateUtil;

/**
 * @author Dulcidio Sobrinho
 *
 */
public class ProdutoDao extends GenericDAO<Produto> {
	
	public Long ContarTotal(Long total){
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		try{
			Criteria consulta = sessao.createCriteria(Produto.class);
			 
			total = (Long) consulta.setProjection(Projections.max("codigo")).uniqueResult();
			 if(total != 0){
				 System.out.println(total);
				 return total;
			 }else{
					return total=0L;
					}
			 
			
		}catch (RuntimeException erro){
			throw erro;
		}finally{
			sessao.close();
		}
	}
	
	public  Produto buscarBarras(String barras ){
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		try{
			Criteria consulta = sessao.createCriteria(Produto.class);
			consulta.add(Restrictions.eq("barras",barras));
			Produto resultado = (Produto)consulta.uniqueResult();
			System.out.println("OKK   "+resultado);
			return resultado;
		}catch (RuntimeException erro){
			throw erro;
		}finally{
			sessao.close();
		}
	}

}
