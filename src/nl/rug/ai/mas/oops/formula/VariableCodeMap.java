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

import java.util.HashMap;
import java.math.BigInteger;

/**
 * A map from Variable to code numbers.
 */
public class VariableCodeMap<T> {
	private HashMap<Variable<T>, BigInteger> d_map;
	private BigInteger d_code;

	public VariableCodeMap() {
		d_map = new HashMap<Variable<T>, BigInteger>();
		d_code = BigInteger.ZERO;
	}

	public BigInteger code(Variable<T> k) {
		BigInteger v = d_map.get(k);
		if (v != null)
			return v;
		d_code = d_code.add(BigInteger.ONE);
		d_map.put(k, d_code);
		return d_code;
	}
}
