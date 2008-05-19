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

/**
 * A tableau branch, a collection of nodes. Implemented recursively, that is, a
 * Branch may itself have a 'parent' Branch and so on. This recursion is
 * abstracted from for most operations. For performance reasons, nodes are
 * grouped by label and the order in which nodes are added is not preserved.
 */
public class Branch {
	private Branch d_parent;
	private HashMap<Label, Vector<Node>> d_current;

	/**
	 * Create a new branch with parent. Use null to create a top-level Branch.
	 */
	public Branch(Branch parent) {
		d_parent = parent;
		d_current = new HashMap<Label, Vector<Node>>();
	}

	/**
	 * @return true if this Branch or it's parent contain n. false otherwise.
	 */
	public boolean contains(Node n) {
		Vector<Node> v = d_current.get(n.getLabel());
		if (v != null && v.contains(n))
			return true;
		if (d_parent != null)
			return d_parent.contains(n);
		return false;
	}

	/**
	 * Add Node n to the branch.
	 */
	public void add(Node n) {
		Vector<Node> v = d_current.get(n.getLabel());
		if (v == null) {
			v = new Vector<Node>();
			v.add(n);
			d_current.put(n.getLabel(), v);
		} else {
			v.add(n);
		}
	}

	/**
	 * Get a set of all the labels on this Branch and it's parent. Relies upon
	 * the appropriate implementation of the hashCode() method in Label.
	 */
	public Set<Label> getLabels() {
		HashSet<Label> l = new HashSet<Label>(d_current.keySet());
		if (d_parent != null)
			l.addAll(d_parent.getLabels());
		return l;
	}

	/**
	 * Get a set of all formulas for a label.
	 */
	public Set<Formula> getFormulas(Label l) {
		Set<Formula> f = new HashSet<Formula>();
		if (d_parent != null) {
			f = d_parent.getFormulas(l);
		}
		Collection<Node> nodes = d_current.get(l);
		if (nodes != null) {
			for (Node n : nodes) {
				f.add(n.getFormula());
			}
		}
		return f;
	}

	/**
	 * Return the parent branch.
	 */
	public Branch getParent() {
		return d_parent;
	}

	/**
	 * Apply a necessity to all labels on the Branch.
	 * @return A collection of concrete matches derived from m.
	 */
	public Vector<Match> apply(Match m) {
		Vector<Match> result = new Vector<Match>();
		for (Label l : getLabels()) {
			result.addAll(m.apply(l));
		}
		return result;
	}
}
