package ast;
import java.util.*;
/*
 * Krakatoa Class
 */
public class TypeCianetoClass {

   public TypeCianetoClass( String name ) {
      this.name = name;
   }
   
   public TypeCianetoClass( String name, String superclass ) {
	      this.name = name;
	      this.superclass = new TypeCianetoClass(superclass);
   }

   public String getCname() {
      return name;
   }

   public void addField(String qualifier, String name, Type typeVar) {
	   FieldDec f = new FieldDec(qualifier, name, typeVar);
	   fieldList.add(f);
   }
   
   private String name;
   private TypeCianetoClass superclass;
   private ArrayList<FieldDec> fieldList = new ArrayList<FieldDec>();
   // private MethodList publicMethodList, privateMethodList;
   // m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
   // entre outros m�todos
}
