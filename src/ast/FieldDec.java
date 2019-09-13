package ast;

public class FieldDec extends Member {
	public FieldDec(String qualifier, String id, Type type){
		this.type = type;
		this.id = id;
		this.qualifier = qualifier;
	}
	
	public void genC( PW pw ) {
		
	}
	public void genJava(PW pw) {
		
	}
	
	private String id;
	private String qualifier;
	private Type type;
}
