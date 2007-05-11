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

package nl.rug.ai.mas.prover;

import java.io.*;
import java.util.*;

import nl.rug.ai.mas.prover.parser.parser.*;
import nl.rug.ai.mas.prover.parser.lexer.*;
import nl.rug.ai.mas.prover.parser.analysis.*;
import nl.rug.ai.mas.prover.parser.node.*;

import nl.rug.ai.mas.prover.formula.*;
import nl.rug.ai.mas.prover.tableau.*;

/**
 * Class to transform a parse tree into a formula tree.
 */
class FormulaAdapter extends DepthFirstAdapter {
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

	public Exception getErrorCause() {
		return d_errorCause;
	}

	private class StackEntry {
		public Formula d_formula = null;
		public Agent d_agent = null;
		public VariableMap<Formula> d_variableMap = null;
		public VariableMap<Agent> d_agentMap = null;

		public String toString() {
			return d_formula.toString();
		}
	}

	private LinkedList<StackEntry> d_stack;
	private PropositionMap d_propMap;
	private AgentIdMap d_aidMap;
	private Exception d_errorCause;

	private void reset() {
		d_stack = new LinkedList<StackEntry>(); 
		d_propMap = new PropositionMap();
		d_aidMap = new AgentIdMap();
		d_errorCause = null;
	}

	public void outAPropositionFormula(APropositionFormula node) {
		StackEntry entry = new StackEntry();
		entry.d_formula = d_propMap.getOrCreate(node.getProp().getText());
		entry.d_variableMap = new VariableMap<Formula>();
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
		VariableMap<Formula> m = new VariableMap<Formula>();
		Variable<Formula> v = m.getOrCreate(node.getVar().getText());
		FormulaReference r = new FormulaReference(v);
		v.add(r);

		StackEntry entry = new StackEntry();
		entry.d_formula = r;
		entry.d_variableMap = m;
		
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
		left.d_variableMap.merge(right.d_variableMap);
		d_stack.addLast(left);
	}

	public void outADisjunctionFormula(ADisjunctionFormula node) {
		StackEntry right = d_stack.removeLast();
		StackEntry left = d_stack.removeLast();
		left.d_formula = new Disjunction(left.d_formula, right.d_formula);
		left.d_variableMap.merge(right.d_variableMap);
		d_stack.addLast(left);
	}

	public void outAImplicationFormula(AImplicationFormula node) {
		StackEntry right = d_stack.removeLast();
		StackEntry left = d_stack.removeLast();
		left.d_formula = new Implication(left.d_formula, right.d_formula);
		left.d_variableMap.merge(right.d_variableMap);
		d_stack.addLast(left);
	}

	public void outABiImplicationFormula(ABiImplicationFormula node) {
		StackEntry right = d_stack.removeLast();
		StackEntry left = d_stack.removeLast();
		left.d_formula = new BiImplication(left.d_formula, right.d_formula);
		left.d_variableMap.merge(right.d_variableMap);
		d_stack.addLast(left);
	}
}
