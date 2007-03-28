package nl.rug.ai.mas.prover;

import java.lang.*;
import java.io.*;
import java.util.*;

import nl.rug.ai.mas.prover.parser.parser.*;
import nl.rug.ai.mas.prover.parser.lexer.*;
import nl.rug.ai.mas.prover.parser.node.*;

public class Main {
  public static void main (String[] argv)
  {
    try {
      Lexer l = new Lexer (new PushbackReader (new BufferedReader(new InputStreamReader (System.in))));
      Parser p = new Parser (l);
      Start start = p.parse ();
//      System.out.println (start.toString());

      ASTDisplay ad = new ASTDisplay ();
      start.apply (ad);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
};

