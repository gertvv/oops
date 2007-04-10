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

public class PropositionalRuleFactory {
	private PropositionalRuleFactory() {
	}

	public static Vector<Rule> build() {
		Vector<Rule> rules = new Vector<Rule>(7);
		rules.add(buildNeg());
		rules.add(buildCon1());
		rules.add(buildCon2());
		rules.add(buildCon3());
		rules.add(buildCon4());
		rules.add(buildDis1());
		rules.add(buildDis2());
		rules.add(buildDis3());
		rules.add(buildDis4());
		return rules;
	}

	public static Rule buildNeg() {
		// variables occuring
		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r);
		r.add(rref);

		// the formula
		Formula f = new Negation(new Negation(rref));

		// the rewrites
		Vector<Formula> rwt = new Vector<Formula>(1);
		rwt.add(rref);

		return new Rule("NEG", f, Rule.Type.LINEAR, rwt);
	}

	public static Rule buildCon1() {
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l);
		l.add(lref);

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r);
		r.add(rref);

		// formula
		Formula f = new Conjunction(lref, rref);

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(lref);
		rwt.add(rref);

		return new Rule("CON1", f, Rule.Type.LINEAR, rwt);
	}

	public static Rule buildCon2() {
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l);
		l.add(lref);

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r);
		r.add(rref);

		// formula
		Formula f = new Negation(new Disjunction(lref, rref));

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Negation(lref));
		rwt.add(new Negation(rref));

		return new Rule("CON2", f, Rule.Type.LINEAR, rwt);
	}

	public static Rule buildCon3() {
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l);
		l.add(lref);

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r);
		r.add(rref);

		// formula
		Formula f = new Negation(new Implication(lref, rref));

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(lref);
		rwt.add(new Negation(rref));

		return new Rule("CON3", f, Rule.Type.LINEAR, rwt);
	}

	public static Rule buildCon4() {
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l);
		l.add(lref);

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r);
		r.add(rref);

		// formula
		Formula f = new Negation(new BiImplication(lref, rref));

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Negation(new Conjunction(
						new Negation(lref), new Negation(rref))));
		rwt.add(new Negation(new Conjunction(
						lref, rref)));

		return new Rule("CON4", f, Rule.Type.LINEAR, rwt);
	}

	public static Rule buildDis1() {
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l);
		l.add(lref);

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r);
		r.add(rref);

		// formula
		Formula f = new Disjunction(lref, rref);

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(lref);
		rwt.add(rref);

		return new Rule("DIS1", f, Rule.Type.SPLIT, rwt);
	}

	public static Rule buildDis2() {
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l);
		l.add(lref);

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r);
		r.add(rref);

		// formula
		Formula f = new Negation(new Conjunction(lref, rref));

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Negation(lref));
		rwt.add(new Negation(rref));

		return new Rule("DIS2", f, Rule.Type.SPLIT, rwt);
	}

	public static Rule buildDis3() {
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l);
		l.add(lref);

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r);
		r.add(rref);

		// formula
		Formula f = new Implication(lref, rref);

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Negation(lref));
		rwt.add(rref);

		return new Rule("DIS3", f, Rule.Type.SPLIT, rwt);
	}

	public static Rule buildDis4() {
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l);
		l.add(lref);

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r);
		r.add(rref);

		// formula
		Formula f = new BiImplication(lref, rref);

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Conjunction(new Negation(lref), new Negation(rref)));
		rwt.add(new Conjunction(lref, rref));

		return new Rule("DIS4", f, Rule.Type.SPLIT, rwt);
	}
}
