package nl.rug.ai.mas.oops.parser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import nl.rug.ai.mas.oops.formula.AgentId;

/**
 * A view on the set of AgentIds contained in the AgentIdMap. Always up-to-date.
 */
public class AgentIdView implements Set<AgentId> {
	private AgentIdMap d_map;

	public AgentIdView(AgentIdMap map) {
		d_map = map;
	}
	
	private Set<AgentId> getInternal() {
		return new HashSet<AgentId>(d_map.values());
	}

	public boolean add(AgentId arg0) {
		return false;
	}

	public boolean addAll(Collection<? extends AgentId> arg0) {
		return false;
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean contains(Object obj) {
		return getInternal().contains(obj);
	}

	public boolean containsAll(Collection<?> obj) {
		return getInternal().containsAll(obj);
	}

	public boolean isEmpty() {
		return getInternal().isEmpty();
	}

	public Iterator<AgentId> iterator() {
		return Collections.unmodifiableSet(getInternal()).iterator();
	}

	public boolean remove(Object arg0) {
		return false;
	}

	public boolean removeAll(Collection<?> arg0) {
		return false;
	}

	public boolean retainAll(Collection<?> arg0) {
		return false;
	}

	public int size() {
		return getInternal().size();
	}

	public Object[] toArray() {
		return getInternal().toArray();
	}

	public <T> T[] toArray(T[] arg0) {
		return getInternal().toArray(arg0);
	}
}