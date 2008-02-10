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

public class UniBox implements UniModalF {
	Formula d_right;

	public UniBox(Formula f) {
		d_right = f;
	}

	public String toString() {
		return "#" + d_right;
	}

	public boolean equals(Object o) {
		if (o == null)
			return false;
		try {
			UniBox other = (UniBox)o;
			return d_right.equals(other.d_right);
		} catch (ClassCastException e) {
		}
		return false;
	}

	public int hashCode() {
		return 1;
	}

	public FullSubstitution match(Formula f) {
		try {
			UniBox m = (UniBox)f;
			return d_right.match(m.d_right);
		} catch (ClassCastException e) {
		}
		return null;
	}

	public Formula substitute(FullSubstitution s) {
		return new UniBox(d_right.substitute(s));
	}

	public void accept(FormulaVisitor v) {
		d_right.accept(v);
		v.visitUniBox(this);
	}

	public Formula opposite() {
		return new Negation(this);
	}

	public boolean isSimple() {
		return false;
	}

	public boolean isConcrete() {
		return d_right.isConcrete();
	}
}
