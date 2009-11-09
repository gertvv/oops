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

package nl.rug.ai.mas.oops.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nl.rug.ai.mas.oops.formula.Proposition;

/**
 * Represents a valuation (assignment of truth value to propositions)
 */
public class Valuation {
	private HashMap<Proposition, Boolean> d_map;

	public Valuation() {
		d_map = new HashMap<Proposition, Boolean>();
	}

	public Set<Map.Entry<Proposition, Boolean>> entrySet() {
		return d_map.entrySet();
	}

	public void setValue(Proposition p, boolean v) {
		d_map.put(p, new Boolean(v));
	}

	public String toString() {
		return d_map.toString();
	}
}
