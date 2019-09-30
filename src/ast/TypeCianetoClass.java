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
/*
 * Krakatoa Class
 */
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

   public void addField(FieldDec f) {
	   fieldList.add(f);
   }
   
   public void addMethodPublic(MethodDec method) {
	   publicMethodList.add(method);
   }
   
   public void addMethodPrivate(MethodDec method) {
	   privateMethodList.add(method);
   }
   
   private boolean open;
   private TypeCianetoClass superclass = null;
   private ArrayList<FieldDec> fieldList = new ArrayList<FieldDec>();
   private ArrayList<MethodDec> publicMethodList = new ArrayList<MethodDec>();
   private ArrayList<MethodDec> privateMethodList = new ArrayList<MethodDec>();
   // métodos públicos get e set para obter e iniciar as variáveis acima,
   // entre outros métodos
}
