package nl.rug.ai.mas.oops;

import nl.rug.ai.mas.oops.model.ConfigurableModel.Relation;
import nl.rug.ai.mas.oops.tableau.EMultiModalValidator;
import nl.rug.ai.mas.oops.tableau.FormulaValidator;
import nl.rug.ai.mas.oops.tableau.MultiModalValidator;
import nl.rug.ai.mas.oops.tableau.PropositionalValidator;
import nl.rug.ai.mas.oops.tableau.ModalRuleFactory.RuleID;

public enum AxiomSystem {
	Propositional(
			new RuleID[] {},
			new Relation[] {},
			new PropositionalValidator()
	),
	K(
			new RuleID[] { RuleID.PosO1, RuleID.PosO2, RuleID.BNecO1, RuleID.BNecO2 },
			new Relation[] {},
			new MultiModalValidator()
	),
	KE
	(
			new RuleID[] { RuleID.PosO1, RuleID.PosO2, RuleID.BNecO1, RuleID.BNecO2, RuleID.EK1, RuleID.EK2, RuleID.EK3, RuleID.EK4 },
			new Relation[] {},
			new EMultiModalValidator()
	),
	T(
			new RuleID[] { RuleID.PosO1, RuleID.PosO2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecO1, RuleID.SNecO2 },
			new Relation[] { Relation.REFLEXIVE },
			new MultiModalValidator()
	),
	B(
			new RuleID[] { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecS1, RuleID.SNecS2 },
			new Relation[] { Relation.SYMMETRIC },
			new MultiModalValidator()
	),
	D(
			new RuleID[] { RuleID.PosO1, RuleID.PosO2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecO3, RuleID.SNecO4 },
			new Relation[] { Relation.SERIAL },
			new MultiModalValidator()
	),
	K4(
			new RuleID[] { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2 },
			new Relation[] { Relation.TRANSITIVE },
			new MultiModalValidator()
	),
	K45(
			new RuleID[] { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecS3, RuleID.SNecS4 },
			new Relation[] { Relation.TRANSITIVE, Relation.SYMMETRIC },
			new MultiModalValidator()
	),
	KD45(
			new RuleID[] { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecO3, RuleID.SNecO4, RuleID.SNecS3, RuleID.SNecS4 },
			new Relation[] { Relation.SERIAL, Relation.TRANSITIVE, Relation.SYMMETRIC },
			new MultiModalValidator()
	),
	S4(
			new RuleID[] { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecO1, RuleID.SNecO2 },
			new Relation[] { Relation.REFLEXIVE, Relation.TRANSITIVE },
			new MultiModalValidator()
	),
	S4E(
			new RuleID[] { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.SNecO1, RuleID.SNecO2, RuleID.EK1, RuleID.EK2, RuleID.EK3, RuleID.EK4 },
			new Relation[] { Relation.REFLEXIVE, Relation.TRANSITIVE },
			new EMultiModalValidator()
	),
	S5(
			new RuleID[] { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.BNecS1, RuleID.BNecS2, RuleID.SNecO1, RuleID.SNecO2, RuleID.SNecS1, RuleID.SNecS2 },
			new Relation[] { Relation.REFLEXIVE, Relation.TRANSITIVE, Relation.SYMMETRIC },
			new MultiModalValidator()
	),
	S5E(
			new RuleID[] { RuleID.PosO1, RuleID.PosO2, RuleID.PosS1, RuleID.PosS2, RuleID.BNecO1, RuleID.BNecO2, RuleID.BNecS1, RuleID.BNecS2, RuleID.SNecO1, RuleID.SNecO2, RuleID.SNecS1, RuleID.SNecS2, RuleID.EK1, RuleID.EK2, RuleID.EK3, RuleID.EK4 },
			new Relation[] { Relation.REFLEXIVE, Relation.TRANSITIVE, Relation.SYMMETRIC },
			new EMultiModalValidator()
	);
	
	public final RuleID[] rules;
	public final Relation[] relations;
	public final FormulaValidator validator;
	private AxiomSystem(RuleID[] rules, Relation[] relations, FormulaValidator validator) {
		this.rules = rules;
		this.relations = relations;
		this.validator = validator;
	}
	
	public Prover buildProver()
	{
		return new Prover(this);
	}
}