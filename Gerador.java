import java.io.*;
import java.util.Stack;

public class Gerador {
	
	String str1;
	String str2;
	String str3;

	public static void gravarArquivo(String cod) {
		  try{
			  FileWriter fstream = new FileWriter("codigo_gerado.txt",true);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(cod);
			  out.close();
		  }catch (Exception e){
			 System.err.println("Erro ao gravar arquivo: " +
		      e.getMessage());
		  }
	}
}