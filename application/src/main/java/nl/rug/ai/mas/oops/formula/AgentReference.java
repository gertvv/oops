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
 * Wraps a VariableReference&lt;Agent&gt; such that it can be used as an Agent.
 */
public class AgentReference extends VariableReference<Agent>
implements Agent {
	private int d_code;

	public AgentReference(Variable<Agent> var, int code) {
		super(var);
		d_code = 2 * code + 1;
	}

	/**
	 * An AgentReference matches any other Agent, returning a substitution of
	 * it's own Variable to the provided Agent.
	 * @param a The agent to match.
	 */
	public Substitution<Agent> match(Agent a) {
		Substitution<Agent> s = new Substitution<Agent>();
		s.put(get(), a);
		return s;
	}

	/**
	 * Substitutes this Variable for the value it is given in the Substitution.
	 * If the Variable does not occur in the Substitution, the Variable itself
	 * is returned.
	 */
	public Agent substitute(Substitution<Agent> s) {
		Agent a = s.get(get());
		if (a != null)
			return a;
		return this;
	}

	public boolean isConcrete() {
		return false;
	}

	public int code() {
		return d_code;
	}

	public int d_hashCode() {
		return d_code;
	}
}
