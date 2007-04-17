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

public class Match implements Comparable<Match> {
	private Rule d_rule;
	private Vector<Node> d_nodes;
	private int d_badness;

	public Match(Rule rule, Vector<Node> nodes) {
		d_rule = rule;
		d_nodes = nodes;
		d_badness = 1;
		if (rule.getType() == Rule.Type.LINEAR) {
			if (nodes.size() > 1) {
				d_badness = 2;
			}
		} else if (rule.getType() == Rule.Type.SPLIT) {
			d_badness = 5;
		} else if (rule.getType() == Rule.Type.ACCESS) {
			d_badness = 3;
		} else if (rule.getType() == Rule.Type.CREATE) {
			d_badness = 4;
		}
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

	public int compareTo(Match other) {
		return other.d_badness - d_badness;
	}
	
	public String toString() {
		return d_rule.toString() + d_nodes.toString();
	}
}
