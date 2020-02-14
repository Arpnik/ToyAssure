package com.increff.assure.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractDao< T > {

	@PersistenceContext
	private EntityManager em;

	private Class< T > clazz;

	public AbstractDao()
	{
		this.clazz=(Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public List< T > selectAll(){
		return em.createQuery( "from " + clazz.getName() ).getResultList();
	}

	public void insert(T entity){
		em.persist( entity );
	}

	public T select(long id){
		return em.find( clazz, id );
	}

	protected T getSingle(TypedQuery<T> query) {
		return query.getResultList().stream().findFirst().orElse(null);
	}

	protected  TypedQuery<T> getQuery(String jpql) {
		return em.createQuery(jpql, clazz);
	}

	protected EntityManager em() {
		return em;
	}

}
