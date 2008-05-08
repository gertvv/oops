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
import java.io.ByteArrayInputStream;
import nl.rug.ai.mas.oops.formula.*;
import nl.rug.ai.mas.oops.tableau.*;

/**
 * Proves formulas using a tableau.
 */
public class Prover {
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

		Context c = new Context();

		// build rules
		Vector<Rule> rules = PropositionalRuleFactory.build(c);
		rules.addAll(ModalRuleFactory.build(c));

		Prover p = new Prover(rules, c);
		try {
			if (mode == Mode.PROVE) {
				System.out.println(p.proveable(formula));
			} else {
				System.out.println(p.satisfiable(formula));
			}
		} catch (TableauErrorException e) {
			System.out.println(e);
		}
	}

	FormulaAdapter d_formulaAdapter;
	Tableau d_tableau;

	public Prover(Vector<Rule> rules, Context c) {
		d_formulaAdapter = new FormulaAdapter(c);
		d_tableau = new Tableau(rules);
	}

	public boolean proveable(String formula)
			throws TableauErrorException {
		if (!d_formulaAdapter.parse(
				new ByteArrayInputStream(formula.getBytes()))) {
			throw new TableauErrorException("Could not parse formula");
		}
		Tableau.BranchState result = d_tableau.tableau(
			new Negation(d_formulaAdapter.getFormula()));
		if (result == Tableau.BranchState.ERROR) {
			throw new TableauErrorException(d_tableau.getError());
		}
		return result == Tableau.BranchState.CLOSED;
	}

	public boolean satisfiable(String formula)
			throws TableauErrorException {
		if (!d_formulaAdapter.parse(
				new ByteArrayInputStream(formula.getBytes()))) {
			throw new TableauErrorException("Could not parse formula");
		}
		Tableau.BranchState result = d_tableau.tableau(
			d_formulaAdapter.getFormula());
		if (result == Tableau.BranchState.ERROR) {
			throw new TableauErrorException(d_tableau.getError());
		}
		return result == Tableau.BranchState.OPEN;
	}

	public Tableau getTableau() {
		return d_tableau;
	}
}
