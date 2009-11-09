/**
 * 
 */
package nl.rug.ai.mas.oops.parser;

import nl.rug.ai.mas.oops.formula.Formula;
import nl.rug.ai.mas.oops.formula.FormulaReference;
import nl.rug.ai.mas.oops.formula.Variable;
import nl.rug.ai.mas.oops.formula.VariableCodeMap;

public class FormulaVarMap extends CombinedVarMap<Formula> {
	public FormulaVarMap() {
		super();
	}

	public FormulaVarMap(VariableCodeMap<Formula> map) {
		super(map);
	}

	public FormulaReference getOrCreateReference(String name) {
		Variable<Formula> v = super.getOrCreate(name);
		FormulaReference r = new FormulaReference(v, super.code(v));
		v.add(r);
		return r;
	}
}