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

package nl.rug.ai.mas.oops.tableau;

import java.util.*;
import nl.rug.ai.mas.oops.formula.*;

public class WorldInstance implements World {
	private Formula d_formula;

	/**
	 * World introduction is based on a formula. Therefore, a worlds identity
	 * is based on the formula that defined it.
	 */
	public WorldInstance(Formula f) {
		d_formula = f;
	}

	/**
	 * Worlds are equal iff their d_formula are equal.
	 */
	public NodeSubstitution match(World o) {
		try {
			WorldInstance other = (WorldInstance)o;

			// first handle null-cases
			if (this.d_formula == null && other.d_formula == null) {
				return new NodeSubstitution();
			}
			if (this.d_formula == null || other.d_formula == null) {
				return null;
			}

			FullSubstitution fs = d_formula.match(other.d_formula);
			if (fs == null) {
				return null;
			}

			NodeSubstitution ns = new NodeSubstitution();
			ns.merge(fs);
			return ns;
		} catch (ClassCastException e) {
			// o is not another WorldInstance - no match
		}
		return null;
	}

	public World substitute(NodeSubstitution ns) {
		if (d_formula != null) {
			FullSubstitution fs =
				new FullSubstitution(ns.getAgentSubstitution(),
					ns.getFormulaSubstitution());
			return new WorldInstance(d_formula.substitute(fs));
		}
		return this;
	}

	public boolean equals(Object o) {
		if (o == null)
			return false;
		try {
			WorldInstance other = (WorldInstance)o;
			if ((d_formula == null && other.d_formula == null) ||
					d_formula.equals(other.d_formula)) {
				return true;
			}
		} catch (ClassCastException e) {
		}
		return false;
	}

	public int hashCode() {
		int hash = 1;
		/* FIXME: implement hashCode() for formulas
		if (d_formula != null) {
			hash = hash * 31 + d_formula.hashCode();
		}
		*/
		return hash;
	}

	public String toString() {
		return "World(" + (d_formula == null ? "null" : 
			d_formula.toString()) + ")";
	}
}
