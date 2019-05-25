package br.com.framework.implementacao.crud;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.framework.hibernate.session.HibernateUtil;
import br.com.framework.interfac.crud.InterfaceCrud;

@Component
@Transactional
public class ImplementaçãoCrud<T> implements InterfaceCrud<T> {

	private static final long serialVersionUID = 1L;

	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	@Autowired
	private JdbcTemplateImpl jdbcTemplate;

	@Autowired
	private SimpleJdbcTemplateImpl simpleJdbcTemplate;

	@Autowired
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	private SimpleJdbcClassImpl simpleJdbcClassImpl;

	private SimpleJdbcClassImpl getSimpleJdbcClassImpl() {
		return simpleJdbcClassImpl;
	}

	@Override
	public void save(T obj) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlushSession();
	}

	@Override
	public void persist(T obj) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().persist(obj);
		executeFlushSession();
	}

	@Override
	public void saveOrUpdate(T obj) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().saveOrUpdate(obj);
		executeFlushSession();
	}

	@Override
	public void update(T obj) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().update(obj);
		executeFlushSession();
	}

	@Override
	public void delete(T obj) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlushSession();
	}

	@Override
	public T merge(T obj) throws Exception {
		validarSessionFactory();
		obj = (T) sessionFactory.getCurrentSession().merge(obj);
		executeFlushSession();
		return obj;
	}

	@Override
	public List<T> findList(Class<T> entidade) throws Exception {
		validarSessionFactory();
		StringBuilder query = new StringBuilder();
		query.append("select distinct(entity) from").append(entidade.getSimpleName()).append(" entity ");

		List<T> lista = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		return lista;
	}

	@Override
	public Object findById(Class<T> entidade, Long id) throws Exception {
		validarSessionFactory();
		Object resultado = sessionFactory.getCurrentSession().load(getClass(), id);
		return resultado;
	}

	@Override
	public T findByyId(Class<T> entidade, Long id) throws Exception {
		validarSessionFactory();
		T resultado = (T) sessionFactory.getCurrentSession().load(getClass(), id);
		return resultado;
	}

	@Override
	public List<T> findListByQueryDinamica(String s) throws Exception {
		validarSessionFactory();
		List<T> lista = new ArrayList<T>();
		lista = sessionFactory.getCurrentSession().createQuery(s).list();
		return lista;
	}

	@Override
	public void executeUpdateQueryDinamica(String s) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().createQuery(s).executeUpdate();
		executeFlushSession();
	}

	@Override
	public void executeUpdateSQLDinamica(String s) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().createSQLQuery(s).executeUpdate();
		executeFlushSession();
	}

	@Override
	public void clearSession() throws Exception {
		sessionFactory.getCurrentSession().clear();
	}

	@Override
	public void evict(Object objs) throws Exception {
		validarSessionFactory();
		sessionFactory.getCurrentSession().evict(objs);
	}

	@Override
	public Session getSession() throws Exception {
		validarSessionFactory();
		return sessionFactory.getCurrentSession();
	}

	public List<?> getListSQLDinamica(String s) throws Exception{
		validarSessionFactory();
		List<?> lista = (List<?>) sessionFactory.getCurrentSession().createSQLQuery(s).list();
		return lista;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	@Override
	public SimpleJdbcInsert getSimpleJdbcInsert() {
		return simpleJdbcInsert;
	}

	@Override
	public Long totalRegistro(String tabel) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from ").append(tabel);
		return jdbcTemplate.queryForLong(sql.toString());
	}

	@Override
	public Query obterQuery(String query) throws Exception {
		validarSessionFactory();
		Query queryReturn = sessionFactory.getCurrentSession().createQuery(query.toString());
		return queryReturn;
	}

	@Override
	public List<T> findListByQueryDinamica(String query, int iniciaNoRegistro, int maximoResultado) throws Exception {
		validarSessionFactory();
		List<T> lista = new ArrayList<T>();
		lista = sessionFactory.getCurrentSession().createQuery(query).setFirstResult(iniciaNoRegistro).setMaxResults(maximoResultado).list();
		return lista;
	}

	private void validarSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = HibernateUtil.getSessionFactory();
		}
		validarTransaction();
	}

	private void validarTransaction() {
		if (!sessionFactory.getCurrentSession().getTransaction().isActive()) {
			sessionFactory.getCurrentSession().beginTransaction();
		}
	}

	private void commitProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().commit();
	}

	private void rollBackProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().rollback();
	}

	/**
	 * Executa instantaneamente o SQL no banco de dados
	 */
	private void executeFlushSession() {
		sessionFactory.getCurrentSession().flush();
	}
	
	public List<Object[]> getListSQLDinamicaArray(String s) throws Exception {
		validarSessionFactory();
		List<Object[]> lista = (List<Object[]>) sessionFactory.getCurrentSession().createSQLQuery(s).list();
		return lista;
	}
	
}
