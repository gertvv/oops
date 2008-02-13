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

import java.awt.LayoutManager;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Container;

import org.eclipse.mylyn.zest.layouts.algorithms.TreeLayoutAlgorithm;

import java.util.ArrayList;

public class TreeLayout implements LayoutManager {
	public void layoutContainer(Container parent) {
		int x = 0; int y = 0;
		ArrayList<LayoutComponent> entities = new ArrayList<LayoutComponent>();
		ArrayList<LayoutRelation> relations = new ArrayList<LayoutRelation>();
		for (java.awt.Component c : parent.getComponents()) {
			LayoutComponent entity = (LayoutComponent)c;
			entities.add(entity);
			relations.addAll(entity.getRelations());
		}

		TreeLayoutAlgorithm alg = new TreeLayoutAlgorithm();
		try {
			alg.applyLayout(
				entities.toArray(new LayoutComponent[0]),
				relations.toArray(new LayoutRelation[0]),
				0, 0, 250, 250, false, false);
			for (LayoutComponent c : entities) {
				c.setBounds((int)c.getXInLayout(), (int)c.getYInLayout(),
				(int)c.getWidthInLayout(), (int)c.getHeightInLayout());
				// FIXME
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(0,0);
	}

	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(250,250);
	}

	public void addLayoutComponent(String name, Component comp) {
	}

	public void removeLayoutComponent(Component comp) {
	}
}
