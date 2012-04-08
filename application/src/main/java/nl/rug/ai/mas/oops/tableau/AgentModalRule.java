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

import nl.rug.ai.mas.oops.formula.Agent;
import nl.rug.ai.mas.oops.formula.AgentReference;
import nl.rug.ai.mas.oops.formula.Formula;
import nl.rug.ai.mas.oops.formula.FullSubstitution;
import nl.rug.ai.mas.oops.parser.Context;

public class AgentModalRule extends Rule {
	protected Formula d_template;
	protected Formula d_rewrite;
	protected Context d_context;
	protected AgentReference d_agentRef;

	protected AgentModalRule(String name, String html, Type type,
			Formula tpl, Formula rwt, Context c, AgentReference ref) {
		super(name, html, type);
		d_template = tpl;
		d_rewrite = rwt;
		d_context = c;
		d_agentRef = ref;
	}

	public Match match(Node n) {
		FullSubstitution s = d_template.match(n.getFormula());
		if (s == null)
			return null;
		Vector<Node> v = new Vector<Node>();
	
		for (Agent agent : d_context.getAgentIdView()) {
			
			s.put(d_agentRef.get(), agent);
			v.add(new Node(n.getLabel(), d_rewrite.substitute(s)));
		}
		
		return new Match(this, n, v);
	}

	public Formula getTemplate() {
		return d_template;
	}

	public Formula getRewrite() {
		return d_rewrite;
	}
}
