package br.com.virtualsistemas.persistence.jpa;

import java.util.function.Supplier;

import javax.persistence.EntityManager;

public class JPAFacadeImpl extends JPAFacade {

	public JPAFacadeImpl(EntityManager entityManager) {
		this.entityManager = () -> entityManager;
	}

	public JPAFacadeImpl(Supplier<EntityManager> entityManager) {
		this.entityManager = entityManager;
	}

	private Supplier<EntityManager> entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager.get();
	}
}