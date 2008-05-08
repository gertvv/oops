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

package nl.rug.ai.mas.oops.rndcnf;

import nl.rug.ai.mas.oops.formula.*;
import java.util.Comparator;

public class FormulaComparator implements Comparator<Formula> {
	public int compare(Formula o1, Formula o2) {
		// a FormulaReference is always smallest
		if (o1 instanceof FormulaReference) {
			if (o2 instanceof FormulaReference) {
				return o1.code() - o2.code();
			}
			return -1;
		}
		if (o2 instanceof FormulaReference) {
			return 1;
		}

		// a Proposition is second-smallest
		if (o1 instanceof Proposition) {
			if (o2 instanceof Proposition) {
				return o1.code() - o2.code();
			}
			return -1;
		}
		if (o2 instanceof Proposition) {
			return 1;
		}

		// the rest in order of operator code
		int diff = o1.code() - o2.code();
		if (diff != 0) {
			return diff;
		}

		// if operator code is equal, by operands
		if (o1 instanceof BiImplication) {
			BiImplication c1 = (BiImplication)o1;
			BiImplication c2 = (BiImplication)o2;
			return compare(
				c1.getLeft(), c1.getRight(), c2.getLeft(), c2.getRight());
		}

		if (o1 instanceof Conjunction) {
			Conjunction c1 = (Conjunction)o1;
			Conjunction c2 = (Conjunction)o2;
			return compare(
				c1.getLeft(), c1.getRight(), c2.getLeft(), c2.getRight());
		}

		if (o1 instanceof Disjunction) {
			Disjunction c1 = (Disjunction)o1;
			Disjunction c2 = (Disjunction)o2;
			return compare(
				c1.getLeft(), c1.getRight(), c2.getLeft(), c2.getRight());
		}

		if (o1 instanceof Implication) {
			Implication c1 = (Implication)o1;
			Implication c2 = (Implication)o2;
			return compare(
				c1.getLeft(), c1.getRight(), c2.getLeft(), c2.getRight());
		}

		if (o1 instanceof MultiBox) {
			MultiBox c1 = (MultiBox)o1;
			MultiBox c2 = (MultiBox)o2;
			return compare(c1.getRight(), c2.getRight());
		}

		if (o1 instanceof MultiDiamond) {
			MultiDiamond c1 = (MultiDiamond)o1;
			MultiDiamond c2 = (MultiDiamond)o2;
			return compare(c1.getRight(), c2.getRight());
		}

		if (o1 instanceof Negation) {
			Negation c1 = (Negation)o1;
			Negation c2 = (Negation)o2;
			return compare(c1.getRight(), c2.getRight());
		}

		if (o1 instanceof UniBox) {
			UniBox c1 = (UniBox)o1;
			UniBox c2 = (UniBox)o2;
			return compare(c1.getRight(), c2.getRight());
		}

		if (o1 instanceof UniDiamond) {
			UniDiamond c1 = (UniDiamond)o1;
			UniDiamond c2 = (UniDiamond)o2;
			return compare(c1.getRight(), c2.getRight());
		}

		throw new IllegalArgumentException("FormulaComparator can't compare " +
			o1.getClass() + " to " + o2.getClass());
	}

	private int compare(Formula l1, Formula r1, Formula l2, Formula r2) {
		int c = compare(l1, l2);
		if (c != 0) {
			return c;
		}
		return compare(l2, r2);
	}

	public boolean equals(Object obj) {
		if (obj instanceof FormulaComparator) {
			return true;
		}
		return false;
	}
}
