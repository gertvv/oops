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

import java.io.*;
import java.util.*;

import nl.rug.ai.mas.oops.parser.parser.*;
import nl.rug.ai.mas.oops.parser.lexer.*;
import nl.rug.ai.mas.oops.parser.analysis.*;
import nl.rug.ai.mas.oops.parser.node.*;

import nl.rug.ai.mas.oops.formula.*;

/**
 * Class to transform a parse tree into a formula tree.
 */
public class FormulaParser extends DepthFirstAdapter {
	private Context d_context;

	public FormulaParser(Context c) {
		d_context = c;
	}
	
	public boolean parse(String s) {
		return parse(new ByteArrayInputStream(s.getBytes()));
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
		return d_context;
	}

	public Exception getErrorCause() {
		return d_errorCause;
	}

	private class StackEntry {
		public Formula d_formula = null;
		public Agent d_agent = null;
		
		public String toString() {
			return d_formula.toString();
		}
	}

	private LinkedList<StackEntry> d_stack;
	private Exception d_errorCause;

	private void reset() {
		d_stack = new LinkedList<StackEntry>(); 
		d_errorCause = null;
	}

	@Override
	public void outAPropositionFormula(APropositionFormula node) {
		StackEntry entry = new StackEntry();
		entry.d_formula = getPropositionMap().getOrCreate(node.getProp().getText());
		d_stack.addLast(entry);
	}

	@Override
	public void outABoxFormula(ABoxFormula node) {
		StackEntry e = d_stack.removeLast();
		if (!d_stack.isEmpty() && d_stack.getLast().d_agent != null) {
			StackEntry a = d_stack.removeLast();
			e.d_formula = new MultiBox(a.d_agent, e.d_formula);
		} else {
			e.d_formula = new UniBox(e.d_formula);
		}
		d_stack.addLast(e);
	}

	@Override
	public void outADiamondFormula(ADiamondFormula node) {
		StackEntry e = d_stack.removeLast();
		if (!d_stack.isEmpty() && d_stack.getLast().d_agent != null) {
			StackEntry a = d_stack.removeLast();
			e.d_formula = new MultiDiamond(a.d_agent, e.d_formula);
		} else {
			e.d_formula = new UniDiamond(e.d_formula);
		}
		d_stack.addLast(e);
	}

	@Override
	public void outAVariableFormula(AVariableFormula node) {
		FormulaReference r = getFormulaVarMap().getOrCreateReference(
			node.getVar().getText());
		StackEntry entry = new StackEntry();
		entry.d_formula = r;

		d_stack.addLast(entry);
	}

	@Override
	public void outANegationFormula(ANegationFormula node) {
		StackEntry e = d_stack.removeLast();
		e.d_formula = new Negation(e.d_formula);
		d_stack.addLast(e);
	}

	@Override
	public void outAConjunctionFormula(AConjunctionFormula node) {
		StackEntry right = d_stack.removeLast();
		StackEntry left = d_stack.removeLast();
		left.d_formula = new Conjunction(left.d_formula, right.d_formula);
		d_stack.addLast(left);
	}

	@Override
	public void outADisjunctionFormula(ADisjunctionFormula node) {
		StackEntry right = d_stack.removeLast();
		StackEntry left = d_stack.removeLast();
		left.d_formula = new Disjunction(left.d_formula, right.d_formula);
		d_stack.addLast(left);
	}

	@Override
	public void outAImplicationFormula(AImplicationFormula node) {
		StackEntry right = d_stack.removeLast();
		StackEntry left = d_stack.removeLast();
		left.d_formula = new Implication(left.d_formula, right.d_formula);
		d_stack.addLast(left);
	}

	@Override
	public void outABiImplicationFormula(ABiImplicationFormula node) {
		StackEntry right = d_stack.removeLast();
		StackEntry left = d_stack.removeLast();
		left.d_formula = new BiImplication(left.d_formula, right.d_formula);
		d_stack.addLast(left);
	}

	@Override
	public void outAIdAgent(AIdAgent node) {
		StackEntry entry = new StackEntry();
		entry.d_agent = getAgentIdMap().getOrCreate(node.getId().getText());
		d_stack.addLast(entry);
	}
	
	@Override
	public void outAVariableAgent(AVariableAgent node) {
		StackEntry entry = new StackEntry();
		entry.d_agent = getAgentVarMap().getOrCreateReference(node.getVar().getText());
		d_stack.addLast(entry);
	}

	private FormulaVarMap getFormulaVarMap() {
		return d_context.getFormulaVarMap();
	}

	private AgentVarMap getAgentVarMap() {
		return d_context.getAgentVarMap();
	}

	private PropositionMap getPropositionMap() {
		return d_context.getPropositionMap();
	}

	private AgentIdMap getAgentIdMap() {
		return d_context.getAgentIdMap();
	}
}
