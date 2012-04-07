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
		T(TRules, TRelations),
		B(BRules, BRelations),
		D(DRules, DRelations),
		K4(K4Rules, K4Relations),
		K45(K45Rules, K45Relations),
		KD45(KD45Rules, KD45Relations),
		S4(S4Rules, S4Relations),
		S5(S5Rules, S5Relations),
		S5E(S5ERules, S5Relations);
		
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
	public static final Relation[] TRelations = { Relation.REFLEXIVE };
	public static final Relation[] DRelations = { /*Relation.SERIAL */ };
	public static final Relation[] BRelations = { Relation.SYMMETRIC };
	public static final Relation[] K4Relations = { Relation.TRANSITIVE };
	public static final Relation[] K45Relations = { Relation.TRANSITIVE, Relation.SYMMETRIC };
	public static final Relation[] KD45Relations = { /*Relation.SERIAL, */ Relation.TRANSITIVE, Relation.SYMMETRIC };
	public static final Relation[] S4Relations = { Relation.REFLEXIVE, Relation.TRANSITIVE };
	public static final Relation[] S5Relations = { Relation.REFLEXIVE, Relation.TRANSITIVE, Relation.SYMMETRIC };
	
	public static final RuleID[] KRules = { RuleID.PosO1, RuleID.PosO2, RuleID.BNecO1, RuleID.BNecO2 };
	public static final RuleID[] TRules = { RuleID.PosO1, RuleID.PosO2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecO1, RuleID.SNecO2 };
	public static final RuleID[] DRules = { RuleID.PosO1, RuleID.PosO2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecO3, RuleID.SNecO4 };
	public static final RuleID[] K4Rules = { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2 };
	public static final RuleID[] K45Rules = { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecS3, RuleID.SNecS4 };
	public static final RuleID[] KD45Rules = { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecO3, RuleID.SNecO4, RuleID.SNecS3, RuleID.SNecS4 };
	public static final RuleID[] BRules = { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecS1, RuleID.SNecS2 };
	public static final RuleID[] S4Rules = { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecO1, RuleID.SNecO2 };
	public static final RuleID[] S5Rules = { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.BNecS1, RuleID.BNecS2, RuleID.SNecO1, RuleID.SNecO2, RuleID.SNecS1, RuleID.SNecS2 };
	public static final RuleID[] S5ERules = { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.BNecS1, RuleID.BNecS2, RuleID.SNecO1, RuleID.SNecO2, RuleID.SNecS1, RuleID.SNecS2, RuleID.EK1, RuleID.EK2 };
	
	public static void main(String [] args) {
		String formula = null;
		boolean satisfyMode = false;
		
		AxiomSystem system = AxiomSystem.S5;

		int argNum = 0;
		
		// Parse command-line
		
		while (argNum < args.length) {
			String arg = args[argNum++];
			
			if (arg.equals("--sat")) {
				satisfyMode = true;
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
	
		DynamicProver p = system.build();
		try {
			if (satisfyMode) {
				System.out.println(p.provable(formula));
			} else {
				System.out.println(p.satisfiable(formula));
			}
		} catch (TableauErrorException e) {
			System.out.println(e);
			System.exit(1);
		}
	}
	
	
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
