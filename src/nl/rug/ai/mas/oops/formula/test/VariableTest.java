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

package nl.rug.ai.mas.prover.formula.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import nl.rug.ai.mas.prover.formula.*;

/**
 * Test functionality related to Variable.
 */
public class VariableTest {
	@Test public void referenceEqualTest() {
		Variable<String> var = new Variable<String>("V");
		VariableReference<String> ref0 = new VariableReference<String>(var);
		VariableReference<String> ref1 = new VariableReference<String>(var);
		assertEquals(ref0, ref1);
	}

	@Test public void referenceHashCodeEqualTest() {
		Variable<String> var = new Variable<String>("V");
		VariableReference<String> ref0 = new VariableReference<String>(var);
		VariableReference<String> ref1 = new VariableReference<String>(var);
		assertEquals(ref0.hashCode(), ref1.hashCode());
	}

	@Test public void referenceSetTest() {
		// test setting references
	}
}
