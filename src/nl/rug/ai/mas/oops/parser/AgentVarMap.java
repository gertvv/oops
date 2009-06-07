/**
 * 
 */
package nl.rug.ai.mas.oops.parser;

import nl.rug.ai.mas.oops.formula.Agent;
import nl.rug.ai.mas.oops.formula.AgentReference;
import nl.rug.ai.mas.oops.formula.Variable;
import nl.rug.ai.mas.oops.formula.VariableCodeMap;

public class AgentVarMap extends CombinedVarMap<Agent> {
	public AgentVarMap() {
		super();
	}
	
	public AgentVarMap(VariableCodeMap<Agent> map) {
		super(map);
	}
	
	public AgentReference getOrCreateReference(String name) {
		Variable<Agent> v = super.getOrCreate(name);
		AgentReference r = new AgentReference(v, super.code(v));
		v.add(r);
		return r;
	}
}