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

import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Vector;
import nl.rug.ai.mas.prover.tableau.*;

public class ProverTest {
	private static Prover s_prover;

	@BeforeClass public static void initProver() {
		Vector<Rule> rules = PropositionalRuleFactory.build();
		rules.addAll(ModalRuleFactory.build());
		s_prover = new Prover(rules);
	}

	@Test public void test0() throws TableauErrorException {
		assertTrue(s_prover.proveable("#_1 %_1 %_3 #_3 #_2 #_1 a > a"));
	}
}
