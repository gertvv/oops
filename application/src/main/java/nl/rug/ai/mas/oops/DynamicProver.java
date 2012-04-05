package nl.rug.ai.mas.oops;

import java.util.Collections;
import java.util.Vector;

import nl.rug.ai.mas.oops.formula.Formula;
import nl.rug.ai.mas.oops.model.KripkeModel;
import nl.rug.ai.mas.oops.model.Model;
import nl.rug.ai.mas.oops.model.Model.Relation;
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
		K(KRules, KRelations),
		S4(S4Rules, S4Relations),
		S5(S5Rules, S5Relations);
		
		private final RuleID[] rules;
		private final Relation[] relations;
		private AxiomSystem(RuleID[] rules, Relation[] relations) {
			this.rules = rules;
			this.relations = relations;
		}
		
		public DynamicProver build()
		{
			return DynamicProver.build(rules, relations);
		}
	}
	
	public static final Relation[] KRelations = {};
	public static final Relation[] S4Relations = { Relation.REFLEXIVE, Relation.TRANSITIVE };
	public static final Relation[] S5Relations = { Relation.REFLEXIVE, Relation.TRANSITIVE, Relation.SYMMETRIC };
	
	public static final RuleID[] KRules = { RuleID.KPos1, RuleID.KPos2, RuleID.KBNec1, RuleID.KBNec2 };
	public static final RuleID[] S4Rules = { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecO1, RuleID.SNecO2, RuleID.SNecS1, RuleID.SNecS2 };
	public static final RuleID[] S5Rules = { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.BNecS1, RuleID.BNecS2, RuleID.SNecO1, RuleID.SNecO2, RuleID.SNecS1, RuleID.SNecS2 };
	
	public static DynamicProver build(RuleID[] ruleIdsArray, Relation[] relationsArray) {
		Vector<RuleID> rules = new Vector<RuleID>();
		Collections.addAll(rules, ruleIdsArray);
		
		Vector<Relation> relations = new Vector<Relation>();
		Collections.addAll(relations, relationsArray);
		
		return build(rules, relations);
	}

	public static DynamicProver build(Vector<RuleID> ruleIds, Vector<Relation> relations) {
		Context c = new Context();

		Vector<Rule> rules = PropositionalRuleFactory.build(c);
		rules.addAll(ModalRuleFactory.build(c, ruleIds));

		FormulaValidator validator = new MultiModalValidator();

		return new DynamicProver(rules, relations, validator, c);
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
	 * The model relations
	 */
	private Vector<Relation> d_relations;

	/**
	 * Constructor. Constructs a new prover object. May be used to parse, prove
	 * or sat-check any number of formulas.
	 */
	public DynamicProver(Vector<Rule> rules, Vector<Relation> relations, FormulaValidator v, Context c) {
		super(rules, v);
		d_relations = relations;
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

	@Override
	public KripkeModel getModel() {
		return new Model(d_context.getAgentIdView(), d_relations);
	}

}
