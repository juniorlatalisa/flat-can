package br.com.virtualsistemas.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import br.com.virtualsistemas.persistence.jpa.JPAFacade;
import br.com.virtualsistemas.persistence.jpa.JPAFacade.QueryStrategy;

public abstract class EntidadeFacade<E extends Entidade, K extends Serializable> {

	protected abstract JPAFacade getFacade();

	protected abstract Class<? extends E> getEntityClass();

	public abstract E novo();

	public Optional<E> verificar(K chave) {
		return Optional.ofNullable(pesquisar(chave));
	}
	
	public E pesquisar(K chave) {
		return getFacade().find(getEntityClass(), chave);
	}

	public E inserir(E entidade) {
		return getFacade().insert(entidade);
	}

	public E atualizar(E entidade) {
		return getFacade().update(entidade);
	}
	
	public E recarregar(E entidade) {
		return getFacade().find(getEntityClass(), getChave(entidade));
	}

	public boolean remover(E entidade) {
		return getFacade().delete(getEntityClass(), getChave(entidade));
	}

	public List<E> listar(int limite) {
		return getFacade().createQueryBuilder(QueryStrategy.CRITERIA, getEntityClass())//
				.setMaxResults(limite)//
				.list();
	}

	public List<E> listar() {
		return listar(VSPersistence.MAX_RESULT_NONE);
	}

	@SuppressWarnings("unchecked")
	protected K getChave(E entidade) {
		if (entidade instanceof Codificavel<?>) {
			return (K) ((Codificavel<?>) entidade).getCodigo();
		}
		if (entidade instanceof Identificavel<?>) {
			return (K) ((Identificavel<?>) entidade).getIdentificador();
		}
		throw new RuntimeException("Chava não reconhecida");
	}
}