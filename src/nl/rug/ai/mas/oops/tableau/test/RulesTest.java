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

package nl.rug.ai.mas.prover.tableau.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import nl.rug.ai.mas.prover.formula.*;
import nl.rug.ai.mas.prover.tableau.*;

/**
 * Test functionality related to rules.
 */
public class RulesTest {
	@Test public void bNecS2Test0() {
		// build rule
		Rule r = ModalRuleFactory.buildBNecS2();
		// build node
		Node n = buildNode1();

		// perform match
		Match m = r.match(n);
		assertNotNull(m);
	}

	@Test public void bNecS2Test1() {
		// build rule
		ModalRule r = (ModalRule)ModalRuleFactory.buildBNecS2();
		// build node
		Node n = buildNode1();

		assertNull(r.getConstraint());

		assertNotNull(r.getTemplate().match(n, r.getConstraint()));
	}

	@Test public void SNecS2Test() {
		// build rule
		Rule r = ModalRuleFactory.buildSNecS2();
		// build node
		Node n = buildNode1();

		// perform match
		Match m = r.match(n);
		assertNotNull(m);
	}

	/**
	 * build formula and label:
	 * s.k_1 ~%_i p
	 */
	private Node buildNode1() {
		Proposition p = new Proposition("p");
		AgentId a = new AgentId("1");
		Formula f = new Negation(new MultiDiamond(a, p));
		World w0 = new WorldInstance();
		World w1 = new WorldInstance();
		Label l = new LabelInstance(
			new LabelInstance(new NullLabel(), w0, new AgentId("NoAgent")),
			w1, a);
		return new Node(l, f);
	}
}
