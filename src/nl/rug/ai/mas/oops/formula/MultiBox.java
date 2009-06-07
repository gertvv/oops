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

public class MultiBox implements MultiModalF {
	public static final int s_code = 7;

	private Formula d_right;
	private Agent d_agent;
	private int d_code;
	private int d_hash;

	public MultiBox(Agent a, Formula f) {
		d_agent = a;
		d_right = f;
		d_code = CodeUtil.codeModalOperator(s_code, a.code());
		d_hash = CodeUtil.codeUnary(d_code, f.hashCode());
	}

	public Agent getAgent() {
		return d_agent;
	}

	public Formula getRight() {
		return d_right;
	}

	public String toString() {
		return "#_" + d_agent.toString() + " " + d_right;
	}

	public boolean equals(Object o) {
		if (o != null) {
			try {
				MultiBox other = (MultiBox) o;
				if (other.d_hash != d_hash) {
					return false;
				}
				if (other.d_code == d_code &&
						other.d_right.equals(d_right)) {
					return true;
				}
			} catch (ClassCastException e) {
			}
		}
		return false;
	}

	public FullSubstitution match(Formula f) {
		try {
			MultiBox m = (MultiBox)f;
			Substitution<Agent> a = d_agent.match(m.d_agent);
			FullSubstitution r = d_right.match(m.d_right);
			if (a == null || r == null)
				return null;
			FullSubstitution l = new FullSubstitution(a);
			if (l.merge(r) == false)
				return null;
			return l;
		} catch (ClassCastException e) {
		}
		return null;
	}

	public Formula substitute(FullSubstitution s) {
		return new MultiBox(
				d_agent.substitute(s.getAgentSubstitution()),
				d_right.substitute(s));
	}

	public void accept(FormulaVisitor v) {
		d_right.accept(v);
		v.visitMultiBox(this);
	}

	public Formula opposite() {
		return new Negation(this);
	}

	public boolean isSimple() {
		return false;
	}

	public boolean isConcrete() {
		return d_agent.isConcrete() && d_right.isConcrete();
	}

	public int code() {
		return d_code;
	}

	public int hashCode() {
		return d_hash;
	}
}
