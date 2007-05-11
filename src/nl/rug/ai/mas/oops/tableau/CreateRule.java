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

public class CreateRule extends ModalRule {
	public CreateRule(String name, Node tpl, Node rwt, Constraint c) {
		super(name, Type.CREATE, tpl, rwt, c);
	}

	public CreateRule(String name, Node tpl, Node rwt) {
		this(name, tpl, rwt, null);
	}

	/**
	 * Matching a create rule will result in a label with a free world
	 * variable.
	 */
	public Match match(Node n) {
		// perform template match
		NodeSubstitution s = d_template.match(n, d_constraint);
		if (s == null)
			return null;

		// perform rewrite
		Node rwt = d_rewrite.substitute(s);

		// create result set
		Vector<Node> v = new Vector<Node>();
		v.add(rwt);
		return new Match(this, v);
	}
}
