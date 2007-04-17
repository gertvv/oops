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
		OPEN, CLOSED, ERROR;
	}

	private Vector<Rule> d_rules;
	private String d_error;

	public Tableau(Vector<Rule> rules) {
		d_rules = rules;
		d_error = null;
	}

	public BranchState tableau(Formula f) {
		d_error = null;
		Node n = new Node(new LabelInstance(null,
					new WorldInstance(), new AgentId(null)), f);
		return tableau(n, null, null, new PriorityQueue<Match>());
	}

	private BranchState tableau(Node node, Branch branch,
			Necessities necessities, PriorityQueue<Match> queue) {
		// create data structures for this tableau branch
		queue = new PriorityQueue<Match>(queue);
		branch = new Branch(branch);
		necessities = new Necessities(necessities);

		BranchState result = handleNode(node, branch, queue);
		if (result != BranchState.OPEN)
			return result;

		// handle all elements on the queue
		while (!queue.isEmpty()) {
			Match match = queue.poll();
			switch (match.getType()) {
				case SPLIT:
					for (Node n : match.getNodes()) {
						result = tableau(n, branch, necessities, queue);
						if (result != BranchState.CLOSED)
							return result;
					}
					return BranchState.CLOSED;
				case LINEAR:
					for (Node n : match.getNodes()) {
						result = handleNode(n, branch, queue);
						if (result != BranchState.OPEN)
							return result;
					}
					break;
				case CREATE:
					for (Node n : match.getNodes()) {
						System.out.println("CREATE: " + n);
						System.out.println("BRANCH: ");
						matchPut(n, branch, queue);
						for (Node m : necessities.apply(n.getLabel())) {
							result = handleNode(m, branch, queue);
							if (result != BranchState.OPEN)
								return result;
						}
						System.out.println(branch);
					}
					break;
				case ACCESS:
					for (Node n : match.getNodes()) {
						System.out.println("ACCESS: " + n);
						System.out.println("BRANCH: ");
						necessities.add(n);
						for (Node m : branch.apply(n)) {
							result = handleNode(m, branch, queue);
							if (result != BranchState.OPEN)
								return result;
						}
						System.out.println(branch);
						System.out.println("QUEUE: ");
						System.out.println(queue);
					}
					break;
				default:
					d_error = "Default case reached in tableau: invalid rules";
					return BranchState.ERROR;
			}
		}
		return BranchState.OPEN;
	}

	private BranchState handleNode(Node n, Branch b, PriorityQueue<Match> q) {
		if (!b.contains(n)) {
			if (b.contains(new Node(n.getLabel(), n.getFormula().opposite()))) {
				return BranchState.CLOSED;
			}
			return matchPut(n, b, q);
		}
		return BranchState.OPEN;
	}

	private BranchState matchPut(Node n, Branch b, PriorityQueue<Match> q) {
		b.add(n);
		Match m = match(n);
		if (m != null) {
			q.add(m);
		} else {
			if (!n.getFormula().isSimple()) {
				d_error = n.getFormula().toString() +
					" is not simple, and does not match any rules";
				return BranchState.ERROR;
			}
		}
		return BranchState.OPEN;
	}

	private Match match(Node f) {
		for (Rule r : d_rules) {
			Match m = r.match(f);
			if (m != null)
				return m;
		}
		return null;
	}

	public String getError() {
		return d_error;
	}
}
