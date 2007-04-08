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

public class Rule {
	public enum Type {
		LINEAR, SPLIT
	}

	private String d_name;
	private Type d_type;
	private Formula d_template;
	private Vector<Formula> d_rewrite;

	public Rule(String name, Formula tpl, Type type, Vector<Formula> rwt) {
		d_name = name;
		d_template = tpl;
		d_type = type;
		d_rewrite = rwt;
	}

	public Type getType() {
		return d_type;
	}

	public Match match(Formula f) {
		FullSubstitution sub = d_template.match(f);
		if (sub == null)
			return null;
		Vector<Formula> result = new Vector<Formula>(d_rewrite.size());
		for (int i = 0; i < d_rewrite.size(); ++i) {
			result.add(d_rewrite.get(i).substitute(sub));
		}
		return new Match(this, result);
	}

	public String toString() {
		return d_name;
	}
}
