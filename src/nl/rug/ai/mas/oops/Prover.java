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
	public static void main(String [] args) {
		if (args.length != 1) {
			System.out.println("Please supply a formula on the command line.");
			return;
		}

		// build rules
		Vector<Rule> rules = PropositionalRuleFactory.build();
		rules.addAll(ModalRuleFactory.build());

		Prover p = new Prover(rules);
		try {
			System.out.println(p.proveable(args[0]));
		} catch (TableauErrorException e) {
			System.out.println(e);
		}
	}

	FormulaAdapter d_formulaAdapter;
	Tableau d_tableau;

	public Prover(Vector<Rule> rules) {
		d_formulaAdapter = new FormulaAdapter();
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
}
