package br.com.virtualsistemas.persistence.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitTransactionType;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.BeforeClass;

import br.com.virtualsistemas.persistence.EntityTest;

public class EntityManagerFactoryBuilderTest extends JPAFacadeTest {

	@BeforeClass
	public static void beforeClass() {
		final EntityManagerFactory emf = new EntityManagerFactoryBuilder()
				.setProperty("javax.persistence.jdbc.url",
						"jdbc:h2:mem:vspersistence_runtime;IGNORECASE=TRUE;DB_CLOSE_ON_EXIT=TRUE")
				.setProperty("javax.persistence.jdbc.driver", org.h2.Driver.class.getName())
				.setProperty("hibernate.dialect", H2Dialect.class.getName())
				.setProperty("hibernate.hbm2ddl.auto", "update").setPersistenceUnitName("VSPersistenceTestRuntime")
				.setPersistenceProviderClassName(HibernatePersistenceProvider.class.getName())
				.setTransactionType(PersistenceUnitTransactionType.RESOURCE_LOCAL)
				.setManagedClassNames(EntityTest.class.getName()).build();
		JPAFacadeTest.instancia = new ResourceLocalFacade(emf.createEntityManager());
	}
}