package src.codegeneration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


import src.AST.SimpleNode;
import src.AST.Program;
import src.AST.ProgramTreeConstants;

import src.semantic.Symbol;
import src.semantic.Symbol.Type;


 public class CodeGenerator {

 	private SimpleNode root;

 	private String file_name;

 	private PrintWriter output;


 	public CodeGenerator(SimpleNode root, String file_name) throws IOException{
        this.root = (SimpleNode) root.getChildren()[0];
		this.file_name = file_name;
		
		FileWriter filewriter = new FileWriter(file_name, false);
        BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
        
	    this.output = new PrintWriter(bufferedwriter);

 	}

 	public void generateCode(){

 		generateClassHeader();
		generateGlobals(root);
		generateStatic();
		generateFunctions();
		output.close();

 	}

 	private void generateClassHeader() {
		output.println(".source " + this.file_name);
 		output.println(".class public " + root.getName());
        output.println( ".super java/lang/Object"  + "\n");
        
     }

     private void generateGlobals(SimpleNode current_node) {

        for(int i=0; i< current_node.jjtGetNumChildren(); i++)
       {
        SimpleNode child = (SimpleNode) current_node.jjtGetChild(i);

        if(child.getId() == ProgramTreeConstants.JJTVAR){
			//TODO PRINT STATIC DIFFERENTIATION
		   output.print(".field public static " + child.getName() + " ");
		   if(child.getType() == "int")
				output.println("I");
			else 
			if(child.getType() == "boolean")
				output.println("B");
			else 
			if(child.getType() == "String")
				output.println("[Ljava/lang/String;");
		}
	}
	}

 	private void generateStatic() {
		// TODO Auto-generated method stub


 	}

 	private void generateFunctions() {
		 
		for (int i = 0; i < root.jjtGetNumChildren(); i++) {
			SimpleNode child_root = (SimpleNode) root.jjtGetChild(i);
			if (child_root.getId() == ProgramTreeConstants.JJTMAIN) {
				generateFunction(child_root);
			}
			if (child_root.getId() == ProgramTreeConstants.JJTMETHOD) {
				generateFunction(child_root);
			}
			
		}
		
	 }
	 

	 private void generateFunction(SimpleNode function_node) {
		output.println();

		if (function_node.getId() == ProgramTreeConstants.JJTMAIN)
			generateFunctionMainHeader(function_node);
		else{
			generateFunctionHeader(function_node);
		}
		
		//body stub
		generateBodyStub(function_node);

		//declarations, etc etc
		if (function_node.jjtGetNumChildren() >= 2){
			if(function_node.getReturnType() == Symbol.Type.INT)
				output.println("\t" + "ireturn");
			else output.println("\t" + "areturn");
		}

		else
			output.println("\t" + "return");
		
		output.println(".end method");
		output.println();

	}

	private void generateBodyStub(SimpleNode function_node) {
		for (int i = 0; i < function_node.jjtGetNumChildren(); i++) {
			SimpleNode function_child = (SimpleNode) function_node.jjtGetChild(i);
			
			switch (function_child.getId()) {
				case ProgramTreeConstants.JJTTERM:
					
					output.print("\t" + "invokestatic " + function_child.getNodeValue() + "(");
					if ((function_child.getType() == "int"))
						output.print("I");

					if ((i + 1 == function_child.jjtGetNumChildren())){
						output.print(";");
						continue;
					}
					
				output.print(")");
				
				if (function_node.getType() == "int")
					output.println("I");
				else
					output.println("V");
				break;

				case ProgramTreeConstants.JJTPERIOD:
					generateCall(function_child);
					break;
				case ProgramTreeConstants.JJTASSIGN:
					generateAssign(function_child);
					break;
				default:
					break;
			}
			
		}
	}

	private void generateCall(SimpleNode function_child) {

		SimpleNode call_method_name = (SimpleNode) function_child.jjtGetChild(0);
		output.print("\t" + "invokestatic " + call_method_name.getNodeValue() + "(");


		for (int i = 1; i < function_child.jjtGetNumChildren(); i++) {
			SimpleNode argument = (SimpleNode) function_child.jjtGetChild(i);

			if ((argument.getType() == "int"))
				output.print("I");
			else
				output.print("V");

			if ((i + 1 == argument.jjtGetNumChildren())){
				output.print(";");
			} else break;
			
			continue;
		}

		output.print(")");

		if (((SimpleNode) function_child.jjtGetParent()).getId() == ProgramTreeConstants.JJTTERM)
			output.print("I");
		else
			output.print("V");

		output.println();
		output.println();
	}

	private void generateFunctionHeader(SimpleNode function_node){
		output.print(".method public static " + function_node.getName());
		
		if (function_node.jjtGetNumChildren() == 0)
			output.println("()V");
		else {
			output.print("(");
			for (int i = 0; i < function_node.jjtGetNumChildren(); i++){
				SimpleNode childFunction = (SimpleNode) function_node.jjtGetChild(i);
				if (childFunction.getId() == ProgramTreeConstants.JJTARGUMENT) {
					if(childFunction.getType() == "int")
						output.print("I");
					else 
					if(childFunction.getType() == "boolean")
						output.print("B");
					else 
					if(childFunction.getType() == "String")
						output.print("[Ljava/lang/String;");
					else output.println(")V");
				continue;
				}
				if(function_node.getType() == "int")
				output.println(")I");
				else output.println(")V");
				break;
			}
		}
	}

	private void generateFunctionMainHeader(SimpleNode function_node) {
		output.println(".method public static main([Ljava/lang/String;)V");

	}


	private void generateAssign(SimpleNode node) {
		
		SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			
			SimpleNode rhs = (SimpleNode) node.jjtGetChild(1);

			for(int j = 0; j < rhs.jjtGetNumChildren(); j++){
				if(rhs.jjtGetChild(i).getId() == ProgramTreeConstants.JJTTERM){
					//VERIFICAR SE É O JJTERM o certo	
					//TODO: Dar load das variáveis na expressão aritmética
					//output.println("iload " + rhs.jjtGetChild(i).getName());
					//TODO: Dar load dos valores na expressão aritmética caso não sejam variáveis
					//output.println("iconst ACHO eu);

				}else
				{

				switch(rhs.jjtGetChild(i).getId()) {
				case ProgramTreeConstants.JJTADD:
					output.println("\tiadd");
					break;
				case ProgramTreeConstants.JJTSUB:
					output.println("\tisub");
					break;
				case ProgramTreeConstants.JJTMUL:
					output.println("\timul");
					break;
				case ProgramTreeConstants.JJTDIV:
					output.println("\tidiv");
					break;
				}

			}
			}
			
		}
		//TODO: right now always assuming ArrayAccess and ScalarAccess are from static fields
		output.println("\tputstatic " + lhs.getNodeValue() + " I");

	}
     
    }