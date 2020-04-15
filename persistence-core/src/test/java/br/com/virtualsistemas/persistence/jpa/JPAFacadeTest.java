package br.com.virtualsistemas.persistence.jpa;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.virtualsistemas.persistence.EntityTest;
import br.com.virtualsistemas.persistence.TestUtil;
import br.com.virtualsistemas.persistence.VSPersistence;
import br.com.virtualsistemas.persistence.jpa.JPAFacade.QueryStrategy;

public class JPAFacadeTest {

	protected static JPAFacade instancia = null;

	@BeforeClass
	public static void beforeClass() {
		JPAFacadeTest.instancia = new ResourceLocalFacade(TestUtil.getFactory().createEntityManager());
	}

	@AfterClass
	public static void afterClass() {
		((ResourceLocalFacade) JPAFacadeTest.instancia).getEntityManager().close();
	}

	private Long getId() {
		EntityTest entity = new EntityTest();
		entity.setName(UUID.randomUUID().toString());
		entity.setValue(1.0);
		return instancia.insert(entity).getId();
	}

	@Test
	public void insert() {
		Assert.assertNotNull(getId());
	}

	@Test
	public void find1() {
		Long id;
		Assert.assertTrue(instancia.find(EntityTest.class, (id = getId())).getId().equals(id));
	}

	@Test
	public void find2() {
		Long id = getId();
		EntityTest e1 = instancia.find(EntityTest.class, id);
		EntityTest e2 = instancia.createQueryBuilder(QueryStrategy.CRITERIA, EntityTest.class).setParam("id", id)
				.find();
		Assert.assertTrue(e1.equals(e2));
	}

	@Test
	public void delete1() {
		Assert.assertTrue(instancia.delete(EntityTest.class, getId()));
	}

	@Test
	public void delete2() {
		Long id = getId();
		Assert.assertTrue(Integer.valueOf(1).equals(
				instancia.createQueryBuilder(QueryStrategy.NAMED, EntityTest.DELETE1).setParam("id", id).execute()));
	}

	@Test
	public void execute1() {
		Long id = getId();
		if (id.equals(1L)) {
			id = getId();
		}
		Integer alterados = instancia.execute(QueryStrategy.DEFAULT, "delete from EntityTest e where e.id = :id",
				Collections.singletonMap("id", id));
		Assert.assertTrue(alterados.equals(1));
	}

	@Test
	public void execute2() {
		List<Long> list1 = Arrays.asList(getId(), getId());
		Assert.assertTrue(Integer.valueOf(list1.size()).equals(instancia
				.createQueryBuilder(QueryStrategy.NAMED, EntityTest.DELETE2).setParam("ids", list1).execute()));
	}

	@Test
	public void update() {
		EntityTest e1 = instancia.find(EntityTest.class, getId());
		EntityTest e2 = instancia.find(EntityTest.class, e1.getId());
		if (!((e1 != e2) && (e1.equals(e2)))) {
			Assert.fail("Falha na condição do teste");
			return;
		}
		e2.setValue(e2.getValue() * 3.1);
		Assert.assertNotEquals(e1, instancia.find(EntityTest.class, instancia.update(e2).getId()));
	}

	@Test
	public void list1() {
		List<EntityTest> lista1 = Arrays.asList(instancia.find(EntityTest.class, getId()),
				instancia.find(EntityTest.class, getId()));
		List<EntityTest> lista2 = instancia.list(QueryStrategy.CRITERIA, EntityTest.class, VSPersistence.PARAMS_NONE,
				VSPersistence.START_RESULT_NONE, VSPersistence.MAX_RESULT_NONE);
		Assert.assertTrue(lista2.containsAll(lista1));
	}

	@Test
	public void list2() {
		List<Long> list1 = Arrays.asList(getId(), getId());
		List<Number> list2 = instancia
				.createQueryBuilder(QueryStrategy.NATIVE, "select e.id from entity_test e where e.id in (:ids)")
				.setMaxResults(list1.size()).setParam("ids", list1).list();
		Assert.assertTrue(
				list1.containsAll(list2.stream().map(value -> value.longValue()).collect(Collectors.toList())));
	}

	@Test
	public void iterator() {
		Set<EntityTest> set = new HashSet<>();
		set.add(instancia.find(EntityTest.class, getId()));
		set.add(instancia.find(EntityTest.class, getId()));
		for (Iterator<EntityTest> iterator = instancia.iterator(QueryStrategy.CRITERIA, EntityTest.class,
				VSPersistence.PARAMS_NONE, VSPersistence.START_RESULT_NONE); iterator.hasNext();) {
			set.remove(iterator.next());
		}
		Assert.assertTrue(set.isEmpty());
	}
}