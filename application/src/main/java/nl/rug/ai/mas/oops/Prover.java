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
import nl.rug.ai.mas.oops.model.KripkeModel;
import nl.rug.ai.mas.oops.parser.Context;
import nl.rug.ai.mas.oops.tableau.*;

/**
 * Proves formulas using a tableau.
 */
public abstract class Prover {
	/**
	 * The Tableau instance.
	 */
	Tableau d_tableau;
	FormulaValidator d_validator;

	/**
	 * Constructor.
	 * Constructs a new prover object. May be used to parse, prove or sat-check
	 * any number of formulas.
	 */
	public Prover(Vector<Rule> rules, FormulaValidator validator) {
		d_tableau = new Tableau(rules);
		d_validator = validator;
	}

	/**
	 * Provability checker (uses ~formula not satisfiable, then formula
	 * provable).
	 * @return true if formula is provable.
	 */
	public boolean provable(Formula formula)
	throws TableauErrorException {
		forceValid(formula);
		Tableau.BranchState result = d_tableau.tableau(
			new Negation(formula));
		if (result == Tableau.BranchState.ERROR) {
			throw new TableauErrorException(d_tableau.getError());
		}
		return result == Tableau.BranchState.CLOSED;
	}
	
	/**
	 * @return true if formula is satisfiable.
	 */
	public boolean satisfiable(Formula formula)
			throws TableauErrorException {
		forceValid(formula);
		Tableau.BranchState result = d_tableau.tableau(formula);
		if (result == Tableau.BranchState.ERROR) {
			throw new TableauErrorException(d_tableau.getError());
		}
		return result == Tableau.BranchState.OPEN;
	}
	
	public abstract KripkeModel getModel();
	public abstract Context getContext();

	/**
	 * Retreive the used Tableau instance.
	 */
	public Tableau getTableau() {
		return d_tableau;
	}

	public boolean validate(Formula f) {
		return d_validator.validate(f);
	}

	private void forceValid(Formula f)
	throws TableauErrorException {
		if (!validate(f)) {
			throw new TableauErrorException("Formula invalid: " +
				d_validator.toString() + " failed");
		} else if (!f.isConcrete()) {
			throw new TableauErrorException("Formula " + f + " is not concrete.");
		}
	}
}
