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
   
   public void genJava(PW pw) {
	    pw.println("");
		pw.printlnIdent("class " + this.getJavaname() + " ");
		if(superclass != null) {
			pw.println("extends" + this.superclass.getJavaname() + "{");
		}
		else {
			pw.println("{");
		}
		pw.add();
		for(int i = 0; i < this.fieldList.size(); i++) {
			this.fieldList.get(i).genJava(pw);
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
   
   private boolean open;
   private TypeCianetoClass superclass = null;
   private ArrayList<FieldDec> fieldList = new ArrayList<FieldDec>();
   private ArrayList<MethodDec> publicMethodList = new ArrayList<MethodDec>();
   private ArrayList<MethodDec> privateMethodList = new ArrayList<MethodDec>();
   // métodos públicos get e set para obter e iniciar as variáveis acima,
   // entre outros métodos
}
