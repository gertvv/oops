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

public class Node {
	private Label d_label;
	private Formula d_formula;

	public Node(Label l, Formula f) {
		d_label = l;
		d_formula = f;
	}

	public Label getLabel() {
		return d_label;
	}
	
	public Formula getFormula() {
		return d_formula;
	}

	public NodeSubstitution match(Node other, Constraint c) {
		NodeSubstitution s = new NodeSubstitution();
		NodeSubstitution ls = d_label.match(other.d_label);
		FullSubstitution fs = d_formula.match(other.d_formula);

		if (ls != null && fs != null && s.merge(ls) && s.merge(fs) &&
			(c == null || c.validate(s)))
			return s;

		return null;
	}

	public Node substitute(NodeSubstitution s) {
		FullSubstitution fs = new FullSubstitution(s.getAgentSubstitution(),
				s.getFormulaSubstitution());
		return new Node(d_label.substitute(s), d_formula.substitute(fs));
	}

	public boolean equals(Object o) {
		try {
			Node other = (Node)o;
			if (d_label.equals(other.d_label) &&
					d_formula.equals(other.d_formula))
				return true;
		} catch (ClassCastException e) {
		}
		return false;
	}

	public String toString() {
		return "{" + d_label.toString() + "," + d_formula.toString() + "}";
	}

	public boolean isConcrete() {
		return d_label.isConcrete() && d_formula.isConcrete();
	}
}
