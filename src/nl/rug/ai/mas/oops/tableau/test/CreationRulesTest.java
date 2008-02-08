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
import static org.junit.Assert.assertTrue;

import nl.rug.ai.mas.oops.formula.*;
import nl.rug.ai.mas.oops.tableau.*;
import nl.rug.ai.mas.oops.*;

/**
 * Test functionality related to Label.
 */
public class CreationRulesTest {
	/**
	 * This
	        {NullLabel.(World(15dfd77),NoAgent).(n,1),a}
			{NullLabel.(World(15dfd77),NoAgent).(n,2),b}
	 * should entail
			{NullLabel.(World(15dfd77),NoAgent).(n,1),a}
	 */
	@Test public void entailsTest() {
		// introduce necessary constants
		Proposition pA = new Proposition("a");
		Proposition pB = new Proposition("b");
		Label nullLabel = new NullLabel();
		World topWorld = new WorldInstance(null);
		Agent noAgent = new AgentId("NoAgent");
		Agent a1 = new AgentId("1");
		Agent a2 = new AgentId("2");
		Label topLabel = new LabelInstance(nullLabel, topWorld, noAgent);

		// world variables
		Variable<World> wN = new Variable<World>("n");
		WorldReference rN = new WorldReference(wN);
		Variable<World> wM = new Variable<World>("m");
		WorldReference rM = new WorldReference(wM);
		Variable<World> wO = new Variable<World>("o");
		WorldReference rO = new WorldReference(wO);

		// add first two nodes to CreationRules
		CreationRules cr = new CreationRules(null);
		cr.add(new Node(new LabelInstance(topLabel, rN, a1), pA));
		cr.add(new Node(new LabelInstance(topLabel, rM, a2), pB));

		// entails test
		assertTrue(cr.entails(new Node(new LabelInstance(topLabel, rO, a1), pA)));
	}

	/*
	 * This
	        {NullLabel.(World(15dfd77),NoAgent).(n,1),%_3#_3#_2#_3a}
			{NullLabel.(World(15dfd77),NoAgent).(n,3),#_3#_2(~a | x)}
	 * should entail
			{NullLabel.(World(15dfd77),NoAgent).(n,1),%_3#_3#_2#_3a}
	 */
}
