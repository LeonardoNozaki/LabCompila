/* ==========================================================================
 * Universidade Federal de Sao Carlos - Campus Sorocaba
 * Disciplina: Laboratorio de Compiladores
 * Prof. Jose Guimaraes
 *
 * Trabalho de Laboratorio de Compiladores - The Cianeto Language
 *
 * Aluno: Bruno Rizzi       RA: 743515
 * Aluno: Leonardo Nozaki   RA: 743561
 * ========================================================================== */

package ast;
import java.util.*;

public class TypeCianetoClass extends Type{
	
   public TypeCianetoClass( String name) {
      super(name);
   }
   
   public void setSuperClass(TypeCianetoClass superclass) {
	   this.superclass = superclass;
   }
   
   public TypeCianetoClass getSuper() {
	   return superclass;
   }
   
   public void setOpen(boolean open) {
	   this.open = open;
   }
   
   public boolean getOpen() {
	   return open;
   }
   
   public String getCname() {
      return getName();
   }
   
   public String getJavaname() {
      return getName();
   }
   
   public boolean searchType(String id) {
	   if(this.getName().equals(id)) {
		   return true;
	   }
	   else if(superclass != null) {
		   return this.superclass.searchType(id);
	   }
	   else {
		   return false;
	   }
   }

   public boolean addField(FieldDec f) {
	   int size = fieldList.size();
	   for(int i = 0; i < size ; i++){
		   if(fieldList.get(i).isEqualField(f)) {
			   return false;
		   }
	   }
	   size = publicMethodList.size();
	   for(int i = 0; i < size ; i++){
		   if(publicMethodList.get(i).isEqualField(f)) {
			   return false;
		   }
	   }
	   size = privateMethodList.size();
	   for(int i = 0; i < size ; i++){
		   if(privateMethodList.get(i).isEqualField(f)) {
			   return false;
		   }
	   }

	   fieldList.add(f);
	   return true;
   }
   
   public FieldDec getField(String idField) {
	   for(int i = 0; i < fieldList.size(); i++) {
		   if(idField.equals(this.fieldList.get(i).getName())) {
			   return this.fieldList.get(i);
		   }
	   }
	   
	   if(superclass != null) {
		   return superclass.getField(idField);
	   }
	   return null;
   }
   
   public MethodDec getMethod(String idMethod) {
	   for(int i = 0; i < publicMethodList.size(); i++) {
		   if(idMethod.equals(this.publicMethodList.get(i).getName())) {
			   return this.publicMethodList.get(i);
		   }
	   }
	   for(int i = 0; i < privateMethodList.size(); i++) {
		   if(idMethod.equals(this.privateMethodList.get(i).getName())) {
			   return this.privateMethodList.get(i);
		   }
	   }
	   if(superclass != null) {
		   return superclass.getMethod(idMethod);
	   }
	   return null;
   }
   
   public MethodDec getMethodPublic(String idMethod) {
	   for(int i = 0; i < publicMethodList.size(); i++) {
		   if(idMethod.equals(this.publicMethodList.get(i).getName())) {
			   return this.publicMethodList.get(i);
		   }
	   }
	   
	   if(superclass != null) {
		   return superclass.getMethod(idMethod);
	   }
	   return null;
   }
   
   public String getNameSuper(String idMethod) {
	   for(int i = 0; i < publicMethodList.size(); i++) {
		   if(idMethod.equals(this.publicMethodList.get(i).getName())) {
			   return this.getName();
		   }
	   }
	   
	   if(superclass != null) {
		   return superclass.getNameSuper(idMethod);
	   }
	   return "";
   }
   
   public boolean addMethodPublic(MethodDec method) {
	   int size = fieldList.size();
	   for(int i = 0; i < size ; i++){
		   if(fieldList.get(i).isEqualMethod(method)) {
			   return false;
		   }
	   }
	   size = publicMethodList.size();
	   for(int i = 0; i < size ; i++){
		   if(publicMethodList.get(i).isEqualMethod(method)) {
			   return false;
		   }
	   }
	   size = privateMethodList.size();
	   for(int i = 0; i < size ; i++){
		   if(privateMethodList.get(i).isEqualMethod(method)) {
			   return false;
		   }
	   }

	   publicMethodList.add(method);
	   return true;
   }
   
   public boolean addMethodPrivate(MethodDec method) {
	   int size = fieldList.size();
	   for(int i = 0; i < size ; i++){
		   if(fieldList.get(i).isEqualMethod(method)) {
			   return false;
		   }
	   }
	   size = publicMethodList.size();
	   for(int i = 0; i < size ; i++){
		   if(publicMethodList.get(i).isEqualMethod(method)) {
			   return false;
		   }
	   }
	   size = privateMethodList.size();
	   for(int i = 0; i < size ; i++){
		   if(privateMethodList.get(i).isEqualMethod(method)) {
			   return false;
		   }
	   }
	   
	   privateMethodList.add(method);
	   return true;
   }
   
	public void genC(PW pw) {
		String className = this.getCname();
		pw.printlnIdent("typedef");
		pw.add();
		pw.printlnIdent("struct" + " _St_" + className + " {");
		pw.add();
		pw.printlnIdent("Func *vt;");
		   
		this.genFieldC(pw);

		pw.sub();
		pw.printlnIdent("} _class_" + className + ";");
		pw.sub();
		pw.println("");
		
		pw.printlnIdent("_class_" + className + " *new_" + className + "(void);");
		pw.println("");
		
		for(int i = 0; i < this.privateMethodList.size(); i++) {
			this.privateMethodList.get(i).genC(pw, className);
			pw.println("");
		}
		for(int i = 0; i < this.publicMethodList.size(); i++) {
			this.publicMethodList.get(i).genC(pw, className);
			pw.println("");
		}
		
		this.genArrayMethodC(pw);
		pw.println("");
		
		pw.printlnIdent("_class_" + className + " *new_" + className + "() {");
		pw.add();
		pw.printlnIdent("_class_" + className + " *t;");
		pw.printlnIdent("if ( (t = malloc(sizeof(_class_" + className + "))) != NULL ){");
		pw.add();
		for(int i = 0; i < mallocString.size(); i++) {
			pw.printlnIdent("t->" + "_" + getCname() + "_" + mallocString.get(i) + " = (char*)malloc(sizeof(char)*1000);");	
		}
		pw.printlnIdent("t->vt = VTclass_" + className + ";");
		pw.sub();
		pw.printlnIdent("}");
		pw.printlnIdent("return t;");
		pw.sub();
		pw.printlnIdent("}");
		
	}
   
	public void genFieldC(PW pw) {
		if(superclass != null) {
			superclass.genFieldC(pw);
		}
	  
		for(int i = 0; i < this.fieldList.size(); i++) {
			if(this.fieldList.get(i).getType() instanceof TypeCianetoClass) {
				pw.printIdent("_class_" + fieldList.get(i).getType().getCname() + " *");
				pw.println("_" + getCname() + "_" + fieldList.get(i).getName() + ";" );
			}
			else if(this.fieldList.get(i).getType() instanceof TypeString) {
				mallocString.add(fieldList.get(i).getName());
				pw.printIdent(fieldList.get(i).getType().getCname() + " *");
				pw.println("_" + getCname() + "_" + fieldList.get(i).getName() + ";" );
			}
			else {
				pw.printIdent(fieldList.get(i).getType().getCname() + " ");
				pw.println("_" + getCname() + "_" + fieldList.get(i).getName() + ";" );
			}
		}
	}
	
	public void genArrayMethodC(PW pw) {
		String className = this.getCname();
		pw.printlnIdent("Func VTclass_" + className + "[] = {");
		pw.add();
		this.createGlobalNames();
		if(globalNames.size() > 0) {
			pw.printIdent("( void (*)() )" + globalNames.get(0));
			for(int i = 1; i < globalNames.size(); i++) {
				pw.println(",");
				pw.printIdent("( void (*)() ) " + globalNames.get(i));
			}
		}
		pw.sub();
		pw.println("");
		pw.printlnIdent("};");
	}
	
	public void genJava(PW pw) {
	    pw.println("");
	    if(open) {
	    	pw.printIdent("");
	    }
	    else {
	    	pw.printIdent("final ");
	    }
	    pw.print("class " + this.getJavaname() + " ");
		if(superclass != null) {
			pw.println("extends " + this.superclass.getJavaname() + "{");
		}
		else {
			pw.println("{");
		}
		pw.add();
		pw.printlnIdent("private Scanner scanner = new Scanner(System.in);");
		for(int i = 0; i < this.fieldList.size(); i++) {
			//this.fieldList.get(i).genJava(pw);
			if(fieldList.get(i).getQuali().isVoid()) {
				pw.printIdent("private");
			}
			else {
				fieldList.get(i).getQuali().genJava(pw);
			}
			pw.print(" " + fieldList.get(i).getType().getJavaname() + " ");
			pw.println(fieldList.get(i).getName() + ";" );
		}
		for(int i = 0; i < this.publicMethodList.size(); i++) {
			this.publicMethodList.get(i).genJava(pw);
		}
		for(int i = 0; i < this.privateMethodList.size(); i++) {
			this.privateMethodList.get(i).genJava(pw);
		}
		pw.sub();
		pw.printlnIdent("}");
   }
   
	public boolean sameSignature(MethodDec md) {
	   MethodDec supermd = this.getMethodPublic(md.getName());
	   if(supermd != null) {
		   if(supermd.getName().equals(md.getName()) && supermd.getType() == md.getType() && supermd.compareParOverrite(md.getParamDec())) {
			   if(supermd.getQuali().hasFinal() || supermd.getQuali().isPrivate()) {
				   return false;
			   }
			   else {
				   return true;
			   }
		   }
		   else {
			   return false;
		   }
	   }
	   else if (this.superclass != null){
		   return this.superclass.sameSignature(md);
	   }
	   else {
		   return false;
	   }
   }
   
	public ArrayList<String> getGlobalNames(){
	   return this.globalNames;
	}
	
	private void createGlobalNames() {
	   if(superclass != null) {
		   globalNames = new ArrayList<String>(superclass.getGlobalNames());
		   createMethodNames();
	   }
	   else {
		   globalNames = new ArrayList<String>();
		   methodNames = new ArrayList<String>();
	   }
	   
	   int size = globalNames.size();
	   String name;
	   boolean flag = false;
	   
	   for(int i = 0; i < publicMethodList.size(); i++) {
		   name = publicMethodList.get(i).getName();
		   if(name.endsWith(":")) {
			   name = name.substring(0, name.length()-1);
			   name = name + "$";
		   }
		   flag = false;
		   for(int j = 0; j < size; j++) {
			   if(methodNames.get(j).equals(name)) {
				   globalNames.set(j, "_" + this.getCname() + "_" + name);
				   j = size;
				   flag = true;
			   }
		   }
		   if(flag == false) {
			   globalNames.add("_" + this.getCname() + "_" + name);
			   methodNames.add(name);
		   }
	   }
	}
	
	private void createMethodNames() {
		methodNames = new ArrayList<String>();
		int size = globalNames.size();
		String aux;
		int qnt = 0, j = 0;
		for(int i = 0; i < size; i++) {
			aux = globalNames.get(i);
			qnt = 0;
			j = 0;
			while(qnt < 2) {
				if(aux.charAt(j) == '_') {
					qnt++;
				}
				j++;
			}
			methodNames.add(aux.substring(j)); //Substring comeca em j e pega ate o fim
		}
	}
	
	public int findMethod(String name) {
		int size = methodNames.size();
		if(name.endsWith(":")) {
			name = name.substring(0, name.length()-1);
			name = name + "$";
		}
		for(int j = 0; j < size; j++) {
		   if(methodNames.get(j).equals(name)) {
			   return j;
		   }
		}
		return -1;
	}
	
    private boolean open;
    private TypeCianetoClass superclass = null;
    private ArrayList<FieldDec> fieldList = new ArrayList<FieldDec>();
    private ArrayList<MethodDec> publicMethodList = new ArrayList<MethodDec>();
    private ArrayList<MethodDec> privateMethodList = new ArrayList<MethodDec>();
    private ArrayList<String> mallocString = new ArrayList<String>();
    private ArrayList<String> globalNames;
    private ArrayList<String> methodNames;
    // métodos públicos get e set para obter e iniciar as variáveis acima,
    // entre outros métodos
}
