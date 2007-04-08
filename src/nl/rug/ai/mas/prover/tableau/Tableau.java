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

public class Tableau {
	public enum BranchState {
		OPEN, CLOSED;
	}

	private Vector<Rule> d_rules;

	public Tableau(Vector<Rule> rules) {
		d_rules = rules;
	}

	public BranchState tableau(Formula f) {
		return tableau(f, null, new PriorityQueue<Match>());
	}

	public BranchState tableau(Formula f, Branch b, PriorityQueue<Match> q) {
		// create data structures for this tableau branch
		q = new PriorityQueue<Match>(q);
		b = new Branch(b);

		// handle formula f
		if (!b.contains(f)) {
			if (b.contains(f.opposite())) {
				return BranchState.CLOSED;
			}
			b.add(f);
			Match m = match(f);
			if (m != null)
				q.add(m);
		}

		// handle all elements on the queue
		while (!q.isEmpty()) {
			Match m = q.poll();
			if (m.getType() == Rule.Type.SPLIT) {
				for (Formula g : m.getFormulas()) {
					if (tableau(g, b, q) == BranchState.OPEN)
						return BranchState.OPEN;
				}
				return BranchState.CLOSED;
			} else { // LINEAR
				for (Formula g : m.getFormulas()) {
					if (!b.contains(g)) {
						if (b.contains(g.opposite()))
							return BranchState.CLOSED;
						b.add(g);
						Match n = match(g);
						if (n != null)
							q.add(n);
					}
				}
			}
		}
		return BranchState.OPEN;
	}

	public Match match(Formula f) {
		for (Rule r : d_rules) {
			Match m = r.match(f);
			if (m != null)
				return m;
		}
		return null;
	}
}
