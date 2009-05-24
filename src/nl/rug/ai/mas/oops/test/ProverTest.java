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

package nl.rug.ai.mas.oops.test;

import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Vector;
import nl.rug.ai.mas.oops.tableau.*;
import nl.rug.ai.mas.oops.*;
import nl.rug.ai.mas.oops.formula.Context;

public class ProverTest {
	private static SimpleProver s_prover;

	@BeforeClass public static void initProver() {
		s_prover = SimpleProver.build();
	}

	/* 	#1  */
	@Test public void test01() {
		try {
			assertTrue(s_prover.provable("#_1 %_1 %_3 #_3 #_2 #_1 a > a"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* 	#2  */
	@Test public void test02() {
		try {
			assertTrue(s_prover.provable(
				"~(#_1(a | ~b) & %_1 ~a & %_1(~a & b))"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* 	#3 	*/
	@Test public void test03() {
		try {
			assertTrue(s_prover.provable("~%_1(~a & a)"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* 	#4 	*/
	@Test public void test04() {
		try {
			assertTrue(s_prover.provable(
				"#_1 %_1 %_3 #_3 #_2 #_3 a > %_1 %_3 a"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* 	#5 	*/
	@Test public void test05() {
		try {
			assertTrue(s_prover.provable(
				"~(#_1(a | b) & %_1 ~a & %_1 #_1 %_3 ~b & " +
				"(%_1 #_2 %_1(~a & ~b) | %_1(~a & ~b) | #_1 a))"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* 	#6 	*/
	@Test public void test06() {
		try {
			assertTrue(s_prover.provable(
				"~(#_1 %_1 %_3 #_3 #_2 #_3 a & #_1 %_3 %_2 #_3 %_2 ~a)"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* 	#7 	*/
	@Test public void test07() {
		try {
			assertTrue(s_prover.provable("~(#_1 %_1 %_3 #_3 #_2 #_3 a & " + 
				"#_1 %_3 #_3 #_2 (~a | x) & #_1 ~x)"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* 	#8 	*/
	@Test public void test08() {
		try {
			assertTrue(s_prover.provable("#_1 (a > b) > ( #_1 a > #_1 b)"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* 	#9 	*/
	@Test public void test09() { 
		try {
			assertTrue(s_prover.provable("#_1 a > a"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #10	*/
	@Test public void test10() { 
		try {
			assertTrue(s_prover.provable("#_1 a > #_1 #_1 a"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #11	*/
	@Test public void test11() { 
		try {
			assertTrue(s_prover.provable("%_1 a > #_1 %_1 a"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #12	*/
	@Test public void test12() { 
		try {
			assertTrue(s_prover.provable("%_1 (#_1 p | %_2 #_2 #_1 q) > #_1 (~q > p)"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #13	*/
	@Test public void test13() { 
		try {
			assertTrue(s_prover.provable("((p & ~#_1 p) > ~p) | ((p & ~#_1 p) > ~((p & ~#_1 p) > ~#_1((p & ~#_1 p) > p )))"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #14	*/
	@Test public void test14() { 
		try {
			assertTrue(s_prover.provable("(#_1 (a | b) & (c > #_1 ~b) & (#_1 a > d)) > (c > d)"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #15	*/
	@Test public void test15() { 
		try {
			assertTrue(s_prover.provable("(#_1(g = #_1 p) & %_1 g) > (g & p)"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #16	*/
	@Test public void test16() { 
		try {
			assertTrue(s_prover.provable("(#_1(g = #_1 p) & %_1 ~g) > ~g"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #17	*/
	@Test public void test17() { 
		try {
			assertTrue(s_prover.provable("(#_1p & #_1((a & p) > b)) > #_1 (a > b)"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}
		
	/* #18  */
	@Test public void test18() { 
		try {
			assertFalse(s_prover.provable("(#_1 p & #_1 ((a & p) > b)) > (%_1 ~b & #_1 (a > b))"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #19  */
	@Test public void test19() { 
		try {
			assertFalse(s_prover.provable("#_1 %_1 %_3 #_3 #_2 #_3 a > b"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #20	*/
	@Test public void test20() { 
		try {
			assertFalse(s_prover.provable("~(#_1 (a | ~b) & %_1 ~a & %_1 b)"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #21	*/
	@Test public void test21() { 
		try {
			assertFalse(s_prover.provable("#_1 %_1 %_3 #_3 #_2 %_3 a > #_1 #_3 a"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #22	*/
	@Test public void test22() { 
		try {
			assertFalse(s_prover.provable("~(#_1 (a | b) & %_1 ~a & (%_1 (~a & ~b) | %_1 a))"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #23	*/
	@Test public void test23() { 
		try {
			assertFalse(s_prover.provable("~(#_1 %_1 %_3 #_3 #_2 #_3 a & #_1 %_3 #_3 #_2 (~a | x) & ~x)"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #24	*/
	@Test public void test24() { 
		try {
			assertFalse(s_prover.provable("((p & ~#_1 p) > p) & ((p & ~#_1 p) > ~((p & ~#_1 p) > #_1((p & ~#_1 p) > p )))"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}

	/* #25	*/
	@Test public void test25() { 
		try {
			assertFalse(s_prover.provable("~(#_1 %_3 (%_1 a | #_4 b) & #_3 (#_1 #_3 ~a & %_3 #_4 #_3 %_6 ~b))"));
		} catch (TableauErrorException e) {
			fail(e.toString());
		}
	}
}
