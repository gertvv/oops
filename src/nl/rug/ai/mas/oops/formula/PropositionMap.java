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

import java.util.*;

import java.math.BigInteger;

/**
 * A map from proposition symbols to a class implementing them. This enables
 * identification of different propositions.
 */
public class PropositionMap extends HashMap<String, Proposition> {
	private BigInteger d_code;

	public PropositionMap() {
		super();
		d_code = BigInteger.ZERO;
	}

	public Proposition getOrCreate(String name) {
		Proposition p = get(name);
		if (p != null)
			return p;
		d_code = d_code.add(BigInteger.ONE);
		p = new Proposition(name, d_code);
		put(name, p);
		return p;
	}
}
