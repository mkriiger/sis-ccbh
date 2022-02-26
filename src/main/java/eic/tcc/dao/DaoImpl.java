package eic.tcc.dao;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings({"deprecation","rawtypes","unchecked"})
public class DaoImpl implements Dao 
{
	//
	// Dependências
	//
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	@Autowired
	protected SessionFactory sessionFactory;
	@Autowired
	protected HibernateTemplate hibernateTemplate;
	
	@Override
	public void flushSession() 
	{
		sessionFactory.getCurrentSession().flush();
	}
	
	@Override
	@Transactional
	public Map<String,Object> querySingleRowSQL(String sqlQuery) 
    {
		try
		{
			return jdbcTemplate.queryForMap(sqlQuery);
		}
		catch(EmptyResultDataAccessException e)
		{
			return new TreeMap<String,Object>();
		}
    }	
	
	@Override
	@Transactional
	public <T> List<T> queryMultiRowSQL(String sqlQuery,RowMapper<T> rowMapper) 
    {
		return jdbcTemplate.query(sqlQuery,rowMapper);		
    }
	
	@Override
	@Transactional
	public void executeSQL(String sql)
	{
		jdbcTemplate.update(sql);
	}
	
	@Override
	public List<?> queryHQL(String hqlQuery, Object... values)
	{
		return hibernateTemplate.find(hqlQuery, values);
	}
	
	@Override
	public List<?> queryLimitHQL(String hqlQuery, int limit, Object... values)
	{
		hibernateTemplate.setMaxResults(limit);
		List<?> result = hibernateTemplate.find(hqlQuery, values);
		hibernateTemplate.setMaxResults(0);
		return result;
	}
	
	@Override
	@Transactional
	public <ENTITY> ENTITY retrieveById(Class<ENTITY> clazz, String id) 
    {
		ENTITY entity = (ENTITY)sessionFactory.getCurrentSession().get(clazz, id);
        return entity;
    }		
	
	@Override
	@Transactional	
	public <ENTITY> ENTITY retrieveLast(Class<ENTITY> clazz) 
    {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clazz);
		criteria.addOrder(Order.desc("timestamp"));
		criteria.setMaxResults(1);		
		
		List<ENTITY> result = (List<ENTITY>)criteria.list();
		
		if(result.size()>=1)
			return result.get(0);
		else
			return null;	        
    }	
	
	@Override
	@Transactional	
	public <ENTITY> ENTITY retrieveLastBySingleLike(Class<ENTITY> clazz, String field, Object value) 
    {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clazz);
		
		if (field == null || field.isEmpty())
			throw new IllegalArgumentException("Field argument cannot be null or empty");

		if (value != null)
			criteria.add(Restrictions.eq(field,value));
		
		criteria.addOrder(Order.desc("timestamp"));
		criteria.setMaxResults(1);		
		
		List<ENTITY> result = (List<ENTITY>)criteria.list();
		
		if(result.size()>=1)
			return result.get(0);
		else
			return null;	        
    }
	
    @Override
    @Transactional
	public <ENTITY> List<ENTITY> retrieveAll(Class<ENTITY> clazz) 
	{		
		return (List<ENTITY>)hibernateTemplate.loadAll(clazz);		
	}
	
	@Override
	@Transactional
	public <ENTITY> List<ENTITY> retrieveAll(Class<ENTITY> clazz, String sortBy,boolean ascending)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clazz);
		
		if (ascending)
			criteria.addOrder(Order.asc(sortBy));
		else
			criteria.addOrder(Order.desc(sortBy));
				
		List<ENTITY> result = criteria.list();
		return result;	
	}
	
	@Override
	@Transactional
	public <ENTITY> List<ENTITY> retrieveBySingleLike(Class<ENTITY> clazz, String field,String value) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clazz);
		
		if (field == null || field.isEmpty())
			throw new IllegalArgumentException("Field argument cannot be null or empty");

		if (value != null && !value.isEmpty())
			criteria.add(Restrictions.ilike(field,value,MatchMode.EXACT));
			
		List<ENTITY> result = criteria.list();
		return result;	
	}
	
	@Override
	@Transactional
	public <ENTITY> List<ENTITY> retrieveBySingleLike(Class<ENTITY> clazz, String field, Object value) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clazz);
		
		if (field == null || field.isEmpty())
			throw new IllegalArgumentException("Field argument cannot be null or empty");

		if (value != null)
			criteria.add(Restrictions.eq(field,value));
			
		List<ENTITY> result = criteria.list();
		return result;	
	}
		
	@Override
	@Transactional
	public <ENTITY> List<ENTITY> retrieveBySingleLike(Class<ENTITY> clazz, String field,String value,MatchMode matchMode) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clazz);
		
		if (field == null || field.isEmpty())
			throw new IllegalArgumentException("Field argument cannot be null or empty");

		if (value != null && !value.isEmpty())
			criteria.add(Restrictions.ilike(field,value,matchMode));
			
		List<ENTITY> result = criteria.list();
		return result;	
	}
	
	@Override
	@Transactional
	public <ENTITY> List<ENTITY> retrieveByManyLikes(Class<ENTITY> clazz, DaoLike... daoLikes)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clazz);
		
		for (DaoLike daoLike : daoLikes) 
		{
			String    field = daoLike.getField();
			Object    value = daoLike.getValue();
						
			if (field == null || field.isEmpty())
				throw new IllegalArgumentException("Field argument cannot be null or empty");

			if (value != null)
			{
				criteria.add(Restrictions.eq(field,value));
			}
		}
		
		List<ENTITY> result = criteria.list();
		return result;	
	}
	
	@Override
	@Transactional
    public void persist(Object object) throws DataAccessException 
    {
		try
		{
			sessionFactory.getCurrentSession().saveOrUpdate(object);
		}
		catch(NonUniqueObjectException e)
		{
			sessionFactory.getCurrentSession().merge(object);
		}
    }
          
    	
	@Override
    @Transactional
	public void delete(Object object) 
	{
		sessionFactory.getCurrentSession().delete(object);
	}
	
	@Override
	@Transactional
	public void deleteAll(Class clazz) 
	{
		List all = retrieveAll(clazz);
		
		for (Object one : all) 
		{
			delete(one);
		}
	}

	@Override
	@Transactional
	public void deleteBySingleLike(Class clazz, String field, String value) 
	{
		List all = this.retrieveBySingleLike(clazz, field, value);
		
		for (Object one : all) 
		{
			delete(one);
		}
	}
	
	@Override
	@Transactional
	public void deleteBySingleLike(Class clazz, String field, Object value) 
	{
		List all = this.retrieveBySingleLike(clazz, field, value);
		
		for (Object one : all) 
		{
			delete(one);
		}
	}	
}