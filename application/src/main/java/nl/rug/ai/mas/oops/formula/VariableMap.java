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

package nl.rug.ai.mas.oops.formula;

import java.util.*;

/**
 * A map from variable names to a class implementing them.
 */
@SuppressWarnings("serial")
public class VariableMap<T> extends HashMap<String, Variable<T>> {
	public VariableMap() {
		super();
	}

	public Variable<T> getOrCreate(String name) {
		Variable<T> v = get(name);
		if (v != null)
			return v;
		v = new Variable<T>(name);
		put(name, v);
		return v;
	}

	public void merge(VariableMap<T> other) {
		for (Map.Entry<String, Variable<T>> e : other.entrySet()) {
			String name = e.getKey();
			Variable<T> var = get(name);
			if (var != null) {
				var.merge(e.getValue());
			} else {
				put(name, e.getValue());
			}
		}
		other.clear();
	}
}
