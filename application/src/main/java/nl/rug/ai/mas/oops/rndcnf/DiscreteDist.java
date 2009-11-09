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

import java.util.Random;
import java.util.List;

public class DiscreteDist {
	int[] d_dist;
	int d_sum;

	public DiscreteDist(List<Integer> dist) {
		d_dist = new int[dist.size()];
		d_sum = 0;

		int i = 0;
		for (Integer x : dist) {
			d_dist[i] = x.intValue();
			d_sum += d_dist[i];
			++i;
		}
	}

	public int generate(Random rnd) {
		int r = rnd.nextInt(d_sum);
		for (int i = 0; i < d_dist.length; ++i) {
			int x = d_dist[i];
			if (x > r)
				return i;
			r -= x;
		}
		return 0; // should not occur
	}
}
