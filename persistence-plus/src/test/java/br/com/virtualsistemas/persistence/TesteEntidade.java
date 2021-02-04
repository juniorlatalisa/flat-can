package br.com.virtualsistemas.persistence;

import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Validation;
import javax.validation.constraints.Size;

import org.junit.Assert;
import org.junit.Test;

import br.com.virtualsistemas.persistence.jpa.JPAFacade;
import br.com.virtualsistemas.persistence.jpa.JPAFacade.QueryStrategy;
import br.com.virtualsistemas.persistence.jpa.ResourceLocalFacade;

public abstract class TesteEntidade<E extends Entidade> {

	protected static final Logger log = Logger.getLogger("TesteEntidade");

	protected static ResourceLocalFacade facade = null;
	protected static EntityManagerFactory factory = null;

	public static JPAFacade getJPAFacade() {
		return facade;
	}

	public static EntityManagerFactory getEntityManagerFactory() {
		return factory;
	}

	protected static void beforeClass(String persistenceUnitName) {
		beforeClass(persistenceUnitName, null);
	}

	protected static void beforeClass(String persistenceUnitName, InputStream loadQuery) {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory(persistenceUnitName);
			factory.createEntityManager().close();
		}
		if (facade == null) {
			log.info("Validador configurado: " + Validation.buildDefaultValidatorFactory().getValidator());
			facade = new ResourceLocalFacade(factory.createEntityManager());
			if (loadQuery != null) {
				log.info("LoadQuery: " + facade.createQueryBuilder(QueryStrategy.NATIVE, loadQuery).execute());
			}
		}
		/*
		<dependency>
			<groupId>org.glassfish</groupId>
			<artifactId>javax.el</artifactId>
			<version>3.0.1-b08</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-validator</artifactId>
			<version>6.0.2.Final</version>
			<scope>test</scope>
		</dependency>
		*/
	}

	protected abstract E criar();

	protected JPAFacade getFacade() {
		return facade;
	}

	protected void detach(Object entity) {
		facade.getEntityManager().detach(entity);
	}

	protected EntityManagerFactory getFactory() {
		return factory;
	}

	protected void testeCopiavel(E origem, Copiavel<E> destino) {
		if (!remover(origem)) {
			throw new RuntimeException("Não removeu");
		}
		destino.copiar(origem);
		getFacade().insert(destino);
	}

	protected void nomeVazio(Nomeavel entidade) {
		entidade.setNome("");
		getFacade().update(entidade);
	}

	protected void nomeNulo(Nomeavel entidade) {
		entidade.setNome(null);
		getFacade().update(entidade);
	}

	protected void nomeSize(Nomeavel entidade) {
		Size size = null;
		{
			Class<?> _class = entidade.getClass();
			while (!_class.equals(Object.class)) {
				try {
					size = _class.getDeclaredField("nome").getAnnotation(Size.class);
					break;
				} catch (NoSuchFieldException | SecurityException e) {
					_class = _class.getSuperclass();
				}
			}
			if (size == null) {
				throw new RuntimeException("nomeSize fail: " + entidade);
			}
		}
		if (size.min() > 0) {
			entidade.setNome("");
		} else if (size.max() > 0) {
			entidade.setNome(new String(new char[1 + size.max()]));
		} else {
			entidade.setNome(null);
		}
		getFacade().update(entidade);
	}

	protected E alterar(E entidade) {
		NomeavelTest.preencher((Nomeavel) entidade);
		return getFacade().update(entidade);
	}

	protected Serializable getPrimaryKey(E entidade) {
		if (entidade instanceof Codificavel<?>) {
			return ((Codificavel<?>) entidade).getCodigo();
		}
		if (entidade instanceof EntidadeComposta) {
			return ((EntidadeComposta) entidade).getId();
		}
		if (entidade instanceof Identificavel<?>) {
			return ((Identificavel<?>) entidade).getIdentificador();
		}
		throw new IllegalAccessError("Unknow entity: " + entidade);
	}

	@SuppressWarnings("unchecked")
	protected E pesquisar(E entidade) {
		return (E) getFacade().find(entidade.getClass(), getPrimaryKey(entidade));
	}

	protected boolean remover(E entidade) {
		return getFacade().delete(entidade.getClass(), getPrimaryKey(entidade));
	}

	@Test
	public void basico() {
		E entidade1 = criar();

		E entidade2;

		if (!entidade1.equals(entidade2 = pesquisar(entidade1))) {
			Assert.fail("Entidades não são iguais [insert]");
			return;
		}

		if (entidade1.equals(alterar(entidade2))) {
			Assert.fail("Entidades são iguais");
			return;
		}

		if (!entidade2.equals(entidade2 = pesquisar(entidade1))) {
			Assert.fail("Entidades não são iguais [update]");
			return;
		}

		if (!((remover(entidade1)) && (pesquisar(entidade1) == null))) {
			Assert.fail("Objeto não foi removido");
		}
	}
}