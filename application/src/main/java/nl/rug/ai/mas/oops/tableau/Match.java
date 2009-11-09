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

public class Match implements Comparable<Match> {
	private Rule d_rule;
	private Vector<Node> d_nodes;
	private Justification d_just;
	private int d_badness;

	public Match(Rule rule, Node origin, Vector<Node> nodes) {
		d_rule = rule;
		d_nodes = nodes;
		d_just = new Justification(rule, origin);
		d_badness = 1;
		if (rule.getType() == Rule.Type.LINEAR) {
			if (nodes.size() > 1) {
				d_badness = 2;
			}
		} else if (rule.getType() == Rule.Type.SPLIT) {
			d_badness = 4;
		} else if (rule.getType() == Rule.Type.ACCESS) {
			d_badness = 3;
		} else if (rule.getType() == Rule.Type.CREATE) {
			d_badness = 5;
		}
	}

	public Match(Rule r, Node origin, Node n) {
		this(r, origin, nodeVector(n));
	}

	private static Vector<Node> nodeVector(Node n) {
		Vector<Node> v = new Vector<Node>();
		v.add(n);
		return v;
	}

	public Rule.Type getType() {
		return d_rule.getType();
	}

	public Vector<Node> getNodes() {
		return d_nodes;
	}

	public Rule getRule() {
		return d_rule;
	}

	public Justification getJustification() {
		return d_just;
	}

	public int compareTo(Match other) {
		return d_badness - other.d_badness;
	}
	
	public String toString() {
		return d_rule.toString() + d_nodes.toString();
	}

	public boolean isConcrete() {
		for (Node n : d_nodes) {
			if (!n.isConcrete())
				return false;
		}
		return true;
	}

	public Vector<Match> apply(Label l) {
		Vector<Match> result = new Vector<Match>();
		for (Node n : d_nodes) {
			NodeSubstitution nsub = n.getLabel().match(l);
			if (nsub != null)
				result.add(
					new Match(d_rule, d_just.getNode(), n.substitute(nsub)));
		}
		return result;
	}
}
