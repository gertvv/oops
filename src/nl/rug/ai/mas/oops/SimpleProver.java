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
 * A simple wrapper for a Prover with default rules. Takes a formula as a
 * command line argument and checks whether it is provable (or satisfiable).
 */
public class SimpleProver extends Prover {
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

		SimpleProver p = build();
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

	public static SimpleProver build() {
		Context c = new Context();

		// build rules
		Vector<Rule> rules = PropositionalRuleFactory.build(c);
		rules.addAll(ModalRuleFactory.build(c));

		FormulaValidator validator = new MultiModalValidator();

		return new SimpleProver(rules, validator, c);
	}

	/**
	 * The parser instance.
	 */
	private FormulaParser d_formulaAdapter;
	/**
	 * The (parser) context.
	 */
	private Context d_context;

	/**
	 * Constructor.
	 * Constructs a new prover object. May be used to parse, prove or sat-check
	 * any number of formulas.
	 */
	public SimpleProver(Vector<Rule> rules, FormulaValidator v, Context c) {
		super(rules, v);
		d_context = c;
		d_formulaAdapter = new FormulaParser(c);
	}

	/**
	 * @return true if formula is provable.
	 */
	public boolean provable(String formula)
	throws TableauErrorException {
		return provable(parse(formula));
	}

	/**
	 * @return true if formula is satisfiable.
	 */
	public boolean satisfiable(String formula)
	throws TableauErrorException {
		return satisfiable(parse(formula));
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
	 * @return the parser context.
	 */
	public Context getContext() {
		return d_context;
	}
}
