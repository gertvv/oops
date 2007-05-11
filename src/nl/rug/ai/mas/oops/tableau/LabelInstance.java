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

/**
 * A concrete Label, consisting of a parent Label, a World and an Agent.
 */
public class LabelInstance implements Label {
	private Label d_parent;
	private World d_world;
	private Agent d_agent;

	public LabelInstance(Label parent, World world, Agent agent) {
		d_parent = parent;
		d_world = world;
		d_agent = agent;
	}

	public LabelSubstitution match(Label o) {
		try {
			LabelInstance other = (LabelInstance)o;

			Substitution<World> sw = d_world.match(other.d_world);
			if (sw == null)
				return null;

			Substitution<Agent> sa = d_agent.match(other.d_agent);
			if (sa == null)
				return null;

			LabelSubstitution s = new LabelSubstitution(sw, sa);

			if (d_parent == null) {
				if (other.d_parent == null) {
					return s;
				}
				return null;
			}

			LabelSubstitution ps = d_parent.match(other.d_parent);
			if (ps == null)
				return null;

			if (s.merge(ps)) {
				return s;
			}
		} catch (ClassCastException e) {
		}
		return null;
	}

	public Label substitute(LabelSubstitution s) {
		Agent a = d_agent.substitute(s.getAgentSubstitution());
		World w = d_world.substitute(s.getWorldSubstitution());
		Label p = d_parent.substitute(s);
		return new LabelInstance(p, w, a);
	}

	/**
	 * A LabelInstance equals() other LabelInstance objects if they have
	 * equal parent, world and agent fields.
	 */
	public boolean equals(Object o) {
		if (o == null)
			return false;
		try {
			LabelInstance other = (LabelInstance)o;
			if (((d_parent == null && other.d_parent == null) ||
					(d_parent != null && d_parent.equals(other.d_parent))) &&
					d_world.equals(other.d_world) && d_agent.equals(other.d_agent))
				return true;
		} catch (ClassCastException e) {
		}
		return false;
	}

	/**
	 * The hashCode() is equal to 1 * 31 + parent.hashCode * 31 + 
	 * world.hashCode * 31 + agent.hashCode * 31.
	 */
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + d_parent.hashCode();
		hash = hash * 31 + d_world.hashCode();
		hash = hash * 31 + d_agent.hashCode();
		return hash;
	}

	public String toString() {
		String s = "";
		if (d_parent != null)
			s = d_parent.toString() + ".";
		s += "(" + d_world + "," + d_agent + ")";
		return s;
	}
}
