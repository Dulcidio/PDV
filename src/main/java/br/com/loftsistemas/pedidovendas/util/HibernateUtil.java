/**
 * @author Dulcidio Sobrinho
 * 14/01/2019
 */
package br.com.loftsistemas.pedidovendas.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.service.ServiceRegistry;

/**
 * @author Dulcidio Sobrinho
 *
 */
public class HibernateUtil {

	private static SessionFactory fabricaDeSessoes = criarFabricaDeSessoes();

	public static SessionFactory getFabricaDeSessoes() {
		return fabricaDeSessoes;
	}
	
	public static Connection getConexao(){
		Session sessao = fabricaDeSessoes.openSession();
		
		Connection conexao= sessao.doReturningWork(new ReturningWork<Connection>() {
			 
			@Override
			public Connection execute(Connection conn)
					throws SQLException {
				 
				return conn;
			}
		});
		
		return conexao;
	}
	 
   
	private static SessionFactory criarFabricaDeSessoes() {
		try {
			Configuration configuracao = new Configuration().configure();
			
			ServiceRegistry registro = new StandardServiceRegistryBuilder().applySettings(configuracao.getProperties()).build();
			
			SessionFactory fabrica = configuracao.buildSessionFactory(registro);
			
			return fabrica;
		} catch (Throwable ex) {
			System.err.println("A f�brica de sess�es n�o pode ser criada." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
}
