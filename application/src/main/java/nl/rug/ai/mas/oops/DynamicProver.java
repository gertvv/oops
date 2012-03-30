package nl.rug.ai.mas.oops;

import java.util.Collections;
import java.util.Vector;

import nl.rug.ai.mas.oops.formula.Formula;
import nl.rug.ai.mas.oops.parser.Context;
import nl.rug.ai.mas.oops.parser.FormulaParser;
import nl.rug.ai.mas.oops.tableau.FormulaValidator;
import nl.rug.ai.mas.oops.tableau.ModalRuleFactory;
import nl.rug.ai.mas.oops.tableau.ModalRuleFactory.RuleID;
import nl.rug.ai.mas.oops.tableau.MultiModalValidator;
import nl.rug.ai.mas.oops.tableau.PropositionalRuleFactory;
import nl.rug.ai.mas.oops.tableau.Rule;

/**
 * 
 * @author Lourens
 * 
 */
public class DynamicProver extends Prover {
	public enum AxiomSystem {
		K(KRules),
		S4(S4Rules),
		S5(S5Rules);
		
		private final RuleID[] rules;
		private AxiomSystem(RuleID[] rules) {
			this.rules = rules;
		}
		
		public DynamicProver build()
		{
			return DynamicProver.build(rules);
		}
	}
	
	public static final RuleID[] KRules = { RuleID.KPos1, RuleID.KPos2, RuleID.KBNec1, RuleID.KBNec2 };
	public static final RuleID[] S4Rules = { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecO1, RuleID.SNecO2, RuleID.SNecS1, RuleID.SNecS2 };
	public static final RuleID[] S5Rules = { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.BNecS1, RuleID.BNecS2, RuleID.SNecO1, RuleID.SNecO2, RuleID.SNecS1, RuleID.SNecS2 };

	public static DynamicProver build(RuleID[] ruleIds) {
		Vector<RuleID> rules = new Vector<RuleID>();
		Collections.addAll(rules, ruleIds);
		return build(rules);
	}

	public static DynamicProver build(Vector<RuleID> ruleIds) {
		Context c = new Context();

		Vector<Rule> rules = PropositionalRuleFactory.build(c);
		rules.addAll(ModalRuleFactory.build(c, ruleIds));

		FormulaValidator validator = new MultiModalValidator();

		return new DynamicProver(rules, validator, c);
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
	 * Constructor. Constructs a new prover object. May be used to parse, prove
	 * or sat-check any number of formulas.
	 */
	public DynamicProver(Vector<Rule> rules, FormulaValidator v, Context c) {
		super(rules, v);
		d_context = c;
		d_formulaAdapter = new FormulaParser(c);
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

}
