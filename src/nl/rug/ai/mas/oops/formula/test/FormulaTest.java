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

package nl.rug.ai.mas.oops.formula.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import nl.rug.ai.mas.oops.formula.*;

/**
 * Test functionality related to Formula.
 */
public class FormulaTest {
	@Test public void variableDiamondMatchTest() {
		// build template
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i);
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f);
		Formula tpl = new MultiDiamond(iref, fref);
		
		// build formula
		Agent a = new AgentId("1");
		Formula p = new Proposition("p");
		Formula fml = new MultiDiamond(a, p);

		assertNotNull(tpl.match(fml));
	}

	@Test public void bNecTest() {
		// build template
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i);
		Variable<Formula> f = new Variable<Formula>("F");
		FormulaReference fref = new FormulaReference(f);
		Formula tpl = new Negation(new MultiDiamond(iref, fref));
		
		// build formula
		Agent a = new AgentId("1");
		Formula p = new Proposition("p");
		Formula fml = new Negation(new MultiDiamond(a, p));

		assertNotNull(tpl.match(fml));
	}

	/**
	 * []%_3#_3#_2#_3a should equal %_3#_3#_2#_3a
	 */
	@Test public void complexModalTest() {
		Agent a3 = new AgentId("3");
		Agent a2 = new AgentId("2");
		Proposition pA = new Proposition("a");
		Formula f0 = new MultiDiamond(a3, new MultiBox(a3, new MultiBox(a2,
			new MultiBox(a3, pA))));
		FullSubstitution s = new FullSubstitution();
		Formula f1 = f0.substitute(s);

		assertEquals(f0, f1);
	}
}
