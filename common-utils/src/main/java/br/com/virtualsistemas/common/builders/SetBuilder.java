package br.com.virtualsistemas.common.builders;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author juniorlatalisa
 */
public class SetBuilder<E> implements Builder<Set<E>> {

	protected SetBuilder(Set<E> source) {
		this.source = source;
	}

	public SetBuilder() {
		this(new HashSet<>());
	}

	private Set<E> source;

	@Override
	public Set<E> build() {
		return new HashSet<>(source);
	}

	public SetBuilder<E> add(E e) {
		source.add(e);
		return this;
	}

	public SetBuilder<E> remove(E e) {
		source.remove(e);
		return this;
	}

	public SetBuilder<E> addAll(Collection<? extends E> c) {
		source.addAll(c);
		return this;
	}

	public SetBuilder<E> removeAll(Collection<? extends E> c) {
		source.removeAll(c);
		return this;
	}

	public SetBuilder<E> clear() {
		source.clear();
		return this;
	}

	@SafeVarargs
	public static <E> Set<E> build(E... elements) {
		Set<E> source = new HashSet<E>(elements.length);
		for (E e : source) {
			source.add(e);
		}
		return source;
	}
}