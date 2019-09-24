package ast;

public class LiteralString extends Expr {
    
    public LiteralString( String literalString ) { 
        this.literalString = literalString;
    }
    
    @Override
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print(literalString);
    }

    @Override
    public Type getType() {
        return Type.stringType;
    }
    
    private String literalString;
}

