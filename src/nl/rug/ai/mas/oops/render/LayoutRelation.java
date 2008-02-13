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

import org.eclipse.mylyn.zest.layouts.LayoutRelationship;
import org.eclipse.mylyn.zest.layouts.LayoutEntity;
import org.eclipse.mylyn.zest.layouts.LayoutBendPoint;
import org.eclipse.mylyn.zest.layouts.constraints.LayoutConstraint;

public class LayoutRelation implements LayoutRelationship {
	private LayoutEntity d_source;
	private LayoutEntity d_destination;
	private Object d_layoutInformation;

	public LayoutRelation(LayoutEntity source, LayoutEntity dest) {
		d_source = source;
		d_destination = dest;
		d_layoutInformation = null;
	}

	public LayoutEntity getSourceInLayout() {
		return d_source;
	}

	public LayoutEntity getDestinationInLayout() {
		return d_destination;
	}

	public Object getLayoutInformation() {
		return d_layoutInformation;
	}

	public void setLayoutInformation(Object l) {
		d_layoutInformation = l;
	}

	public void populateLayoutConstraint(LayoutConstraint c) {
	}

	public void clearBendPoints() {
	}

	public void setBendPoints(LayoutBendPoint[] b) {
	}
}
