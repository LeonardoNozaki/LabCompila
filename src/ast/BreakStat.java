package ast;

public class BreakStat extends Statement{
   public void genC( PW pw) {
	   pw.printIdent("break;");
   }
	   
}
