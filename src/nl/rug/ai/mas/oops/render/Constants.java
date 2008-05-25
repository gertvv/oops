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

package nl.rug.ai.mas.oops.render;

/**
 * Unicode constants for important logical symbols.
 */
public class Constants {
	/**
	 * Conjunction.
	 */
	public static final char CONJ = '\u2227';
	/**
	 * Disjunction.
	 */
	public static final char DISJ = '\u2228';
	/**
	 * Negation.
	 */
	public static final char NEG = '\u00ac'; 
	/**
	 * Implication.
	 */
	public static final char IMPL = '\u2192';
	/**
	 * Bi-implication (equivalence).
	 */
	public static final char BIIM = '\u2194';
	/**
	 * True (top).
	 */
	public static final char TRUE = '\u22a4';
	/**
	 * False (bottom).
	 */
	public static final char FALSE = '\u22a5';
	/**
	 * 'For all'.
	 */
	public static final char FORALL = '\u2200';
	/**
	 * 'There exists'.
	 */
	public static final char EXISTS = '\u2203';
	/**
	 * Lozenge (modal possibility).
	 */
	public static final char LOZENGE = '\u25ca';
	/**
	 * Square (modal necessity).
	 */
	public static final char SQUARE = '\u25fb';
	/**
	 * Empty set.
	 */
	public static final char EMPTY = '\u2205';
	/**
	 * Alpha.
	 */
	public static final char ALPHA = '\u03b1';
	/**
	 * Left ceiling.
	 */
	public static final char LCEIL = '\u300c';
	/**
	 * Right ceiling.
	 */
	public static final char RCEIL = '\u300d';
	/**
	 * Top-left Quine symbol (corner).
	 */
	public static final char TLQUINE = '\u231c';
	/**
	 * Top-right Quine symbol (corner).
	 */
	public static final char TRQUINE = '\u231d';

	/**
	 * Do not allow instances of this class.
	 */
	private Constants() {
	}
}
