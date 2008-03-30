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

package nl.rug.ai.mas.oops.formula;

/**
 * Context in which to interpret a formula.
 */
public class Context {
	private PropositionMap d_propMap;
	private AgentIdMap d_aidMap;
	private VariableCodeMap<Formula> d_formulaCodeMap;
	private VariableCodeMap<Agent> d_agentCodeMap;

	public Context() {
		d_propMap = new PropositionMap();
		d_aidMap = new AgentIdMap();
		d_formulaCodeMap = new VariableCodeMap<Formula>();
		d_agentCodeMap = new VariableCodeMap<Agent>();
	}

	public Context(PropositionMap p, AgentIdMap a, VariableCodeMap<Formula> vf,
			VariableCodeMap<Agent> va) {
		d_propMap = p;
		d_aidMap = a;
		d_formulaCodeMap = vf;
		d_agentCodeMap = va;
	}

	public PropositionMap getPropositionMap() {
		return d_propMap;
	}

	public AgentIdMap getAgentIdMap() {
		return d_aidMap;
	}

	public VariableCodeMap<Formula> getFormulaCodeMap() {
		return d_formulaCodeMap;
	}

	public VariableCodeMap<Agent> getAgentCodeMap() {
		return d_agentCodeMap;
	}
}
