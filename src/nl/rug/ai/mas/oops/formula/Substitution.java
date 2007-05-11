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
 * A substitution of values for variables
 */
public class Substitution<T> extends HashMap<Variable<T>, T> {
	public Substitution() {
		super();
	}

	public boolean merge(Substitution<T> other) {
		for (Map.Entry<Variable<T>, T> e : other.entrySet()) {
			T v = get(e.getKey());
			if (v == null) {
				put(e.getKey(), e.getValue());
			} else {
				if (!v.equals(e.getValue())) {
					return false;
				}
			}
		}
		return true;
	}

	public String toString() {
		Iterator<Map.Entry<Variable<T>, T>> it = entrySet().iterator();
		String s = "[";
		while (it.hasNext()) {
			Map.Entry<Variable<T>, T> e = it.next();
			s += e.getValue() + "/" + e.getKey();
			if (it.hasNext())
				s += ", ";
		}
		s += "]";
		return s;
	}
}
