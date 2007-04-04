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

/**
 * Class to transform a parse tree into a formula tree.
 */
class TransformTest extends DepthFirstAdapter {
	public static void main (String[] argv) {
		try {
			Lexer l = new Lexer (new PushbackReader (new BufferedReader(
							new InputStreamReader (System.in))));
			Parser p = new Parser (l);
			Start start = p.parse ();

			TransformTest tr = new TransformTest();
			start.apply(tr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class StackEntry {
		public Formula d_formula;
		public VariableMap d_variableMap;

		public String toString() {
			return d_formula.toString();
		}
	}

	private LinkedList<StackEntry> d_stack = new LinkedList<StackEntry>(); 
	private LinkedList<StackEntry> d_formulaList = new LinkedList<StackEntry>();
	PropositionMap d_propMap = new PropositionMap();

	public void outAPropositionFormula(APropositionFormula node) {
		StackEntry entry = new StackEntry();
		entry.d_formula = d_propMap.getOrCreate(node.getProp().getText());
		entry.d_variableMap = new VariableMap();
		d_stack.addLast(entry);
	}

	public void outAVariableFormula(AVariableFormula node) {
		VariableMap m = new VariableMap();
		Variable v = m.getOrCreate(node.getVar().getText());
		VariableReference r = new VariableReference(v);
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

	public void outAFormulaList(AFormulaList node) {
		while(!d_stack.isEmpty()) {
			d_formulaList.add(d_stack.removeFirst());
		}
	}

	public void outAFormulaCommand(AFormulaCommand Node) {
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
				Substitution s = f0.match(f1);
				if (s != null) {
					System.out.println(s.toString() + f0 + " = " + f1);
				} else {
					System.out.println(f0 + " != " + f1);
				}
			}
		}
	}
}
