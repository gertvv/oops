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

/**
 * Represents a justification for a match. That is, the rule that created the
 * match and the node that the rule matched on.
 */
public class Justification {
	private Rule d_rule;
	private Node d_node;

	public Justification(Rule rule, Node node) {
		d_rule = rule;
		d_node = node;
	}

	public Rule getRule() {
		return d_rule;
	}

	public Node getNode() {
		return d_node;
	}
}
