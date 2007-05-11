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

public class Branch {
	private Branch d_parent;
	private HashMap<Label, Vector<Node>> d_current;

	public Branch(Branch parent) {
		d_parent = parent;
		d_current = new HashMap<Label, Vector<Node>>();
	}

	public boolean contains(Node n) {
		Vector<Node> v = d_current.get(n.getLabel());
		if (v != null && v.contains(n))
			return true;
		if (d_parent != null)
			return d_parent.contains(n);
		return false;
	}

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

	public Set<Label> getLabels() {
		HashSet<Label> l = new HashSet(d_current.keySet());
		if (d_parent != null)
			l.addAll(d_parent.getLabels());
		return l;
	}

	/**
	 * Apply a necessity to all labels on the Branch.
	 * @return The resulting nodes.
	 */
	public Vector<Node> apply(Node n) {
		Vector<Node> result = new Vector<Node>();
		for (Label l : getLabels()) {
			LabelSubstitution lsub = n.getLabel().match(l);
			if (lsub != null) {
				NodeSubstitution nsub = new NodeSubstitution();
				nsub.merge(lsub, new FullSubstitution());
				result.add(n.substitute(nsub));
			}
		}
		return result;
	}

	public String toString() {
		String s = new String();
		if (d_parent == null)
			s = "***\n";
		else
			s = d_parent.toString() + "---\n";
		for (Map.Entry<Label, Vector<Node>> w : d_current.entrySet()) {
			s += w.getKey() + "\n";
			for (Node n : w.getValue()) {
				s += "\t" + n.toString() + "\n";
			}
		}
		return s;
	}
}
