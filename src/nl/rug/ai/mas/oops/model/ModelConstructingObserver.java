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

package nl.rug.ai.mas.oops.model;

import java.util.Set;
import java.util.HashMap;

import nl.rug.ai.mas.oops.tableau.Tableau;
import nl.rug.ai.mas.oops.tableau.TableauObserver;
import nl.rug.ai.mas.oops.tableau.TableauEvent;
import nl.rug.ai.mas.oops.tableau.BranchOpenEvent;
import nl.rug.ai.mas.oops.tableau.Branch;
import nl.rug.ai.mas.oops.tableau.Label;
import nl.rug.ai.mas.oops.tableau.LabelInstance;
import nl.rug.ai.mas.oops.formula.Formula;
import nl.rug.ai.mas.oops.formula.AgentId;
import nl.rug.ai.mas.oops.formula.Proposition;
import nl.rug.ai.mas.oops.formula.Negation;

public class ModelConstructingObserver implements TableauObserver {
	private KripkeModel d_model;

	public ModelConstructingObserver(KripkeModel m) {
		d_model = m;
	}

	public void update(Tableau t, TableauEvent e) {
		if (e instanceof BranchOpenEvent) {
			BranchOpenEvent event = (BranchOpenEvent)e;
			Branch branch = event.getBranch();
			
			Set<Label> labels = branch.getLabels();
			System.out.println("Labels: " + labels);
			HashMap<Label, World> labelMap = new HashMap<Label, World>();

			// create a world for each label
			int i = 0;
			for (Label l : labels) {
				String n = Character.toString((char)('A' + i));
				Valuation v = new Valuation();
				for (Formula f : branch.getFormulas(l)) {
					if (f.isSimple()) {
						if (f instanceof Proposition) {
							v.setValue((Proposition)f, true);
						} else {
							Formula g = ((Negation)f).getRight();
							v.setValue((Proposition)g, false);
						}
					}
				}
				World w = new World(n, v);
				labelMap.put(l, w);
				d_model.addWorld(w);
				++i;
			}

			// add relations
			for (Label label : labels) {
				LabelInstance l = (LabelInstance)label;
				Label p = l.getParent();
				World w1 = labelMap.get(p);
				if (w1 == null) {
					continue;
				}
				World w2 = labelMap.get(l);
				d_model.addArrow((AgentId)l.getAgent(), w1, w2);
			}
		}
	}
}
