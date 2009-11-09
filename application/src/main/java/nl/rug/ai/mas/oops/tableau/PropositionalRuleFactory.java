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
import nl.rug.ai.mas.oops.parser.Context;

public class PropositionalRuleFactory {
	public static String CONJ = "&and;";
	public static String DISJ = "&or;";
	public static String NEG = "&not;";
	public static String IMPL = "&rarr;";
	public static String BIIM = "&harr;";

	private PropositionalRuleFactory() {
	}

	public static Vector<Rule> build(Context context) {
		Vector<Rule> rules = new Vector<Rule>(7);
		rules.add(buildNeg(context));
		rules.add(buildCon1(context));
		rules.add(buildCon2(context));
		rules.add(buildCon3(context));
		rules.add(buildCon4(context));
		rules.add(buildDis1(context));
		rules.add(buildDis2(context));
		rules.add(buildDis3(context));
		rules.add(buildDis4(context));
		return rules;
	}

	public static Rule buildNeg(Context context) {
		String html = NEG;
		// variables occuring
		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r, 
			context.getFormulaCodeMap().code(r));

		// the formula
		Formula f = new Negation(new Negation(rref));

		// the rewrites
		Vector<Formula> rwt = new Vector<Formula>(1);
		rwt.add(rref);

		return new LinearRule("NEG", html, f, rwt);
	}

	public static Rule buildCon1(Context context) {
		String html = CONJ + "<sub>" + CONJ + "</sub>";
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l, 
			context.getFormulaCodeMap().code(l));

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r, 
			context.getFormulaCodeMap().code(r));

		// formula
		Formula f = new Conjunction(lref, rref);

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(lref);
		rwt.add(rref);

		return new LinearRule("CON1", html, f, rwt);
	}

	public static Rule buildCon2(Context context) {
		String html = CONJ + "<sub>" + DISJ + "</sub>";
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l, 
			context.getFormulaCodeMap().code(l));

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r, 
			context.getFormulaCodeMap().code(r));

		// formula
		Formula f = new Negation(new Disjunction(lref, rref));

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Negation(lref));
		rwt.add(new Negation(rref));

		return new LinearRule("CON2", html, f, rwt);
	}

	public static Rule buildCon3(Context context) {
		String html = CONJ + "<sub>" + IMPL + "</sub>";
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l, 
			context.getFormulaCodeMap().code(l));

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r, 
			context.getFormulaCodeMap().code(r));

		// formula
		Formula f = new Negation(new Implication(lref, rref));

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(lref);
		rwt.add(new Negation(rref));

		return new LinearRule("CON3", html, f, rwt);
	}

	public static Rule buildCon4(Context context) {
		String html = CONJ + "<sub>" + BIIM + "</sub>";
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l, 
			context.getFormulaCodeMap().code(l));

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r, 
			context.getFormulaCodeMap().code(r));

		// formula
		Formula f = new Negation(new BiImplication(lref, rref));

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Negation(new Conjunction(
						new Negation(lref), new Negation(rref))));
		rwt.add(new Negation(new Conjunction(
						lref, rref)));

		return new LinearRule("CON4", html, f, rwt);
	}

	public static Rule buildDis1(Context context) {
		String html = DISJ + "<sub>" + DISJ + "</sub>";
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l, 
			context.getFormulaCodeMap().code(l));

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r, 
			context.getFormulaCodeMap().code(r));

		// formula
		Formula f = new Disjunction(lref, rref);

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(lref);
		rwt.add(rref);

		return new SplitRule("DIS1", html, f, rwt);
	}

	public static Rule buildDis2(Context context) {
		String html = DISJ + "<sub>" + CONJ + "</sub>";
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l, 
			context.getFormulaCodeMap().code(l));

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r, 
			context.getFormulaCodeMap().code(r));

		// formula
		Formula f = new Negation(new Conjunction(lref, rref));

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Negation(lref));
		rwt.add(new Negation(rref));

		return new SplitRule("DIS2", html, f, rwt);
	}

	public static Rule buildDis3(Context context) {
		String html = DISJ + "<sub>" + IMPL + "</sub>";
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l, 
			context.getFormulaCodeMap().code(l));

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r, 
			context.getFormulaCodeMap().code(r));

		// formula
		Formula f = new Implication(lref, rref);

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Negation(lref));
		rwt.add(rref);

		return new SplitRule("DIS3", html, f, rwt);
	}

	public static Rule buildDis4(Context context) {
		String html = DISJ + "<sub>" + BIIM + "</sub>";
		// variables
		Variable<Formula> l = new Variable<Formula>("L");
		FormulaReference lref = new FormulaReference(l, 
			context.getFormulaCodeMap().code(l));

		Variable<Formula> r = new Variable<Formula>("R");
		FormulaReference rref = new FormulaReference(r, 
			context.getFormulaCodeMap().code(r));

		// formula
		Formula f = new BiImplication(lref, rref);

		// rewrites
		Vector<Formula> rwt = new Vector<Formula>(2);
		rwt.add(new Conjunction(new Negation(lref), new Negation(rref)));
		rwt.add(new Conjunction(lref, rref));

		return new SplitRule("DIS4", html, f, rwt);
	}
}
