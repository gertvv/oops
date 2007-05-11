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

package nl.rug.ai.mas.oops.tableau.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import nl.rug.ai.mas.oops.formula.*;
import nl.rug.ai.mas.oops.tableau.*;

/**
 * Test functionality related to Node.
 */
public class NodeTest {
	@Test public void testNullConstraint() {
		NodeSubstitution ns = new NodeSubstitution();
		LabelSubstitution ls = new LabelSubstitution();
		FullSubstitution fs = new FullSubstitution();

		assertTrue(ns.merge(ls, fs));
	}
}
