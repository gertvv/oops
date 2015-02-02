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
import nl.rug.ai.mas.oops.model.ConfigurableModel;
import nl.rug.ai.mas.oops.model.ConfigurableModel.Relation;
import nl.rug.ai.mas.oops.model.KripkeModel;
import nl.rug.ai.mas.oops.parser.Context;
import nl.rug.ai.mas.oops.parser.FormulaParser;
import nl.rug.ai.mas.oops.tableau.*;
import nl.rug.ai.mas.oops.tableau.ModalRuleFactory.RuleID;

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

	/**
	 * The axiom system this prover was constructed from.
	 */
	private final AxiomSystem d_axiomSystem;
	/**
	 * The Tableau instance.
	 */
	private final Tableau d_tableau;
	/**
	 * The parser instance.
	 */
	private final FormulaParser d_formulaAdapter;
	/**
	 * The (parser) context.
	 */
	private final Context d_context;
	/**
	 * The model relations
	 */
	private final Vector<Relation> d_relations;

	/**
	 * Constructor.
	 * Constructs a new prover object. May be used to parse, prove or sat-check
	 * any number of formulas.
	 */
	public Prover(AxiomSystem as) {
		RuleID[] ruleIdsArray = as.rules;
		Vector<RuleID> rules = new Vector<RuleID>();
		Collections.addAll(rules, ruleIdsArray);
		
		Relation[] relationsArray = as.relations;
		Vector<Relation> relations = new Vector<Relation>();
		Collections.addAll(relations, relationsArray);
		
		Context c = new Context();
		
		Vector<Rule> rules1 = PropositionalRuleFactory.build(c);
		rules1.addAll(ModalRuleFactory.build(c, rules));
		
		d_formulaAdapter = new FormulaParser(c);
		d_tableau = new Tableau(rules1);
		d_context = c;
		d_axiomSystem = as;
		d_relations = relations;
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
	
	public AxiomSystem getAxiomSystem() {
		return d_axiomSystem;
	}

	/**
	 * Retrieve the used Tableau instance.
	 */
	public Tableau getTableau() {
		return d_tableau;
	}

	public boolean validate(Formula f) {
		return d_axiomSystem.validator.validate(f);
	}

	private void forceValid(Formula f)
	throws TableauErrorException {
		if (!validate(f)) {
			throw new TableauErrorException("Formula invalid in the system " + d_axiomSystem);
		} else if (!f.isConcrete()) {
			throw new TableauErrorException("Formula " + f + " is not concrete.");
		}
	}

	/**
	 * @return true if formula is provable.
	 */
	public boolean provable(String formula) throws TableauErrorException {
		return provable(parse(formula));
	}

	/**
	 * @return true if formula is satisfiable.
	 */
	public boolean satisfiable(String formula) throws TableauErrorException {
		return satisfiable(parse(formula));
	}

	/**
	 * Parse a string into a Formula object. Note that calling this method
	 * separately is useful, e.g. when using a Tableau Observer that expects a
	 * list of syntactic entities in advance.
	 * 
	 * @see nl.rug.ai.mas.oops.model.ModelConstructingObserver
	 */
	public Formula parse(String formula) throws TableauErrorException {
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

	public KripkeModel getModel() {
		return new ConfigurableModel(d_context.getAgentIdView(), d_relations);
	}

	public static void main(String [] args) {
		String formula = null;
		Mode mode = Mode.PROVE;
		
		AxiomSystem system = AxiomSystem.S5;

		int argNum = 0;
		
		// Parse command-line
		
		while (argNum < args.length) {
			String arg = args[argNum++];
			
			if (arg.equals("--sat")) {
				mode = Mode.SAT;
			} else if (arg.equals("--prover")) {
				try {
					system = AxiomSystem.valueOf(args[argNum++]);
				} catch (Exception e) {
					System.out.println("Invalid prover specified");
					return;
				}
			} else if (formula == null && !arg.startsWith("--") && !arg.startsWith("-") ) {
				formula = arg;
			}
		}
		
		if (formula == null) {
			System.out.println("Please supply a formula on the command line.");
			return;
		}
	
		Prover p = system.buildProver();
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
}
