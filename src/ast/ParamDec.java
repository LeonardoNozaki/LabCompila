package ast;

public class ParamDec {
	public ParamDec(String id, Type type){
		this.type = type;
		this.id = id;
	}
	
	public void genC( PW pw ) {
		
	}
	public void genJava(PW pw) {
		
	}
	
	private String id;
	private Type type;
}
