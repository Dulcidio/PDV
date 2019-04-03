/**
 * @author Dulcidio Sobrinho
 * 29/01/2019
 */
package br.com.loftsistemas.pedidovendas.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import br.com.loftsistemas.pedidovendas.dominio.Registro;
import br.com.loftsistemas.pedidovendas.util.HibernateUtil;

/**
 * @author Dulcidio Sobrinho
 *
 */
public class RegistroDao extends GenericDAO<Registro>{
	
	/*Metodo para ver identificar o n�mero */ 
	public Long abreReg(Long numero){
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		try{
			Criteria consulta = sessao.createCriteria(Registro.class);
			numero =  (Long) consulta.setProjection(Projections.max("codigo")).uniqueResult();
			if(numero!=null){
			// System.out.println("o codigo n�o nulo �: "+numero); 
		    return numero;
			}else{
				//System.out.println("o nulo �"+numero); 
			return numero=0L;
			}
		}catch (RuntimeException erro){
			throw erro;
		}finally{
			sessao.close();
		}
	}

}
