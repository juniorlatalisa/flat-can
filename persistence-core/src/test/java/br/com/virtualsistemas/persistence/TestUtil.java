package br.com.virtualsistemas.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestUtil {
	
	private static EntityManagerFactory factory = null;
	
	public static EntityManagerFactory getFactory() {
		if (factory == null) {
			TestUtil.factory = Persistence.createEntityManagerFactory("VSPersistenceTest");
		}
		return factory;
	}

}