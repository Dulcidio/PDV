/**
 * @author Dulcidio Sobrinho
 * 14/01/2019
 */
package br.com.loftsistemas.pedidovendas.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.loftsistemas.pedidovendas.util.HibernateUtil;
 

/**
 * @author Dulcidio Sobrinho
 *
 */
public class GenericDAO <Entidade> {
	
private Class<Entidade> classe;
	
	@SuppressWarnings("unchecked")
	public GenericDAO(){
		this.classe = (Class<Entidade>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public void salvar(Entidade entidade){
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao =null;
		try{
			transacao = sessao.beginTransaction();
			sessao.save(entidade);
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
	
	@SuppressWarnings("unchecked")
	public List<Entidade> listar(){
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		try{
			Criteria consulta = sessao.createCriteria(classe);
			List<Entidade> resultado = consulta.list();
			return resultado;
		}catch (RuntimeException erro){
			throw erro;
		}finally{
			sessao.close();
		}
	}
	 
	public List<Entidade> listarComNomes(String descr){
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		try{
			Criteria consulta = sessao.createCriteria(classe);
			List<Entidade> resultado = consulta.list();
			return resultado;
		}catch (RuntimeException erro){
			throw erro;
		}finally{
			sessao.close();
		}
	}
	
	public  Entidade buscar(Long codigo){
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		try{
			Criteria consulta = sessao.createCriteria(classe);
			consulta.add(Restrictions.idEq(codigo));
			Entidade resultado = (Entidade)consulta.uniqueResult();
			System.out.println("OKK   "+codigo);
			return resultado;
		}catch (RuntimeException erro){
			throw erro;
		}finally{
			sessao.close();
		}
	}
/*
	public  Entidade buscarBarras(String barras){
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		try{
			Criteria consulta = sessao.createCriteria(classe);
			consulta.add(Restrictions.eq("barras","15004932"));
			Entidade resultado = (Entidade)consulta.uniqueResult();
			System.out.println("OKK   "+resultado);
			return resultado;
		}catch (RuntimeException erro){
			throw erro;
		}finally{
			sessao.close();
		}
	}
	*/
	public void editar(Entidade entidade){
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao =null;
		try{
			transacao = sessao.beginTransaction();
			sessao.update(entidade);
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
	
	public void merge(Entidade entidade){
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao =null;
		try{
			transacao = sessao.beginTransaction();
			sessao.merge(entidade);
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
	 
	  
	
	public void excluir(Entidade entidade){
		Session sessao=HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao =null;
		try{
			transacao = sessao.beginTransaction();
			sessao.delete(entidade);
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

}
