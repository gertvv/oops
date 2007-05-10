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
		/* 	#1  */	assertTrue(s_prover.proveable("#_1 %_1 %_3 #_3 #_2 #_1 a > a"));
		/* 	#2  */	assertTrue(s_prover.proveable("~(#_1(a | b) & %_1 ~a & %_1(~a & b))"));
		/* 	#3 	*/	assertTrue(s_prover.proveable("~%_1(~a & a)"));
		/* 	#4 	*/	assertTrue(s_prover.proveable("#_1 %_1 %_3 #_3 #_2 #_3 a > %_1 %_3 a");
		/* 	#5 	*/	assertTrue(s_prover.proveable("~(#_1(a | b) & %_1 ~a & %_1 #_1 %_3 ~b & (%_1 #_2 %_1(~a & ~b) | %_1(~a & ~b) | #_1 a))");
		/* 	#6 	*/	assertTrue(s_prover.proveable("~(#_1 %_1 %_3 #_3 #_2 #_3 a & #_1 %_3 %_2 #_3 %_2 ~a)");
		/* 	#7 	*/	assertTrue(s_prover.proveable("#_1 %_1 %_3 #_3 #_2 #_3 a & #_1 %_3 #_2 (~a | x) & #_1 ~x)");
		/* 	#8 	*/	assertTrue(s_prover.proveable("#_1 (a > b) > ( #_1 a > #_1 b)");
		/* 	#9 	*/	assertTrue(s_prover.proveable("#_1 a > a");
		/* #10	*/	assertTrue(s_prover.proveable("#_1 a > #_1 #_1 a");
		/* #11	*/	assertTrue(s_prover.proveable("%_1 a > #_1 %_1 a");
		/* #12	*/	assertTrue(s_prover.proveable("%_1 (#_1 p | %_2 #_2 #_1 q > #_1 (~q > p)");
		/* #13	*/	assertTrue(s_prover.proveable("((p & ~#_1 p) > ~p | ((p & ~#_1 p) > ~((p & ~#_1 p) > ~#_1((p & ~#_1 p) > p )))");
		/* #14	*/	assertTrue(s_prover.proveable("(#_1 (a | b) & (c > #_1 ~b) & (#_1 a > d)) > (c > d)");
		/* #15	*/	assertTrue(s_prover.proveable("(#_1(g = #_1 p) & %_1 g) > (g & p)");
		/* #16	*/	assertTrue(s_prover.proveable("(#_1(g = #_1 p) & %_1 ~g) > ~g");
		/* #17	*/	assertTrue(s_prover.proveable("(#_1p & #_1((a & p) > b)) > #_1 (a > b)");
		
		/* #18  */	assertFalse(s_prover.proveable("(#_1 p & #_1 ((a & p) > b)) > (%_1 ~b & #_1 (a > b))");
		/* #19  */	assertFalse(s_prover.proveable("#_1 %_1 %_3 #_3 #_2 #_3 a > b");
		/* #20	*/	assertFalse(s_prover.proveable("#_1 (a | ~b) & %_1 ~a & %_1 b)");
		/* #21	*/	assertFalse(s_prover.proveable("#_1 %_1 %_3 #_3 #_2 %_3 a > #_1 #_3 a");
		/* #22	*/	assertFalse(s_prover.proveable("~(#_1 (a | b) & %_1 ~a & (%_1 (~a & ~b) | %_1 a))");
		/* #23	*/	assertFalse(s_prover.proveable("~(#_1 %_1 %_3 $_3 #_2 #_3 a & %_1 %_3 #_3 #_2 (~a | x) & ~x)");
		/* #24	*/	assertFalse(s_prover.proveable("((p & ~#_1 p) > p | ((p & ~#_1 p) > ~((p & ~#_1 p) > #_1((p & ~#_1 p) > p )))");
		/* #25	*/	assertFalse(s_prover.proveable("~(#_1 %_3 (%_1 a | #_4 b) & #_3 (#_1 #_3 ~a & %_3 #_4 #_3 %_6))");
	}
}
