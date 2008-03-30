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

	private static final String s_errorInvalidRules = 
		"Default case reached in tableau: invalid rules";
	private static final String s_errorNoMatch = 
		" is not simple, and does not match any rules";

	public Tableau(Vector<Rule> rules) {
		d_rules = rules;
		d_error = null;
		d_observers = new Vector<TableauObserver>();
	}

	public BranchState tableau(Formula f) {
		d_error = null;
		notify(new TableauStartedEvent());
		BranchState result = (new Worker(f)).tableau();
		notify(new TableauFinishedEvent(result));
		return result;
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

	public String getError() {
		return d_error;
	}

	private class Worker { 
		/**
		 * Node that generates this branch.
		 */
		private Node d_node;
		/**
		 * The Branch we are currently working on.
		 */
		private Branch d_branch;
		/**
		 * The Match that resulted in the creation of this branch.
		 */
		private Match d_origin;
		/**
		 * The list of Necessities.
		 */
		private Necessities d_necessities;
		/**
		 * The Queue we are working from.
		 */
		private PriorityQueue<Match> d_queue;
		/**
		 * The result obtained.
		 */
		private BranchState d_result;

		public Worker(Formula f) {
			d_node  = new Node(
				new LabelInstance(
					new NullLabel(),
					new WorldInstance(null),
					new NullAgent()),
				f);
			d_branch = new Branch(null);
			d_origin = null;
			d_necessities = new Necessities(null);
			d_queue = new PriorityQueue<Match>();
			d_result = null;
		}

		public Worker(Worker w, Node n, Match o) {
			d_node = n;
			d_origin = o;
			d_branch = new Branch(w.d_branch);
			d_necessities = new Necessities(w.d_necessities);
			d_queue = new PriorityQueue<Match>(w.d_queue);
			d_result = null;
		}

		public BranchState tableau() {
			if (d_result != null) {
				return d_result;
			}

			Tableau.this.notify(new BranchAddedEvent(d_branch));
			handleNode(d_node, d_origin);
			if (d_result != null) {
				Tableau.this.notify(new BranchDoneEvent(d_branch));
				return d_result;
			}

			// handle all elements on the queue
			while (!d_queue.isEmpty()) {
				Match match = d_queue.poll();
				switch (match.getType()) {
					case SPLIT:
						handleSplit(match);
						break;
					case LINEAR:
						handleLinear(match);
						break;
					case CREATE:
						handleCreate(match);
						break;
					case ACCESS:
						handleLinear(match);
						break;
					default:
						d_error = s_errorInvalidRules;
						return BranchState.ERROR;
				}
				if (d_result != null) {
					Tableau.this.notify(new BranchDoneEvent(d_branch));
					return d_result;
				}
			}

			Tableau.this.notify(new BranchOpenEvent(d_branch));
			Tableau.this.notify(new BranchDoneEvent(d_branch));
			return BranchState.OPEN;
		}

		private void handleSplit(Match match) {
			for (Node n : match.getNodes()) {
				Worker worker = new Worker(this, n, match);
				BranchState result = worker.tableau();
				if (result != BranchState.CLOSED) {
					d_result = result;
					return;
				}
			}
			d_result = BranchState.CLOSED;
		}

		private void handleLinear(Match match) {
			for (Node n : match.getNodes()) {
				if (!d_branch.contains(n)) {
					handleNode(n, match);
					if (d_result != null)
						return;
				}
			}
		}

		private void handleCreate(Match match) {
			for (Node n : match.getNodes()) {
				if (!d_branch.contains(n)) {
					handleNode(n, match);
					if (d_result != null)
						return;
					d_queue.addAll(d_necessities.apply(n.getLabel()));
				}
			}
		}

		private void handleNode(Node n, Match m) {
			Node neg = new Node(n.getLabel(), n.getFormula().opposite());
			if (d_branch.contains(neg)) {
				put(n, m);
				Tableau.this.notify(
					new BranchClosedEvent(d_branch, n, neg));
				d_result = BranchState.CLOSED;
			} else {
				matchPut(n, m);
			}
		}

		private void put(Node n, Match m) {
			d_branch.add(n);
			Tableau.this.notify(new NodeAddedEvent(d_branch, n, m));
		}

		private void matchPut(Node n, Match m) {
			put(n, m);
			Vector<Match> v = match(n);
			if (!v.isEmpty()) {
				for (Match match : v) { // treat necessities specially
					if (match.getType() == Rule.Type.ACCESS) {
						d_necessities.add(match);
						d_queue.addAll(d_branch.apply(match));
					} else {
						d_queue.add(match);
					}
				}
			} else {
				if (!n.getFormula().isSimple()) {
					d_error = n.toString() + s_errorNoMatch;
					d_result = BranchState.ERROR;
				}
			}
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
	}
}
