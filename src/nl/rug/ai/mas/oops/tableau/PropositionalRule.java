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

public class PropositionalRule extends Rule {
	private Formula d_template;
	private Vector<Formula> d_rewrite;

	protected PropositionalRule(String name, Type type,
			Formula tpl, Vector<Formula> rwt) {
		super(name, type);
		d_template = tpl;
		d_rewrite = rwt;
	}

	public Match match(Node n) {
		FullSubstitution sub = d_template.match(n.getFormula());
		if (sub == null)
			return null;
		Vector<Node> result = new Vector<Node>(d_rewrite.size());
		for (int i = 0; i < d_rewrite.size(); ++i) {
			result.add(new Node(n.getLabel(),
					d_rewrite.get(i).substitute(sub)));
		}
		return new Match(this, result);
	}
}
