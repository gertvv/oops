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
 * Tableau rule. Matches labels and formulas.
 */
abstract public class Rule {
	private String d_name;
	private Type d_type;

	protected Rule(String name, Type type) {
		d_name = name;
		d_type = type;
	}

	public Type getType() {
		return d_type;
	}

	abstract public Match match(Node f);

	public String toString() {
		return d_name;
	}

	/**
	 * Rule type enumeration.
	 */
	public enum Type {
		/**
		 * Propositional rule not creating new branches.
		 */
		LINEAR,
		
		/**
		 * Propositional rule creating new branches.
		 */
		SPLIT,
		
		/**
		 * Modal rule matching existing worlds.
		 */
		ACCESS,
		
		/**
		 * Modal rule introducing new worlds.
		 */
		CREATE
	}
}
