package ast;

public class TypeId extends Type{
	
	public TypeId(String tipo) {
		super(tipo);
	}
	
	@Override
	   public String getCname() {
	      return "int";
	   }
}
