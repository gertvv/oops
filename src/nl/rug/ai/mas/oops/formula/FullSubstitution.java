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

/**
 * A substitution, both agents and formulas for variables.
 */
public class FullSubstitution {
	private Substitution<Formula> d_fsub;
	private Substitution<Agent> d_asub;

	public FullSubstitution() {
		d_fsub = new Substitution<Formula>();
		d_asub = new Substitution<Agent>();
	}

	public FullSubstitution(Substitution<Agent> a) {
		d_asub = a;
		d_fsub = new Substitution<Formula>();
	}

	public FullSubstitution(Substitution<Agent> a, Substitution<Formula> f) {
		d_asub = a;
		d_fsub = f;
	}

	public boolean merge(FullSubstitution other) {
		if (!d_fsub.merge(other.d_fsub))
			return false;
		if (!d_asub.merge(other.d_asub))
			return false;
		return true;
	}

	public Formula put(Variable<Formula> k, Formula v) {
		return d_fsub.put(k, v);
	}

	public Formula get(Variable<Formula> k) {
		return d_fsub.get(k);
	}

	public Agent put(Variable<Agent> k, Agent v) {
		return d_asub.put(k, v);
	}

	public Agent get(Variable<Agent> k) {
		return d_asub.get(k);
	}

	public Substitution<Agent> getAgentSubstitution() {
		return d_asub;
	}

	public Substitution<Formula> getFormulaSubstitution() {
		return d_fsub;
	}

	public String toString() {
		return d_asub.toString() + d_fsub;
	}
}
