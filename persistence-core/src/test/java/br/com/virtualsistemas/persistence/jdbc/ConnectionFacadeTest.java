package br.com.virtualsistemas.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.virtualsistemas.persistence.TestUtil;

public class ConnectionFacadeTest {

	private static ConnectionFacade instancia;

	@BeforeClass
	public static void beforeClass() {
		EntityManagerFactory factory = TestUtil.getFactory();
		Session session = factory.createEntityManager().unwrap(Session.class);
		session.doWork(connection -> ConnectionFacadeTest.instancia = new ConnectionFacadeImpl(connection));
	}

	protected int insert(String name) {
		return instancia.createQueryBuilder("insert into entity_test (name) values (?)").setParam(1, name).execute();
	}

	protected Object[] get(String name) {
		return instancia.createQueryBuilder("select id, name from entity_test where name = ?").setParam(1, name).find();
	}

	@Test
	public void execute() {
		Assert.assertTrue(Integer.valueOf(1).equals(insert(UUID.randomUUID().toString())));
	}

	@Test
	public void list() {
		String name = UUID.randomUUID().toString();
		Assert.assertTrue(Integer.valueOf(insert(name))
				.equals(instancia.createQueryBuilder("select id, name from entity_test where name = ?").setMaxResults(1)
						.setParam(1, name).list().size()));
	}

	@Test
	public void iterator() {
		String name = UUID.randomUUID().toString();
		insert(name);
		Assert.assertTrue(name.equals(
				instancia.createQueryBuilder("select name from entity_test where name = ?").setParam(1, name).find()));
	}

	protected static class ConnectionFacadeImpl extends ConnectionFacade {

		public ConnectionFacadeImpl(Connection connection) {
			this.connection = connection;
		}

		private Connection connection;

		@Override
		protected Connection getConnection() {
			return connection;
		}

		@Override
		public void close() {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new PersistenceException(e);
			}
		}
	}
}