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
           generateGlobals((SimpleNode) current_node.jjtGetChild(i));
       }

        if(current_node.getId() == ProgramTreeConstants.JJTVAR){
			//TODO PRINT STATIC DIFFERENTIATION
		   output.print(".field public static " + current_node.getName() + " ");
		   if(current_node.getType() == "int")
				output.println("I");
			else 
			if(current_node.getType() == "boolean")
				output.println("B");
			else 
			if(current_node.getType() == "String")
				output.println("[Ljava/lang/String;");
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
		/*else if (function_node.jjtGetNumChildren() >= 2
				&& ((SimpleNode) function_node.jjtGetChild(1)).getId() == ProgramTreeConstants.JJTASSIGN)
			generateAssignFunction(function_node);
		*/
		else{
			generateFunctionHeader(function_node);
		}
		
		//body stub
		//declarations, etc etc
		if (function_node.jjtGetNumChildren() >= 2){
			if(function_node.getReturnType() == Symbol.Type.INT)
				output.println("ireturn");
			else output.println("areturn");
		}

		else
			output.println("return");
		
		output.println(".end method");
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


     
    }