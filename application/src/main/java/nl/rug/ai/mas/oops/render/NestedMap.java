/**
  * This program (working title: MAS Prover) is an automated tableaux prover
  * for epistemic logic (S5n).
  * Copyright (C) 2007  Elske van der Vaart and Gert van Valkenhoef

  * This program is free software; you can redistribute it and/or modify it
  * under the terms of the GNU General Public License version 2 as published
  * by the Free Software Foundation.

  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.

  * You should have received a copy of the GNU General Public License along
  * with this program; if not, write to the Free Software Foundation, Inc.,
  * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
  */
package nl.rug.ai.mas.oops.render;

import java.util.*;

public class NestedMap<K0, K1, V> implements Map<K1, V> {
	private HashMap<K0, HashMap<K1, V>> d_data;
	private K0 d_current;

	public NestedMap() {
		d_data = new HashMap<K0, HashMap<K1, V>>();
		d_current = null;
	}

	public void clear() {
		d_data.clear();
	}

	public boolean containsKey(Object key) {
		for (HashMap<K1, V> m : d_data.values()) {
			if (m.containsKey(key))
				return true;
		}
		return false;
	}

	public boolean containsValue(Object value) {
		for (HashMap<K1, V> m : d_data.values()) {
			if (m.containsValue(value))
				return true;
		}
		return false;
	}

	public Set<Map.Entry<K1,V>> entrySet() {
		Set<Map.Entry<K1,V>> set = new HashSet<Map.Entry<K1,V>>();
		for (HashMap<K1, V> m : d_data.values()) {
			set.addAll(m.entrySet());
		}
		return set;
	}

	public boolean equals(Object o) {
		return o == this;
	}

	public V get(Object key) {
		for (HashMap<K1, V> m : d_data.values()) {
			V v = m.get(key);
			if (v != null)
				return v;
		}
		return null;
	}

	public int hashCode() {
		return d_data.hashCode();
	}

	public boolean isEmpty() {
		for (HashMap<K1, V> m : d_data.values()) {
			if (!m.isEmpty())
				return false;
		}
		return true;
	}

	public Set<K1> keySet() {
		Set<K1> set = new HashSet<K1>();
		for (HashMap<K1, V> m : d_data.values()) {
			set.addAll(m.keySet());
		}
		return set;
	}

	public V put(K1 key, V value) {
		if (d_current == null || d_data.get(d_current) == null) {
			throw new Error("Using uninitialized NestedMap");
		}
		return d_data.get(d_current).put(key, value);
	}

	public void putAll(Map<? extends K1, ? extends V> t) {
		for (Map.Entry<? extends K1, ? extends V> e : t.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	public V remove(Object key) {
		throw new Error("Not Implemented");
	}

	public int size() {
		int size = 0;
		for (HashMap<K1, V> m : d_data.values()) {
			size += m.size();
		}
		return size;
	}

	public Collection<V> values() {
		ArrayList<V> l =  new ArrayList<V>();
		for (HashMap<K1, V> m : d_data.values()) {
			l.addAll(m.values());
		}
		return l;
	}

	public void addMap(K0 key) {
		d_data.put(key, new HashMap<K1, V>());
		d_current = key;
	}

	public void removeMap(K0 key) {
		d_data.remove(key);
	}
}
