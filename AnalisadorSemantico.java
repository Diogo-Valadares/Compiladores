import java.util.*;

public class AnalisadorSemantico {

	private Hashtable<Object, Identificador> identificadores = new Hashtable<>();
    private java_cup.runtime.Symbol currToken;

    //insere um identificador na tabela 
	public String inserirSimbolo(Object variavel, TipoVariavel tipo, Object left, Object right) {
        if(identificadores.contains(variavel)){
            System.out.println("\n\nErro na linha "+left.toString()+" coluna "+right.toString()+": Variável \"" + variavel + "\" já foi previamente declarada");
            throw new IllegalArgumentException("Erro", null);
            //return "ERRO SEMÂNTICO";
        }        
		identificadores.put(variavel.toString(), new Identificador(tipo));
        return tipo.toString().toLowerCase() + " " + variavel + ";";
	}

    //obtem um identificador da tabela
    public Identificador obterIdentificador(Object variavel, Object left, Object right) {
        Identificador identificador = identificadores.get(variavel);

		if (identificador == null) {
			System.out.println(String.format("\n\nErro na linha "+left.toString()+" coluna "+right.toString()+": Variável \"" + variavel + "\" precisa ser declarada antes de ser usada."));
			throw new IllegalArgumentException("Erro", null);
            //return null;
		} 
        return identificador;
	}
	
    //tenta atribuir uma variavel, se der errado retorna "ERRO SEMANTICO"
    //se der certo retorna a atribuição
	public String atribuirVariavel(Object variavel, Expressao valor, Object left, Object right) {
        TipoVariavel tipo1;
        TipoVariavel tipo2;
        if(!identificadores.containsKey(variavel)) {
            System.out.println("\n\nErro na linha "+left.toString()+" coluna "+right.toString()+": Variável \"" + variavel + "\" precisa ser declarada antes de ser usada.");
            throw new IllegalArgumentException("Erro", null);
            //return null;
        }      
        tipo1 = identificadores.get(variavel).tipo;
              
		if(valor.ehVariavel){
            if(!identificadores.containsKey(valor.resultado)){
                System.out.println("\n\nErro na linha "+left.toString()+" coluna "+right.toString()+": Variável \"" + valor.resultado + "\" precisa ser declarada antes de ser usada.");
                throw new IllegalArgumentException("Erro", null);
                //return null;
		    }            
            if(!identificadores.get(valor.resultado).estaAtribuido){
                System.out.println (String.format("\n\nErro na linha "+left.toString()+" coluna "+right.toString()+": Variável \"" + valor.resultado + "\" precisa ser atribuida antes de ser usada."));
                throw new IllegalArgumentException("Erro", null);
                //return null;
            }
            tipo2 = identificadores.get(valor.resultado).tipo;
        }
        else{
            tipo2 = valor.tipoResultado;
        }

        if(tipo1 != tipo2 && (tipo1 == TipoVariavel.BOOL || tipo1 == TipoVariavel.CHAR || tipo2 == TipoVariavel.BOOL || tipo2 == TipoVariavel.CHAR)){
            switch(tipo1){  
                case BOOL:
                    System.out.println("\n\nErro na linha "+left.toString()+" coluna "+right.toString()+": O tipo boolean da variável \"" + variavel + "\" só pode receber \"true\" ou \"false\" na atribuição");
                    throw new IllegalArgumentException("Erro", null);
                    //break;
                case INT:
                    System.out.println(String.format("\n\nErro na linha "+left.toString()+" coluna "+right.toString()+": O tipo inteiro da variável \"" + variavel + "\" só pode receber números inteiros na atribuição"));
                    throw new IllegalArgumentException("Erro", null);
                    //break;
                case FLOAT:
                    System.out.println(String.format("\n\nErro na linha "+left.toString()+" coluna "+right.toString()+": O tipo float da variável \"" + variavel + "\" só pode receber números decimais na atribuição"));
                    throw new IllegalArgumentException("Erro", null);
                    //break;
                case CHAR:
                    System.out.println(String.format("\n\nErro na linha "+left.toString()+" coluna "+right.toString()+": O tipo char da variável \"" + variavel + "\" só pode receber um caractere por vez"));
                    throw new IllegalArgumentException("Erro", null);
                    //break;
            } 
        }        

        identificadores.get(variavel).estaAtribuido = true;

		return variavel.toString() + "=" + valor.resultado + ";";
	}

    //testa uma expressão, se não for valida vai imprimir o erro e retornar nulo
    //se for valida cria uma expressão nova combinando os duas expressões que foram comparadas
    //e definindo como tipo numerico se for uma expressão +-*/ e tipo bool para qualquer outro operador
	public Expressao testarExpressao(Expressao var1, Expressao var2, String operador, Object left1, Object right1, Object left2, Object right2) {
        TipoVariavel tipo1;
        TipoVariavel tipo2;
        if(var1.ehVariavel){
            if(!identificadores.containsKey(var1.resultado)) {
                System.out.println("\n\nErro na linha "+left1.toString()+" coluna "+right1.toString()+": Variável \"" + var1.resultado + "\" precisa ser declarada antes de ser usada.");
                throw new IllegalArgumentException("Erro", null);
                //return null;
		    }            
            if(!identificadores.get(var1.resultado).estaAtribuido){
                System.out.println(String.format("\n\nErro na linha "+left1.toString()+" coluna "+right1.toString()+": Variável \"" + var1.resultado + "\" precisa ser atribuida antes de ser usada."));
                throw new IllegalArgumentException("Erro", null);
                //return null;
            }
            tipo1 = identificadores.get(var1.resultado).tipo;
        }
        else{
            tipo1 = var1.tipoResultado;
        }
		if(var2.ehVariavel){
            if(!identificadores.containsKey(var2.resultado)){
                System.out.println("\n\nErro na linha "+left2.toString()+" coluna "+right2.toString()+": Variável \"" + var2.resultado + "\" precisa ser declarada antes de ser usada.");
                throw new IllegalArgumentException("Erro", null);
                //return null;
		    }            
            if(!identificadores.get(var2.resultado).estaAtribuido){
                System.out.println(String.format("\n\nErro na linha "+left2.toString()+" coluna "+right2.toString()+": Variável \"" + var2.resultado + "\" precisa ser atribuida antes de ser usada."));
                throw new IllegalArgumentException("Erro", null);
                //return null;
            }
            tipo2 = identificadores.get(var2.resultado).tipo;
        }
        else{
            tipo2 = var2.tipoResultado;
        }

        switch(operador){

            case ">":
            case "<":
            case "<=":
            case ">=":
            case "+":
            case "-":
            case "*":
            case "/":
                if(tipo1 == TipoVariavel.CHAR || tipo2 == TipoVariavel.CHAR){
                    System.out.println("\n\nErro na linha "+left1.toString()+" coluna "+right1.toString()+": Operadores aritméticos não podem ser executados com o tipo char");
                    throw new IllegalArgumentException("Erro", null);
                    //return null;
                }
                if(tipo1 == TipoVariavel.BOOL || tipo2 == TipoVariavel.BOOL){
                    System.out.println("\n\nErro na linha "+left1.toString()+" coluna "+right1.toString()+": Operadores aritméticos não podem ser executados com o tipo bool");
                    throw new IllegalArgumentException("Erro", null);
                    //return null;
                }
                if(operador == "+" || operador == "-" || operador == "*" || operador == "/"){
                    return new Expressao(var1.resultado + operador + var2.resultado,tipo1,false);
                }
                break;                
            case "==":
            case "!=":
                if(tipo1 != tipo2){
                    System.out.println("\n\nErro na linha "+left1.toString()+" coluna "+right1.toString()+": É apenas possivel comparar variáveis de mesmo tipo(" + tipo1 + " != " + tipo2 +")");
                    throw new IllegalArgumentException("Erro", null);
                    //return null;
                }
                break;
            case "&&":
            case "||":
            case "^":
                if(tipo1 != TipoVariavel.BOOL || tipo2 != TipoVariavel.BOOL){
                    System.out.println("\n\nErro na linha "+left1.toString()+" coluna "+right1.toString()+": operadores lógicos só podem ser realizados entre tipos booleanos");
                    throw new IllegalArgumentException("Erro", null);
                    //return null;
                }
                break;
        }
		return new Expressao(var1.resultado + operador + var2.resultado,TipoVariavel.BOOL,false);
	}

    //obtem o tipo do identificador, se não achar o identificador, retorna o tipo do proprio objeto
    public TipoVariavel obterTipo(Object obj){
        Identificador identificador = identificadores.get(obj);
        if(identificador != null){
            return identificador.tipo;
        }
        if(obj.toString().startsWith("'")){
            return TipoVariavel.CHAR;
        }
        else if(obj.toString() == "false" || obj.toString() == "true"){
            return TipoVariavel.BOOL;
        }
        else if(obj.toString().contains(".")){
            return TipoVariavel.FLOAT;
        }
        return TipoVariavel.INT;
    }
    
    //obtem o caractere de um tipo, por exemplo float -> f. é usado na compilação de printf e scanf
    public char obterCharDoTipo(TipoVariavel tipo){
        switch(tipo){
            case BOOL:
                return 'b';
            case INT:
                return 'i';
            case FLOAT:
                return 'f';
            case CHAR:
                return 'c';
        }
        return ' ';
    }
}