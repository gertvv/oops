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

import org.eclipse.mylyn.zest.layouts.LayoutEntity;
import org.eclipse.mylyn.zest.layouts.constraints.LayoutConstraint;
import org.eclipse.mylyn.zest.layouts.constraints.BasicEntityConstraint;

import java.awt.Dimension;
import javax.swing.JComponent;

import java.util.List;
import java.util.ArrayList;

public class LayoutComponent extends JComponent implements LayoutEntity {
	private JComponent d_component;
	private double d_x;
	private double d_y;
	private double d_width;
	private double d_height;
	private Object d_layoutInformation;
	private ArrayList<LayoutRelation> d_relations;

	public LayoutComponent(JComponent c) {
		super();
		// use even simpler layout? only to proxy preferred/minimum size etc.
		setLayout(new java.awt.BorderLayout());
		add(c);
		d_component = c;
		d_relations = new ArrayList<LayoutRelation>();
		d_x = 0; d_y = 0; d_width = 0; d_height = 0;
	}

	public JComponent getJComponent() {
		return d_component;
	}

	public int compareTo(Object o) {
		return 0;
	}

	public void addRelation(LayoutComponent c) {
		LayoutRelation r = new LayoutRelation(this, c);
		d_relations.add(r);
	}

	public List<LayoutRelation> getRelations() {
		return d_relations;
	}

	public int getRelationsCount() {
		return d_relations.size();
	}

	public Object getLayoutInformation() {
		return d_layoutInformation;
	}

	public double getWidthInLayout() {
		return d_width;
	}

	public double getHeightInLayout() {
		return d_height;
	}

	public double getXInLayout() {
		return d_x;
	}

	public double getYInLayout() {
		return d_y;
	}

	public void populateLayoutConstraint(LayoutConstraint constraint) {
		// useful to implement BasicEntityConstraint?
		if (constraint instanceof BasicEntityConstraint) {
			BasicEntityConstraint c = (BasicEntityConstraint)constraint;
			Dimension d = getPreferredSize();
			c.hasPreferredLocation = false;
			c.hasPreferredSize = true;
			c.preferredWidth = d.getWidth();
			c.preferredHeight = d.getHeight();
		}
	}

	public void setLayoutInformation(Object internalEntity) {
		d_layoutInformation = internalEntity;
	}

	public void setLocationInLayout(double x, double y) {
		d_x = x;
		d_y = y;
	}

	public void setSizeInLayout(double width, double height) {
		d_width = width;
		d_height = height;
	}
}
