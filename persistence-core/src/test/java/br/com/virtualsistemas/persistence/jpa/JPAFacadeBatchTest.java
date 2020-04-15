package br.com.virtualsistemas.persistence.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.com.virtualsistemas.persistence.EntityTest;
import br.com.virtualsistemas.persistence.TestUtil;

public class JPAFacadeBatchTest {

	protected static EntityTest create(Double value) {
		EntityTest entity = new EntityTest();
		entity.setName(UUID.randomUUID().toString());
		entity.setValue(value);
		return entity;
	}

	protected static List<EntityTest> create(int size) {
		List<EntityTest> retorno = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			retorno.add(create(1.3 * 1));
		}
		return retorno;
	}

	protected static void insert1(JPAFacade instancia) {
		Date inicio;
		EntityTest entity;
		List<EntityTest> entities;
		long delay1, delay2;
		StringBuilder log = new StringBuilder("\nInsere 1");
		for (int i = 0; i < 50; i++) {
			entity = create(1.89 * i);
			inicio = new Date();
			instancia.insert(entity);
			delay1 = new Date().getTime() - inicio.getTime();
			log.append("\n\tInsert comum (").append(i).append(") .:\t").append(delay1);

			entities = Arrays.asList(create(1.89));
			inicio = new Date();
			instancia.insert(entities);
			delay2 = new Date().getTime() - inicio.getTime();
			log.append("\n\tInsert list (").append(i).append(") ..:\t").append(delay2);

			log.append("\n\tInsert diff (").append(i).append(") ..:\t").append(delay1 - delay2).append('\n');
		}
		System.out.println(log);
	}

	protected static void insertMultiplos(JPAFacade instancia) {
		Date inicio;
		List<EntityTest> entities;
		long delay1, delay2, diff;
		Long maior = null, menor = null;
		double maiorP = 0.0, menorP = 0.0;
		StringBuilder log = new StringBuilder("\nInsere Multiplos");
		for (int i = 0; i < 50; i++) {
			entities = create(100);
			inicio = new Date();
			entities.forEach(entity -> instancia.insert(entity));
			delay1 = new Date().getTime() - inicio.getTime();
			log.append("\n\tInsert comum (").append(i).append(") .:\t").append(delay1);

			entities = create(100);
			inicio = new Date();
			instancia.insert(entities);
			delay2 = new Date().getTime() - inicio.getTime();
			log.append("\n\tInsert list (").append(i).append(") ..:\t").append(delay2);

			log.append("\n\tInsert diff (").append(i).append(") ..:\t").append(diff = delay1 - delay2).append('\n');

			if (maior == null) {
				maior = Long.MIN_VALUE;
			} else if (diff > maior) {
				maior = diff;
				maiorP = diff * 100 / (delay2 / 1.0);
			}
			if (menor == null) {
				menor = Long.MAX_VALUE;
			} else if (diff < menor) {
				menor = diff;
				menorP = diff * 100 / (delay2 / 1.0);
			}
		}
		System.out.println(log);
		System.out.println(String.format("Diff maior %d, menor %d", maior, menor));
		System.out.println(String.format("Diff P maior %f, menor %f", maiorP, menorP));
	}

	public static void main(String[] args) {
		ResourceLocalFacade instancia = new ResourceLocalFacade(TestUtil.getFactory().createEntityManager());
		try {
			insertMultiplos(instancia);
		} finally {
			instancia.getEntityManager().close();
		}
	}
}