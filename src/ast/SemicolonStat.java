package ast;

public class SemicolonStat extends Statement{
   public void genC( PW pw) {
	   pw.printIdent(";");
   }
}
