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

package nl.rug.ai.mas.oops.rndcnf;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class PropRateDist {
	private List<List<DiscreteDist>> d_dist;

	public PropRateDist(String dist) {
		this(ListParser.parseList3(dist));
	}

	public PropRateDist(List<List<List<Integer>>> dist) {
		d_dist = new ArrayList<List<DiscreteDist>>();
		for (List<List<Integer>> l : dist) {
			List<DiscreteDist> d = new ArrayList<DiscreteDist>();
			for (List<Integer> l1 : l) {
				d.add(new DiscreteDist(l1));
			}
			d_dist.add(d);
		}
	}

	/**
	 * @param rnd Random number generator
	 * @param d Depth
	 * @param k Clause length
	 */
	public int rndPropNum(Random rnd, int d, int k) {
		List<DiscreteDist> dist = d_dist.get(Math.min(d, d_dist.size() - 1));
		DiscreteDist ddist = dist.get(k - 1); // assume it exists
		return ddist.generate(rnd);
	}
}

