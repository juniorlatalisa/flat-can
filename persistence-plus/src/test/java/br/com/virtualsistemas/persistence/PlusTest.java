package br.com.virtualsistemas.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.validation.ConstraintViolationException;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.virtualsistemas.persistence.jpa.EntityManagerFactoryBuilder;
import br.com.virtualsistemas.persistence.jpa.JPAFacade;
import br.com.virtualsistemas.persistence.jpa.ResourceLocalFacade;

public class PlusTest extends TesteEntidade<PlusEntityTest> {

	private static JPAFacade facade;

	@BeforeClass
	public static void beforeClass() {
		TesteEntidade.beforeClass();
		final EntityManagerFactory emf = new EntityManagerFactoryBuilder()
				.setProperty("javax.persistence.jdbc.url",
						"jdbc:h2:mem:vspersistence_runtime;IGNORECASE=TRUE;DB_CLOSE_ON_EXIT=TRUE")
				.setProperty("javax.persistence.jdbc.driver", org.h2.Driver.class.getName())
				.setProperty("hibernate.dialect", H2Dialect.class.getName())
				.setProperty("hibernate.hbm2ddl.auto", "update").setPersistenceUnitName("VSPersistenceTestRuntime")
				.setPersistenceProviderClassName(HibernatePersistenceProvider.class.getName())
				.setTransactionType(PersistenceUnitTransactionType.RESOURCE_LOCAL)
				.setManagedClassNames(PlusEntityTest.class.getName()).build();
		PlusTest.facade = new ResourceLocalFacade(emf.createEntityManager());
	}

	@AfterClass
	public static void afterClass() {
		((ResourceLocalFacade) PlusTest.facade).getEntityManager().close();
	}

	@Override
	protected PlusEntityTest alterar(PlusEntityTest entity) {
		entity.setEmail("teste@teste.com.br");
		return getFacade().update(entity);
	}

	@Override
	protected PlusEntityTest criar() {
		PlusEntityTest entity = new PlusEntityTest();
		entity.setCpf("11144477735");
		return getFacade().insert(entity);
	}

	@Override
	protected JPAFacade getFacade() {
		return facade;
	}

	@Test(expected = ConstraintViolationException.class)
	public void cfpNulo() {
		PlusEntityTest entity = new PlusEntityTest();
		Assert.assertNotNull(getFacade().insert(entity).getCodigo());
	}

	@Test(expected = ConstraintViolationException.class)
	public void cfpVazio() {
		PlusEntityTest entity = new PlusEntityTest();
		entity.setCpf("");
		Assert.assertNotNull(getFacade().insert(entity).getCodigo());
	}

	@Test(expected = ConstraintViolationException.class)
	public void cfpInvalido() {
		PlusEntityTest entity = new PlusEntityTest();
		entity.setCpf("11144477733");
		getFacade().insert(entity);
	}

	@Test(expected = ConstraintViolationException.class)
	public void emailInvalido() {
		PlusEntityTest entity = new PlusEntityTest();
		entity.setCpf("11144477735");
		entity.setEmail("inválido");
		getFacade().insert(entity);
	}
}