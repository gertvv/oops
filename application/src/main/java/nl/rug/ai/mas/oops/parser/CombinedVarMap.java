/**
 * 
 */
package nl.rug.ai.mas.oops.parser;

import nl.rug.ai.mas.oops.formula.Variable;
import nl.rug.ai.mas.oops.formula.VariableCodeMap;
import nl.rug.ai.mas.oops.formula.VariableMap;

public class CombinedVarMap<T> {
	private VariableMap<T> d_varMap;
	private VariableCodeMap<T> d_codeMap;

	public CombinedVarMap() {
		this(new VariableCodeMap<T>());
	}

	public CombinedVarMap(VariableCodeMap<T> codeMap) {
		d_varMap = new VariableMap<T>();
		d_codeMap = codeMap;
	}

	public VariableCodeMap<T> getCodeMap() {
		return d_codeMap;
	}

	public Variable<T> getOrCreate(String name) {
		return d_varMap.getOrCreate(name);
	}

	public int code(Variable<T> var) {
		return d_codeMap.code(var);
	}
}