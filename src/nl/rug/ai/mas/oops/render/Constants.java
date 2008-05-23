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

public class Constants {
	public static final char CONJ = '\u2227';
	public static final char DISJ = '\u2228';
	public static final char NEG = '\u00ac'; 
	public static final char IMPL = '\u2192';
	public static final char BIIM = '\u2194';
	public static final char TRUE = '\u22a4';
	public static final char FALSE = '\u22a5';
	public static final char FORALL = '\u2200';
	public static final char EXISTS = '\u2203';
	public static final char LOZENGE = '\u25ca';
	public static final char SQUARE = '\u25fb';
	public static final char EMPTY = '\u2205';
	public static final char ALPHA = '\u03b1';
	public static final char LCEIL = '\u300c';
	public static final char RCEIL = '\u300d';
	public static final char TLQUINE = '\u231c';
	public static final char TRQUINE = '\u231d';

	private Constants() {
	}
}
