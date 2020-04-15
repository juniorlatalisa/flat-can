package br.com.virtualsistemas.persistence.jpa;

import javax.persistence.EntityManager;

public class JPAFacadeImpl extends JPAFacade {

	public JPAFacadeImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	private EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
}