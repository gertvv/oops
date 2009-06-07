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

package nl.rug.ai.mas.oops.parser;

import nl.rug.ai.mas.oops.formula.Agent;
import nl.rug.ai.mas.oops.formula.Formula;
import nl.rug.ai.mas.oops.formula.VariableCodeMap;

/**
 * Context in which to interpret a formula.
 */
public class Context {
	private PropositionMap d_propMap;
	private AgentIdMap d_aidMap;
	private FormulaVarMap d_fVarMap;
	private AgentVarMap d_aVarMap;
	private VariableCodeMap<Formula> d_formulaCodeMap;
	private VariableCodeMap<Agent> d_agentCodeMap;

	public Context() {
		d_propMap = new PropositionMap();
		d_aidMap = new AgentIdMap();
		d_formulaCodeMap = new VariableCodeMap<Formula>();
		d_agentCodeMap = new VariableCodeMap<Agent>();
		d_fVarMap = new FormulaVarMap(d_formulaCodeMap);
		d_aVarMap = new AgentVarMap(d_agentCodeMap);
	}

	public PropositionMap getPropositionMap() {
		return d_propMap;
	}

	public AgentIdMap getAgentIdMap() {
		return d_aidMap;
	}
	
	public FormulaVarMap getFormulaVarMap() {
		return d_fVarMap;
	}
	
	public AgentVarMap getAgentVarMap() {
		return d_aVarMap;
	}

	public VariableCodeMap<Formula> getFormulaCodeMap() {
		return d_formulaCodeMap;
	}

	public VariableCodeMap<Agent> getAgentCodeMap() {
		return d_agentCodeMap;
	}
}
