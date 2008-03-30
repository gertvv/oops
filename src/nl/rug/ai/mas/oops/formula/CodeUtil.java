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

public class CodeUtil {
	public static final BigInteger ONE = BigInteger.ONE;
	public static final BigInteger TWO = ONE.add(ONE);
	public static final BigInteger THREE = TWO.add(ONE);

	public static final BigInteger IDENTITY = ONE;

	/**
	 * Code a unary operator
	 */
	public static BigInteger codeUnary(BigInteger op, BigInteger arg) {
		return pair(op, arg);
	}

	/**
	 * Code a binary operator
	 */
	public static BigInteger codeBinary(
			BigInteger op, BigInteger left, BigInteger right) {
		return pair(op, pair(left, right));
	}

	/**
	 * Code a proposition
	 */
	public static BigInteger codeProposition(BigInteger i) {
		return codeUnary(IDENTITY, i.multiply(TWO));
	}

	/**
	 * Code a variable
	 */
	public static BigInteger codeVariable(BigInteger i) {
		return codeUnary(IDENTITY, i.multiply(TWO).add(ONE));
	}

	/**
	 * Code a modal operator
	 */
	public static BigInteger codeModal(
			BigInteger op, BigInteger a, BigInteger f) {
		return codeUnary(op.add(a.multiply(TWO)), f);
	}

	public static BigInteger pair(BigInteger x, BigInteger y) {
		return x.pow(2).add(
				x.multiply(y).multiply(TWO)
			).add(
				y.pow(2)
			).add(
				x.multiply(THREE)
			).add(y);
	}
}
