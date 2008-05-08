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

public class Negation implements PropositionalF {
	public static final int s_code = 2;

	private Formula d_right;
	private int d_hash;

	public Negation(Formula r) {
		d_right = r;
		d_hash = CodeUtil.codeUnary(s_code, r.hashCode());
	}

	public Formula getRight() {
		return d_right;
	}

	public String toString() {
		return "~" + d_right;
	}

	public boolean equals(Object o) {
		if (o != null) {
			try {
				Negation other = (Negation) o;
				if (other.d_hash != d_hash) {
					return false;
				}
				if (other.d_right.equals(d_right)) {
					return true;
				}
			} catch (ClassCastException e) {
			}
		}
		return false;
	}

	public FullSubstitution match(Formula f) {
		try {
			Negation n = (Negation)f;
			return d_right.match(n.d_right);
		} catch (ClassCastException e) {
		}
		return null;
	}

	public Formula substitute(FullSubstitution s) {
		return new Negation(d_right.substitute(s));
	}

	public void accept(FormulaVisitor v) {
		d_right.accept(v);
		v.visitNegation(this);
	}

	public Formula opposite() {
		return d_right;
	}

	public boolean isSimple() {
		if (d_right.match(
					new Negation(new FormulaReference(new Variable<Formula>("F"), 1))
					) == null && d_right.isSimple())
			return true;
		return false;
	}

	public boolean isConcrete() {
		return d_right.isConcrete();
	}

	public int code() {
		return s_code;
	}

	public int hashCode() {
		return d_hash;
	}
}
