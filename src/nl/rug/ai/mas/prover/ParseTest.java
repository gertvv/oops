package nl.rug.ai.mas.prover;

import nl.rug.ai.mas.prover.parser.node.*;
import nl.rug.ai.mas.prover.parser.analysis.*;
import nl.rug.ai.mas.prover.parser.lexer.*;
import nl.rug.ai.mas.prover.parser.parser.*;

import java.io.*;

public class ParseTest extends DepthFirstAdapter {
	public static void main (String [] args) {
		try {
			// create lexer
			Lexer lexer = new Lexer(new PushbackReader(new BufferedReader(
							new FileReader(args[0])), 1024));

			// parser program
			Parser parser = new Parser(lexer);

			Start ast = parser.parse();

			// check program semantics
			ast.apply(new ParseTest());
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("");
	}

	public void inAConJunction(AConJunction node) {
		System.out.print("&");
	}

	public void inADisJunction(ADisJunction node) {
		System.out.print("|");
	}

	public void inAImpJunction(AImpJunction node) {
		System.out.print(">");
	}

	public void inAEqvJunction(AEqvJunction node) {
		System.out.print("=");
	}

	/*
	public void inABaseConjunction(ABaseConjunction node) {
		System.out.print("&");
	}
	*/

	public void inAPropositionSimpleFormula(APropositionSimpleFormula node) {
		System.out.print(node.getProp().getText());
	}

	public void inAVariableSimpleFormula(AVariableSimpleFormula node) {
		System.out.print("!" + node.getVar().getText());
	}

	public void inANegationSimpleFormula(ANegationSimpleFormula node) {
		System.out.print("~");
	}

	public void outASingleBox(ASingleBox node) {
		System.out.print("#");
	}
	public void outAMultiBox(AMultiBox node) {
		System.out.print("#" + node.getId().getText());
	}

	public void outASingleDia(ASingleDia node) {
		System.out.print("%");
	}
	public void outAMultiDia(AMultiDia node) {
		System.out.print("%" + node.getId().getText());
	}
	public void inAEnclosedFormulaList(AEnclosedFormulaList node) {
		System.out.println("{");
	}
	public void outABaseFormulaListNonempty(ABaseFormulaListNonempty node) {
		System.out.println(",");
	}
	public void outAIndFormulaListNonempty(AIndFormulaListNonempty node) {
		System.out.println(",");
	}
	public void outAEnclosedFormulaList(AEnclosedFormulaList node) {
		System.out.print("}");
	}
	public void inAAssignment(AAssignment node) {
		System.out.print(node.getVar().getText() + " : ");
	}
}
