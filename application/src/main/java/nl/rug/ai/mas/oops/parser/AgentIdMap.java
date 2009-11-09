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

package nl.rug.ai.mas.oops.parser;

import java.util.*;

import nl.rug.ai.mas.oops.formula.AgentId;

/**
 * A map from agent symbols to a class implementing them. This enables
 * identification of different agents.
 */
public class AgentIdMap extends HashMap<String, AgentId> {
	private int d_code;

	public AgentIdMap() {
		super();
		d_code = 0;
	}

	/**
	 * Get a reference to an AgentId, either an existing one having the supplied
	 * name, or a new one if one doesn't exist.
	 */
	public AgentId getOrCreate(String name) {
		AgentId id = get(name);
		if (id != null)
			return id;
		d_code++;
		id = new AgentId(name, d_code);
		put(name, id);
		return id;
	}

	public Set<AgentId> getAgentSet() {
		HashSet<AgentId> set = new HashSet<AgentId>();
		set.addAll(values());
		return set;
	}
}
