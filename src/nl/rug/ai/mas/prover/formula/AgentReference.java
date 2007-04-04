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

/**
 * Holds a reference to a Variable object, used for unification of variables.
 */
public class AgentReference extends VariableReference<Agent>
implements Agent {
	public AgentReference(Variable<Agent> var) {
		super(var);
	}

	public Substitution<Agent> match(Agent f) {
		Substitution<Agent> s = new Substitution<Agent>();
		s.put(get(), f);
		return s;
	}
}

