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

import java.io.*;
import java.util.*;

import nl.rug.ai.mas.oops.parser.parser.*;
import nl.rug.ai.mas.oops.parser.lexer.*;
import nl.rug.ai.mas.oops.parser.analysis.*;
import nl.rug.ai.mas.oops.parser.node.*;

import nl.rug.ai.mas.oops.formula.*;
import nl.rug.ai.mas.oops.tableau.*;

/**
 * Class to transform a parse tree into a formula tree.
 */
class FormulaAdapter extends DepthFirstAdapter {
	private Context d_context;

	public FormulaAdapter(Context c) {
		d_context = c;
	}

	public boolean parse(InputStream is) {
		try {
			reset(); // reset internal variables

			Lexer l = new Lexer (new PushbackReader (new BufferedReader(
							new InputStreamReader(is))));
			Parser p = new Parser (l);
			Start start = p.parse ();

			start.apply(this);
		} catch (Exception e) {
			d_errorCause = e;
			return false;
		}
		return true;
	}

	public Formula getFormula() {
		StackEntry first = d_stack.getFirst();
		return (first != null ? first.d_formula : null);
	}

	public Context getContext() {
		return new Context(d_propMap, d_aidMap, d_varMap.getCodeMap(),
			new VariableCodeMap<Agent>());
	}

	public Exception getErrorCause() {
		return d_errorCause;
	}

	private class CombinedVarMap<T> {
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

	private class FormulaVarMap extends CombinedVarMap<Formula> {
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

	private class AgentVarMap extends CombinedVarMap<Agent> {
		// FIXME: not used
	}

	private class StackEntry {
		public Formula d_formula = null;
		public Agent d_agent = null;
		// public VariableMap<Formula> d_variableMap = null;
		// public VariableMap<Agent> d_agentMap = null; FIXME: not used

		public String toString() {
			return d_formula.toString();
		}
	}

	private LinkedList<StackEntry> d_stack;
	private PropositionMap d_propMap;
	private AgentIdMap d_aidMap;
	private FormulaVarMap d_varMap;
	private Exception d_errorCause;

	private void reset() {
		d_stack = new LinkedList<StackEntry>(); 
		d_propMap = d_context.getPropositionMap();
		d_aidMap = d_context.getAgentIdMap();
		d_varMap = new FormulaVarMap(d_context.getFormulaCodeMap());
		d_errorCause = null;
	}

	public void outAPropositionFormula(APropositionFormula node) {
		StackEntry entry = new StackEntry();
		entry.d_formula = d_propMap.getOrCreate(node.getProp().getText());
		d_stack.addLast(entry);
	}

	public void outAId(AId node) {
		StackEntry entry = new StackEntry();
		entry.d_agent = d_aidMap.getOrCreate(node.getId().getText());
		d_stack.addLast(entry);
	}

	public void outABoxFormula(ABoxFormula node) {
		StackEntry e = d_stack.removeLast();
		StackEntry a = d_stack.getLast();
		if (a.d_agent != null) {
			d_stack.removeLast();
			e.d_formula = new MultiBox(a.d_agent, e.d_formula);
		} else {
			e.d_formula = new UniBox(e.d_formula);
		}
		d_stack.addLast(e);
	}

	public void outADiamondFormula(ADiamondFormula node) {
		StackEntry e = d_stack.removeLast();
		StackEntry a = d_stack.getLast();
		if (a.d_agent != null) {
			d_stack.removeLast();
			e.d_formula = new MultiDiamond(a.d_agent, e.d_formula);
		} else {
			e.d_formula = new UniDiamond(e.d_formula);
		}
		d_stack.addLast(e);
	}

	public void outAVariableFormula(AVariableFormula node) {
		FormulaReference r = d_varMap.getOrCreateReference(
			node.getVar().getText());
		StackEntry entry = new StackEntry();
		entry.d_formula = r;

		d_stack.addLast(entry);
	}

	public void outANegationFormula(ANegationFormula node) {
		StackEntry e = d_stack.removeLast();
		e.d_formula = new Negation(e.d_formula);
		d_stack.addLast(e);
	}

	public void outAConjunctionFormula(AConjunctionFormula node) {
		StackEntry right = d_stack.removeLast();
		StackEntry left = d_stack.removeLast();
		left.d_formula = new Conjunction(left.d_formula, right.d_formula);
		d_stack.addLast(left);
	}

	public void outADisjunctionFormula(ADisjunctionFormula node) {
		StackEntry right = d_stack.removeLast();
		StackEntry left = d_stack.removeLast();
		left.d_formula = new Disjunction(left.d_formula, right.d_formula);
		d_stack.addLast(left);
	}

	public void outAImplicationFormula(AImplicationFormula node) {
		StackEntry right = d_stack.removeLast();
		StackEntry left = d_stack.removeLast();
		left.d_formula = new Implication(left.d_formula, right.d_formula);
		d_stack.addLast(left);
	}

	public void outABiImplicationFormula(ABiImplicationFormula node) {
		StackEntry right = d_stack.removeLast();
		StackEntry left = d_stack.removeLast();
		left.d_formula = new BiImplication(left.d_formula, right.d_formula);
		d_stack.addLast(left);
	}
}
