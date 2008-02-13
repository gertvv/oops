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

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.util.ArrayList;

public class TreeTest {
	private Tree d_tree;

	public TreeTest() {
		d_tree = new Tree();
		JLabel root = new JLabel("root");
		LayoutComponent rootc = d_tree.addComponent(root, null);
		System.out.println(root.getPreferredSize());
		System.out.println(rootc.getPreferredSize());
		JLabel e1 = new JLabel("element1");
		LayoutComponent e1c = d_tree.addComponent(e1, rootc);
		JLabel e2 = new JLabel("element2");
		LayoutComponent e2c = d_tree.addComponent(e2, rootc);
		JLabel e11 = new JLabel("element1.1");
		LayoutComponent e11c = d_tree.addComponent(e11, e1c);
		JLabel e12 = new JLabel("element1.2");
		LayoutComponent e12c = d_tree.addComponent(e12, e1c);

		JFrame frame = new JFrame();
		JScrollPane panel = new JScrollPane(d_tree);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		root.revalidate();
		root.repaint();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		TreeTest t = new TreeTest();
	}
}
