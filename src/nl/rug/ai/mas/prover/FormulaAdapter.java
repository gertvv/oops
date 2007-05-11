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
	
	public static void main (String[] argv) {
		FormulaAdapter fa = new FormulaAdapter();
		if (fa.parse(System.in)) {
			testTableau(fa.getFormula());
		} else {
			System.out.println(fa.getErrorCause());
		}
	}

	private static void testTableau(Formula f) {
		Vector<Rule> rules = PropositionalRuleFactory.build();
		rules.addAll(ModalRuleFactory.build());
		Tableau t = new Tableau(rules);

		Tableau.BranchState result = t.tableau(f);
		System.out.println(f + " is " + result + 
			(result == Tableau.BranchState.ERROR ? " " + t.getError() : ""));
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

/*
	public void outAFormulaList(AFormulaList node) {
		while(!d_stack.isEmpty()) {
			d_formulaList.add(d_stack.removeFirst());
		}
	}

	public void outAFormulaCommand(AFormulaCommand Node) {
		// print the list of agents
		System.out.println(d_aidMap);
		// print the formula
		System.out.println(d_stack.removeLast().d_formula);
	}

	public void outAFormulaListCommand(AFormulaListCommand Node) {
		// print out the formula list
		System.out.println(d_formulaList);

		// test equality among all formulas
		ListIterator<StackEntry> it0 = d_formulaList.listIterator(0);
		while (it0.hasNext()) {
			Formula f0 = it0.next().d_formula;
			ListIterator<StackEntry> it1 =
				d_formulaList.listIterator(it0.nextIndex());
			while (it1.hasNext()) {
				Formula f1 = it1.next().d_formula;
				if (f1.equals(f0)) {
					System.out.println(f0 + " equals " + f1);
				}
			}
		}

		// try matching formulas to eachother
		it0 = d_formulaList.listIterator(0);
		while (it0.hasNext()) {
			Formula f0 = it0.next().d_formula;
			ListIterator<StackEntry> it1 =
				d_formulaList.listIterator(0);
			while (it1.hasNext()) {
				Formula f1 = it1.next().d_formula;
				FullSubstitution s = f0.match(f1);
				if (s != null) {
					System.out.println(s.toString() + f0 + " = " + f1);
				} else {
					System.out.println(f0 + " != " + f1);
				}
			}
		}

		testRuleMatch();

		testTableau();
	}

	private void testRuleMatch() {
		System.out.println();
		Vector<Rule> rules = PropositionalRuleFactory.build();
		rules.addAll(ModalRuleFactory.build());

		ListIterator<StackEntry> it = d_formulaList.listIterator(0);
		while (it.hasNext()) {
			Formula f = it.next().d_formula;
			nl.rug.ai.mas.prover.tableau.Node n = 
				new nl.rug.ai.mas.prover.tableau.Node(
					new LabelInstance(
						new NullLabel(), new WorldInstance(), new AgentId("NoAgent")
						), f);
			for (Rule r : rules) {
				Match m = r.match(n);
				if (m != null) 
					System.out.println(n + " -- " + m);
			}
		}
	}

	private void testTableau() {
		System.out.println();
		Vector<Rule> rules = PropositionalRuleFactory.build();
		rules.addAll(ModalRuleFactory.build());
		Tableau t = new Tableau(rules);

		ListIterator<StackEntry> it = d_formulaList.listIterator(0);
		while (it.hasNext()) {
			Formula f = it.next().d_formula;
			Tableau.BranchState result = t.tableau(f);
			System.out.println(f + " is " + result + 
					(result == Tableau.BranchState.ERROR ? " " + t.getError() : ""));
		}
		
	}
	*/
}
