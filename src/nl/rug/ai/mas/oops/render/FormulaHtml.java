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

package nl.rug.ai.mas.oops.render;

import nl.rug.ai.mas.oops.formula.*;

import java.util.Stack;

/**
 * Visit a Formula in order to generate an HTML description from it.
 */
class FormulaHtml implements FormulaVisitor {
	public static String CONJ = "&and;";
	public static String DISJ = "&or;";
	public static String NEG = "&not;";
	public static String IMPL = "&rarr;";
	public static String BIIM = "&harr;";
	public static String LOZENGE = "&#9674;";
	public static String SQUARE = "&#9723;";
	public static String AGENT(Agent a) {
		return "<sub>" + a.toString() + "</sub>";
	}

	private Stack<String> d_stack;

	public FormulaHtml() {
		d_stack = new Stack<String>();
	}

	public String getHtml() {
		return d_stack.pop();
	}

	public void visitBiImplication(BiImplication f) {
		String right = d_stack.pop();
		String left = d_stack.pop();
		d_stack.push("(" + left + BIIM + right + ")");
	}

	public void visitConjunction(Conjunction f) {
		String right = d_stack.pop();
		String left = d_stack.pop();
		d_stack.push("(" + left + CONJ + right + ")");
	}

	public void visitDisjunction(Disjunction f) {
		String right = d_stack.pop();
		String left = d_stack.pop();
		d_stack.push("(" + left + DISJ + right + ")");
	}

	public void visitImplication(Implication f) { 
		String right = d_stack.pop();
		String left = d_stack.pop();
		d_stack.push("(" + left + IMPL + right + ")");
	}

	public void visitMultiBox(MultiBox f) { 
		String right = d_stack.pop();
		d_stack.push(SQUARE + AGENT(f.getAgent()) + right);
	}

	public void visitMultiDiamond(MultiDiamond f) { 
		String right = d_stack.pop();
		d_stack.push(LOZENGE + AGENT(f.getAgent()) + right);
	}

	public void visitNegation(Negation f) { 
		String right = d_stack.pop();
		d_stack.push(NEG + right);
	}

	public void visitProposition(Proposition f) {
		d_stack.push(f.toString());
	}

	public void visitUniBox(UniBox f) { 
		String right = d_stack.pop();
		d_stack.push(SQUARE + right);
	}

	public void visitUniDiamond(UniDiamond f) {
		String right = d_stack.pop();
		d_stack.push(LOZENGE + right);
	}

	public void visitFormulaReference(FormulaReference f) {
		d_stack.push(f.toString());
	}
}
