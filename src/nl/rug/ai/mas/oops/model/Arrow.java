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

import nl.rug.ai.mas.oops.formula.AgentId;

/**
 * Represents an accessibility relation between two Worlds, for a given Agent.
 */
public class Arrow {
	private AgentId d_agent;
	private World d_source;
	private World d_target;

	public Arrow(AgentId agent, World source, World target) {
		d_agent = agent;
		d_source = source;
		d_target = target;
	}

	public boolean equals(Object o) {
		try {
			Arrow other = (Arrow)o;
			if (other.d_agent.equals(d_agent) &&
					other.d_source.equals(d_source) &&
					other.d_target.equals(d_target)) {
				return true;
			}
		} catch (ClassCastException e) {
		}
		return false;
	}

	public AgentId getAgent() {
		return d_agent;
	}

	public World getSource() {
		return d_source;
	}

	public World getTarget() {
		return d_target;
	}

	public String toString() {
		return d_agent.toString();
	}
}
