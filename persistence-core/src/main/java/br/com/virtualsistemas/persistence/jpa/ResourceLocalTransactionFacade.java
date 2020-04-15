package br.com.virtualsistemas.persistence.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ResourceLocalTransactionFacade extends JPAFacade {
	
	public ResourceLocalTransactionFacade(EntityManager entityManager) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		this.entityManager = entityManager;
		this.entityTransaction = entityTransaction;
	}

	private EntityTransaction entityTransaction;
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	protected EntityTransaction getEntityTransaction() {
		return entityTransaction;
	}
	
	public void apply() {
		getEntityTransaction().commit();
		getEntityManager().close();
	}
	
	public void cancel() {
		getEntityTransaction().rollback();
		getEntityManager().close();
	}
}