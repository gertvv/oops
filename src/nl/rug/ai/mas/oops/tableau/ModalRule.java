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

package nl.rug.ai.mas.prover.tableau;

import java.util.*;
import nl.rug.ai.mas.prover.formula.*;

public class ModalRule extends Rule {
	protected Node d_template;
	protected Node d_rewrite;
	protected Constraint d_constraint;

	protected ModalRule(String name, Type type,
			Node tpl, Node rwt, Constraint c) {
		super(name, type);
		d_template = tpl;
		d_rewrite = rwt;
		d_constraint = c;
	}

	public Match match(Node n) {
		NodeSubstitution s = d_template.match(n, d_constraint);
		if (s == null)
			return null;
		Vector<Node> v = new Vector<Node>();
		v.add(d_rewrite.substitute(s));
		return new Match(this, v);
	}

	public Node getTemplate() {
		return d_template;
	}

	public Node getRewrite() {
		return d_rewrite;
	}

	public Constraint getConstraint() {
		return d_constraint;
	}
}
