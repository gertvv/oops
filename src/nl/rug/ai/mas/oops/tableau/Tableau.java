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

public class Tableau {
	public enum BranchState {
		OPEN, CLOSED, ERROR;
	}

	private Vector<Rule> d_rules;
	private String d_error;
	private Vector<TableauObserver> d_observers;

	public Tableau(Vector<Rule> rules) {
		d_rules = rules;
		d_error = null;
		d_observers = new Vector<TableauObserver>();
	}

	public BranchState tableau(Formula f) {
		d_error = null;
		Node n = new Node(
				new LabelInstance(new NullLabel(), new WorldInstance(null),
					new NullAgent()), f);
		notify(new TableauStartedEvent());
		BranchState result = tableau(n, null, null, null, new PriorityQueue<Match>());
		notify(new TableauFinishedEvent(result));
		return result;
	}

	private BranchState tableau(Node node, Branch branch, Match origin,
			Necessities necessities,
			PriorityQueue<Match> queue) {
		// create data structures for this tableau branch
		queue = new PriorityQueue<Match>(queue);
		branch = new Branch(branch);
		necessities = new Necessities(necessities);
		notify(new BranchAddedEvent(branch));

		BranchState result = handleNode(node, branch, origin, queue, necessities);
		if (result != BranchState.OPEN)
			return result;

		// handle all elements on the queue
		while (!queue.isEmpty()) {
			Match match = queue.poll();
			switch (match.getType()) {
				case SPLIT:
					for (Node n : match.getNodes()) {
						result = tableau(n, branch, match, necessities, queue);
						if (result != BranchState.CLOSED)
							return result;
					}
					return BranchState.CLOSED;
				case LINEAR:
					for (Node n : match.getNodes()) {
						result = handleNode(n, branch, match, queue, necessities);
						if (result != BranchState.OPEN)
							return result;
					}
					break;
				case CREATE:
					for (Node n : match.getNodes()) {
						if (!branch.contains(n)) {
							result = handleNode(n, branch, match, queue, necessities);
							if (result != BranchState.OPEN)
								return result;
							queue.addAll(necessities.apply(n.getLabel()));
						}
					}
					break;
				case ACCESS:
					for (Node n : match.getNodes()) {
						result = handleNode(n, branch, match, queue, necessities);
						if (result != BranchState.OPEN)
							return result;
					}
					break;
				default:
					d_error = "Default case reached in tableau: invalid rules";
					return BranchState.ERROR;
			}
		}

		notify(new BranchOpenEvent(branch));
		return BranchState.OPEN;
	}

	private BranchState handleNode(Node n, Branch b, Match m,
			PriorityQueue<Match> q,
			Necessities nec) {
		if (!b.contains(n)) {
			if (b.contains(new Node(n.getLabel(), n.getFormula().opposite()))) {
				put(n, b, m);
				notify(new BranchClosedEvent(b));
				return BranchState.CLOSED;
			}
			return matchPut(n, b, m, q, nec);
		}
		return BranchState.OPEN;
	}

	private void put(Node n, Branch b, Match m) {
		b.add(n);
		notify(new NodeAddedEvent(b, n, m));
	}

	private BranchState matchPut(Node n, Branch b, Match m,
			PriorityQueue<Match> q,
			Necessities nec) {
		put(n, b, m);
		Vector<Match> v = match(n);
		if (!v.isEmpty()) {
			// q.addAll(m);
			for (Match match : v) { // treat necessities specially
				if (match.getType() == Rule.Type.ACCESS) {
					nec.add(match);
					q.addAll(b.apply(match));
				} else {
					q.add(match);
				}
			}
		} else {
			if (!n.getFormula().isSimple()) {
				d_error = n.toString() +
					" is not simple, and does not match any rules";
				return BranchState.ERROR;
			}
		}
		return BranchState.OPEN;
	}

	private Vector<Match> match(Node f) {
		Vector<Match> result = new Vector<Match>();
		for (Rule r : d_rules) {
			Match m = r.match(f);
			if (m != null)
				result.add(m);
		}
		return result;
	}

	public String getError() {
		return d_error;
	}

	public void attachObserver(TableauObserver o) {
		d_observers.add(o);
	}

	public void detachObserver(TableauObserver o) {
		d_observers.remove(o);
	}

	private void notify(TableauEvent e) {
		for (TableauObserver o : d_observers) {
			o.update(this, e);
		}
	}
}
