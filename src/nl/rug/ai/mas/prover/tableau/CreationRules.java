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

public class CreationRules {
	private CreationRules d_parent;
	private Vector<Node> d_current;

	public CreationRules(CreationRules parent) {
		d_parent = parent;
		d_current = new Vector<Node>();
	}

	public void add(Node n) {
		d_current.add(n);
	}

	public boolean entails(Node concrete) {
		System.out.println("Entails test: " + concrete);
		if (d_parent != null && d_parent.entails(concrete)) {
			System.out.println("parent entails");
			return true;
		}

		for (Node n : d_current) {
			System.out.println(n.getFormula() + "  " + n.getFormula().equals(concrete.getFormula()));
			System.out.println(n.getLabel() + " " + n.getLabel().match(concrete.getLabel()));
			if (n.getFormula().equals(concrete.getFormula()) &&
					n.getLabel().match(concrete.getLabel()) != null)
				return true;
		}
		return false;
	}

	public String toString() {
		String s = new String();
		if (d_parent == null)
			s = "###\n";
		else
			s = d_parent.toString() + "---\n";
		for (Node n : d_current) {
			s += "\t" + n.toString() + "\n";
		}
		return s;
	}
}
