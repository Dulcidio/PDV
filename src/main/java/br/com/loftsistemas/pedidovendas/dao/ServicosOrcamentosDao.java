/**
 * @author Dulcidio Sobrinho
 * 26/01/2019
 */
package br.com.loftsistemas.pedidovendas.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.loftsistemas.pedidovendas.dominio.LancamentosCaixa;
import br.com.loftsistemas.pedidovendas.dominio.ServicosOrcamento;
import br.com.loftsistemas.pedidovendas.util.HibernateUtil;

/**
 * @author Dulcidio Sobrinho
 *
 */
public class ServicosOrcamentosDao extends GenericDAO<ServicosOrcamento> {

	public Long ContarTotal(Long total){
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		try{
			Criteria consulta = sessao.createCriteria(ServicosOrcamento.class);
			 
			total = (Long) consulta.setProjection(Projections.max("codigo")).uniqueResult();
			 if(total != 0){
				 //System.out.println(total);
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
	
	public  ServicosOrcamento buscar(Long codigo){
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		try{
			Criteria consulta = sessao.createCriteria(ServicosOrcamento.class);
			consulta.add(Restrictions.idEq(codigo));
			ServicosOrcamento resultado = (ServicosOrcamento)consulta.uniqueResult();
			System.out.println("OKK   "+codigo);
			return resultado;
		}catch (RuntimeException erro){
			throw erro;
		}finally{
			sessao.close();
		}
	}
}
