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

import nl.rug.ai.mas.oops.formula.*;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Collection;

import java.text.AttributedString;
import java.awt.Font;
import java.awt.font.TextAttribute;

/**
 * Visit a Formula in order to generate an AttributedString from it.
 */
public class FormulaAttrString implements FormulaVisitor {
	protected Stack<List<AttrChar>> d_stack;
	private Font d_normal;
	private Font d_subscript;

	public FormulaAttrString(Font font) {
		d_stack = new Stack<List<AttrChar>>();
		d_normal = font;

		HashMap<TextAttribute, Object> atmap =
			new HashMap<TextAttribute, Object>();
		atmap.put(TextAttribute.SUPERSCRIPT,
			TextAttribute.SUPERSCRIPT_SUB);
		Font d_subscript = font.deriveFont(atmap);
	}

	public AttributedString getAttributedString() {
		List<AttrChar> l = d_stack.pop();

		// construct string, find subscript ranges
		List<Range> subList = new ArrayList<Range>();
		int i = 0;
		String str = new String();
		boolean sub = false;
		int start = 0;
		for (AttrChar ac : l) {
			str += String.valueOf(ac.getCharacter());
			if (ac.getSubscript()) {
				if (!sub) {
					start = i;
				}
			} else {
				if (sub) {
					subList.add(new Range(start, i));
				}
			}
			sub = ac.getSubscript();
			++i;
		}

		AttributedString atstr = new AttributedString(str);
		atstr.addAttribute(TextAttribute.FONT, d_normal);
		for (Range r : subList) {
			atstr.addAttribute(TextAttribute.FONT, d_subscript,
				r.getBegin(), r.getEnd());
		}

		return atstr;
	}

	public void visitBiImplication(BiImplication f) {
		visitBinary(Constants.BIIM);
	}

	public void visitConjunction(Conjunction f) {
		visitBinary(Constants.CONJ);
	}

	public void visitDisjunction(Disjunction f) {
		visitBinary(Constants.DISJ);
	}

	public void visitImplication(Implication f) { 
		visitBinary(Constants.IMPL);
	}

	public void visitMultiBox(MultiBox f) { 
		visitMulti(Constants.SQUARE, f.getAgent());
	}

	public void visitMultiDiamond(MultiDiamond f) { 
		visitMulti(Constants.LOZENGE, f.getAgent());
	}

	public void visitNegation(Negation f) { 
		visitUnary(Constants.NEG);
	}

	public void visitProposition(Proposition f) {
		visitString(f.toString());
	}

	public void visitUniBox(UniBox f) { 
		visitUnary(Constants.SQUARE);
	}

	public void visitUniDiamond(UniDiamond f) {
		visitUnary(Constants.LOZENGE);
	}

	public void visitFormulaReference(FormulaReference f) {
		visitString(f.toString());
	}

	private void visitBinary(char op) {
		List<AttrChar> right = d_stack.pop();
		List<AttrChar> left = d_stack.pop();
		left.add(0, new AttrChar('('));
		left.add(new AttrChar(op));
		left.addAll(right);
		left.add(new AttrChar(')'));
		d_stack.push(left);
	}
	
	private void visitUnary(Collection<AttrChar> op) {
		List<AttrChar> right = d_stack.pop();
		right.addAll(0, op);
		d_stack.push(right);
	}

	private void visitUnary(char op) {
		List<AttrChar> l = new ArrayList<AttrChar>();
		l.add(new AttrChar(op));
		visitUnary(l);
	}

	private void visitMulti(char op, Agent a) {
		List<AttrChar> l = new ArrayList<AttrChar>();
		l.add(new AttrChar(op));
		l.addAll(codeAgent(a));
		visitUnary(l);
	}

	protected void visitString(String str) {
		visitString(str, false);
	}

	protected void visitString(String str, boolean sub) {
		d_stack.push(codeString(str, sub));
	}

	protected List<AttrChar> codeString(String str, boolean sub) {
		List<AttrChar> l = new ArrayList<AttrChar>();
		for (char c : str.toCharArray()) {
			l.add(new AttrChar(c, sub));
		}
		return l;
	}

	protected List<AttrChar> codeAgent(Agent a) {
		List<AttrChar> l = new ArrayList<AttrChar>();
		try {
			NullAgent na = (NullAgent) a;
			l.add(new AttrChar(Constants.EMPTY, true));
		} catch (ClassCastException e) {
			l = codeString(a.toString(), true);
		}
		return l;
	}

	protected class AttrChar {
		private char d_c;
		private boolean d_sub;

		public AttrChar(char c) {
			this(c, false);
		}

		public AttrChar(char c, boolean sub) {
			d_c = c;
			d_sub = sub;
		}

		public char getCharacter() {
			return d_c;
		}

		public boolean getSubscript() {
			return d_sub;
		}
	}

	private class Range {
		private int d_begin;
		private int d_end;
		
		public Range(int begin, int end) {
			d_begin = begin;
			d_end = end;
		}

		public int getBegin() {
			return d_begin;
		}

		public int getEnd() {
			return d_end;
		}
	}
}
