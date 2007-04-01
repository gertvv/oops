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

package nl.rug.ai.mas.prover.formula;

import java.util.*;

/**
 * A map from variable names to a class implementing them.
 */
public class VariableMap extends HashMap<String, Variable> {
	public VariableMap() {
		super();
	}

	public Variable getOrCreate(String name) {
		Variable v = get(name);
		if (v != null)
			return v;
		v = new Variable(name);
		put(name, v);
		return v;
	}

	public void merge(VariableMap other) {
		for (Map.Entry<String, Variable> e : other.entrySet()) {
			String name = e.getKey();
			Variable var = get(name);
			if (var != null) {
				var.merge(e.getValue());
			} else {
				put(name, e.getValue());
			}
		}
		other.clear();
	}
}
