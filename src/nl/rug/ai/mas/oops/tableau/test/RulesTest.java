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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import nl.rug.ai.mas.oops.formula.*;
import nl.rug.ai.mas.oops.tableau.*;

/**
 * Test functionality related to rules.
 */
public class RulesTest {
	@Test public void bNecS2Test0() {
		Context c = new Context();
		// build rule
		Rule r = ModalRuleFactory.buildBNecS2(c);
		// build node
		Node n = buildNode1(c);

		// perform match
		Match m = r.match(n);
		assertNotNull(m);
	}

	@Test public void bNecS2Test1() {
		Context c = new Context();
		// build rule
		ModalRule r = (ModalRule)ModalRuleFactory.buildBNecS2(c);
		// build node
		Node n = buildNode1(c);

		assertNull(r.getConstraint());

		assertNotNull(r.getTemplate().match(n, r.getConstraint()));
	}

	@Test public void SNecS2Test() {
		Context c = new Context();
		// build rule
		Rule r = ModalRuleFactory.buildSNecS2(c);
		// build node
		Node n = buildNode1(c);

		// perform match
		Match m = r.match(n);
		assertNotNull(m);
	}

	/**
	 * build formula and label:
	 * s.k_1 ~%_i p
	 */
	private Node buildNode1(Context c) {
		Proposition p = new Proposition("p", 1);
		AgentId a = new AgentId("1", 1);
		Formula f = new Negation(new MultiDiamond(a, p));
		World w0 = new WorldInstance(null);
		World w1 = new WorldInstance(null);
		Label l = new LabelInstance(
			new LabelInstance(new NullLabel(), w0, new NullAgent()),
			w1, a);
		return new Node(l, f);
	}
}
