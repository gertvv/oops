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

import java.math.BigInteger;
import java.util.Stack;

/**
 * Visit a formula to code it into the natural numbers.
 */
public class CodingVisitor implements FormulaVisitor {
	private Stack<BigInteger> d_stack;

	public CodingVisitor() {
		d_stack = new Stack<BigInteger>();
	}

	public BigInteger getCode() {
		return d_stack.pop();
	}

	public void visitBiImplication(BiImplication f) {
		visitBinary(f);
	}

	public void visitConjunction(Conjunction f) {
		visitBinary(f);
	}

	public void visitDisjunction(Disjunction f) {
		visitBinary(f);
	}

	public void visitImplication(Implication f) {
		visitBinary(f);
	}

	public void visitMultiBox(MultiBox f) {
		visitUnary(f);
	}

	public void visitMultiDiamond(MultiDiamond f) {
		visitUnary(f);
	}

	public void visitNegation(Negation f) {
		visitUnary(f);
	}

	public void visitProposition(Proposition f) {
		BigInteger current = CodeUtil.codeProposition(
			BigInteger.valueOf(f.code()));
		d_stack.push(current);
	}

	public void visitUniBox(UniBox f) {
		visitUnary(f);
	}

	public void visitUniDiamond(UniDiamond f) {
		visitUnary(f);
	}

	public void visitFormulaReference(FormulaReference f) {
		BigInteger current = CodeUtil.codeVariable(
			BigInteger.valueOf(f.code()));
		d_stack.push(current);
	}

	private void visitBinary(Formula f) {
		BigInteger right = d_stack.pop();
		BigInteger left = d_stack.pop();
		BigInteger op = BigInteger.valueOf(f.code());
		BigInteger current = CodeUtil.codeBinary(op, left, right);
		d_stack.push(current);
	}

	private void visitUnary(Formula f) {
		BigInteger right = d_stack.pop();
		BigInteger op = BigInteger.valueOf(f.code());
		BigInteger current = CodeUtil.codeUnary(op, right);
		d_stack.push(current);
	}
}
