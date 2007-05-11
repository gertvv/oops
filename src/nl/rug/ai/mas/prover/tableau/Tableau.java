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
		Node n = new Node(
				new LabelInstance(new NullLabel(), new WorldInstance(),
					new AgentId("NoAgent")), f);
		return tableau(n, null, null, null, new PriorityQueue<Match>());
	}

	private BranchState tableau(Node node, Branch branch,
			Necessities necessities, CreationRules creationRules,
			PriorityQueue<Match> queue) {
		// create data structures for this tableau branch
		queue = new PriorityQueue<Match>(queue);
		branch = new Branch(branch);
		necessities = new Necessities(necessities);
		creationRules = new CreationRules(creationRules);

		BranchState result = handleNode(node, branch, queue);
		if (result != BranchState.OPEN)
			return result;

		// handle all elements on the queue
		while (!queue.isEmpty()) {
			Match match = queue.poll();
			switch (match.getType()) {
				case SPLIT:
					for (Node n : match.getNodes()) {
						result = tableau(n, branch, necessities, creationRules,
							queue);
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
						Node concrete = concretize(n);
						if (creationRules.entails(concrete)) {
							System.out.println("NOT NEW");
							continue;
						}
						creationRules.add(n);
						System.out.println("CREATION RULES: ");
						System.out.println(creationRules);
						matchPut(concrete, branch, queue);
						for (Node m : necessities.apply(concrete.getLabel())) {
							result = handleNode(m, branch, queue);
							if (result != BranchState.OPEN)
								return result;
						}
						System.out.println("BRANCH: ");
						System.out.println(branch);
					}
					break;
				case ACCESS:
					for (Node n : match.getNodes()) {
						System.out.println("ACCESS: " + n);
						necessities.add(n);
						for (Node m : branch.apply(n)) {
							System.out.println("Applied to " + m.getLabel());
							result = handleNode(m, branch, queue);
							if (result != BranchState.OPEN)
								return result;
						}
						System.out.println("BRANCH: ");
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
		Vector<Match> m = match(n);
		if (!m.isEmpty()) {
			q.addAll(m);
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
	
	/**
	 * This is a hack that could have been done in a more generic way (provided
	 * a way to check for free variables in the label).
	 */
	private Node concretize(Node rwt) {
		// create a pattern to match rwt
		Variable<World> w = new Variable<World>("W");
		WorldReference wref = new WorldReference(w);
		Variable<Agent> a = new Variable<Agent>("a");
		AgentReference aref = new AgentReference(a);
		Variable<Label> p = new Variable<Label>("p");
		LabelReference pref = new LabelReference(p);

		// get the reference to the world we want to create
		LabelInstance label = new LabelInstance(pref, wref, aref);
		Substitution<World> wsub =
			label.match(rwt.getLabel()).getWorldSubstitution();
		WorldReference newWorldRef = (WorldReference)wsub.get(w);

		// create new substitution to replace the reference by our actual world
		LabelSubstitution ls = new LabelSubstitution();
		ls.put(newWorldRef.get(), new WorldInstance());
		NodeSubstitution ns = new NodeSubstitution();
		ns.merge(ls, new FullSubstitution());

		// perform the substitution
		rwt = rwt.substitute(ns);

		return rwt;
	}

	public String getError() {
		return d_error;
	}
}
