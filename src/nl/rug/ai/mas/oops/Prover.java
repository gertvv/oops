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

package nl.rug.ai.mas.oops;

import java.util.*;
import nl.rug.ai.mas.oops.formula.*;
import nl.rug.ai.mas.oops.tableau.*;
import nl.rug.ai.mas.oops.theory.Theory;

/**
 * Proves formulas using a tableau.
 */
public class Prover {
	/**
	 * Tableau mode (whether to PROVE a formula, or to do SAT checking).
	 */
	private enum Mode {
		PROVE,
		SAT
	};

	public static void main(String [] args) {
		String formula = null;
		Mode mode = null;

		if (args.length == 1) {
			mode = Mode.PROVE;
			formula = args[0];
		} else if (args.length == 2 && args[0].equals("--sat")) {
			mode = Mode.SAT;
			formula = args[1];
		} else {
			System.out.println("Please supply a formula on the command line.");
			return;
		}

		Prover p = build();
		try {
			if (mode == Mode.PROVE) {
				System.out.println(p.provable(formula));
			} else {
				System.out.println(p.satisfiable(formula));
			}
		} catch (TableauErrorException e) {
			System.out.println(e);
			System.exit(1);
		}
	}

	public static Prover build() {
		Context c = new Context();

		// build rules
		Vector<Rule> rules = PropositionalRuleFactory.build(c);
		rules.addAll(ModalRuleFactory.build(c));

		return new Prover(rules, c);
	}

	/**
	 * The parser instance.
	 */
	FormulaParser d_formulaAdapter;
	/**
	 * The Tableau instance.
	 */
	Tableau d_tableau;

	/**
	 * Constructor.
	 * Constructs a new prover object. May be used to parse, prove or sat-check
	 * any number of formulas.
	 */
	public Prover(Vector<Rule> rules, Context c) {
		d_formulaAdapter = new FormulaParser(c);
		d_tableau = new Tableau(rules);
	}

	/**
	 * @return true if formula is provable.
	 */
	public boolean provable(String formula)
	throws TableauErrorException {
		return provable(parse(formula));
	}

	/**
	 * Provability checker (uses ~formula not satisfiable, then formula
	 * provable).
	 * @return true if formula is provable.
	 */
	public boolean provable(Formula formula)
	throws TableauErrorException {
		Tableau.BranchState result = d_tableau.tableau(
			new Negation(formula));
		if (result == Tableau.BranchState.ERROR) {
			throw new TableauErrorException(d_tableau.getError());
		}
		return result == Tableau.BranchState.CLOSED;
	}
	
	/**
	 * Attempts to prove (theory -> formula)
	 * @param theory The Theory from which to prove formula.
	 * @param formula The Formula to prove.
	 * @return true if (theory -> formula) is provable.
	 * @throws TableauErrorException
	 */
	public boolean provable(Theory theory, Formula formula)
	throws TableauErrorException {
		if (theory == null || theory.asFormula() == null) {
			return provable(formula);
		}
		return provable(new Implication(theory.asFormula(), formula));
	}
	
	/**
	 * Checks the satisfiability of (theory & formula)
	 * @param theory The Theory in which to check SAT.
	 * @param formula The Formula to check SAT of.
	 * @return true if formula is satisfiable in the theory.
	 * @throws TableauErrorException
	 */
	public boolean satisfiable(Theory theory, Formula formula)
	throws TableauErrorException {
		if (theory == null || theory.asFormula() == null) {
			return satisfiable(formula);
		}
		return satisfiable(new Conjunction(theory.asFormula(), formula));
	}
	
	/**
	 * Checks consistency of theory (i.e. theory is satisfiable)
	 * @param theory The Theory to check.
	 * @return true if the theory is consistent.
	 * @throws TableauErrorException
	 */
	public boolean consistent(Theory theory)
	throws TableauErrorException {
		if (theory == null || theory.asFormula() == null) {
			return true;
		}
		return satisfiable(theory.asFormula());
	}
	
	/**
	 * @return true if formula is satisfiable.
	 */
	public boolean satisfiable(String formula)
	throws TableauErrorException {
		return satisfiable(parse(formula));
	}
	
	/**
	 * @return true if formula is satisfiable.
	 */
	public boolean satisfiable(Formula formula)
			throws TableauErrorException {
		Tableau.BranchState result = d_tableau.tableau(formula);
		if (result == Tableau.BranchState.ERROR) {
			throw new TableauErrorException(d_tableau.getError());
		}
		return result == Tableau.BranchState.OPEN;
	}

	/**
	 * Parse a string into a Formula object.
	 * Note that calling this method separately is useful, e.g. when using a
	 * Tableau Observer that expects a list of syntactic entities in advance.
	 * @see nl.rug.ai.mas.oops.model.ModelConstructingObserver
	 */
	public Formula parse(String formula)
			throws TableauErrorException {
		if (!d_formulaAdapter.parse(formula)) {
			throw new TableauErrorException("Could not parse formula");
		}
		return d_formulaAdapter.getFormula();
	}

	/**
	 * Retreive the used Tableau instance.
	 */
	public Tableau getTableau() {
		return d_tableau;
	}
}
