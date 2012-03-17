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

/**
 * Holds a reference to a Variable object, used for unification of variables.
 */
public class FormulaReference extends VariableReference<Formula>
implements Formula {
	private int d_code;

	public FormulaReference(Variable<Formula> var, int code) {
		super(var);
		d_code = code;
	}

	public FullSubstitution match(Formula f) {
		FullSubstitution s = new FullSubstitution();
		s.put(get(), f);
		return s;
	}

	public Formula substitute(FullSubstitution s) {
		Formula f = s.getFormula(get());
		if (f != null)
			return f;
		return this;
	}

	public void accept(FormulaVisitor v) {
		v.visitFormulaReference(this);
	}

	public Formula opposite() {
		return new Negation(this);
	}

	public boolean isSimple() {
		return false;
	}

	public boolean isConcrete() {
		return false;
	}

	public int code() {
		return d_code;
	}

	public int hashCode() {
		return d_code;
	}
}
