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

import nl.rug.ai.mas.oops.tableau.*;
import nl.rug.ai.mas.oops.formula.Agent;
import nl.rug.ai.mas.oops.formula.NullAgent;
import nl.rug.ai.mas.oops.formula.Formula;

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
 * Visit a Label in order to generate an AttributedString from it.
 * @see java.text.AttributedString
 */
public class LabelAttrString extends FormulaAttrString implements LabelVisitor {
	/**
	 * Constructor.
	 * @param font The font this formula should be rendered in.
	 */
	public LabelAttrString(Font font) {
		super(font);
	}

	public void visitLabelInstance(LabelInstance l) {
		List<AttrChar> parent = new ArrayList<AttrChar>();
		if (!d_stack.empty()) {
			parent = d_stack.pop();
		}

		World w = l.getWorld();

		parent.add(new AttrChar('.'));
		parent.addAll(codeWorld(l.getWorld()));
		parent.addAll(codeAgent(l.getAgent()));
	}

	public void visitLabelReference(LabelReference l) {
		visitString(l.toString());
	}

	public void visitNullLabel(NullLabel l) {
		visitString(String.valueOf(Constants.EMPTY));
	}

	private List<AttrChar> codeWorld(World w) {
		List<AttrChar> world = null;
		try {
			WorldInstance wi = (WorldInstance)w;
			Formula f = wi.getFormula();
			if (f == null) {
				world = codeString(String.valueOf(Constants.ALPHA), false);
			} else {
				f.accept(this);
				world = d_stack.pop();
				world.add(0, new AttrChar(Constants.TLQUINE));
				world.add(new AttrChar(Constants.TRQUINE));
			}
		} catch (ClassCastException e) {
			world = codeString(w.toString(), false);
		}
		return world;
	}
}
