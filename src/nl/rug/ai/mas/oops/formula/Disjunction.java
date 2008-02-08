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

package nl.rug.ai.mas.oops.formula;

public class Disjunction implements PropositionalF {
	private Formula d_left;
	private Formula d_right;

	public Disjunction(Formula l, Formula r) {
		d_left = l;
		d_right = r;
	}

	public Formula getLeft() {
		return d_left;
	}

	public Formula getRight() {
		return d_right;
	}

	public String toString() {
		return "(" + d_left + " | " + d_right + ")";
	}

	public boolean equals(Object o) {
		if (o != null) {
			try {
				Disjunction other = (Disjunction) o;
				if (other.d_left.equals(d_left) &&
						other.d_right.equals(d_right)) {
					return true;
				}
			} catch (ClassCastException e) {
			}
		}
		return false;
	}

	public int hashCode() {
		return 1;
	}

	public FullSubstitution match(Formula f) {
		try {
			Disjunction x = (Disjunction)f;
			FullSubstitution l = d_left.match(x.d_left);
			FullSubstitution r = d_right.match(x.d_right);
			if (l == null || r == null || l.merge(r) == false)
				return null;
			return l;
		} catch (ClassCastException e) {
		}
		return null;
	}

	public Formula substitute(FullSubstitution s) {
		return new Disjunction(d_left.substitute(s), d_right.substitute(s));
	}

	public Formula opposite() {
		return new Negation(this);
	}

	public boolean isSimple() {
		return false;
	}

	public boolean isConcrete() {
		return d_left.isConcrete() && d_right.isConcrete();
	}
}
