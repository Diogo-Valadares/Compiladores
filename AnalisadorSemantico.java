import java.util.*;

public class AnalisadorSemantico {

	private Hashtable<Object, Identificador> identificadores = new Hashtable<>();

    //insere um identificador na tabela 
	public String inserirSimbolo(Object variavel, TipoVariavel tipo) {
        if(identificadores.contains(variavel)){
            System.out.println("\n\nErro: Variável \"" + variavel + "\" já foi previamente declarada");
            return "ERRO SEMÂNTICO";
        }        
		identificadores.put(variavel.toString(), new Identificador(tipo));
        return tipo.toString().toLowerCase() + " " + variavel + ";";
	}

    //obtem um identificador da tabela
    public Identificador obterIdentificador(Object variavel) {
        Identificador identificador = identificadores.get(variavel);

		if (identificador == null) {
			System.out.println(String.format("\n\nErro: Variável \"" + variavel + "\" precisa ser declarada antes de ser usada."));
			return null;
		} 
        return identificador;
	}
	
    //tenta atribuir uma variavel, se der errado retorna "ERRO SEMANTICO"
    //se der certo retorna a atribuição
	public String atribuirVariavel(Object variavel, Expressao valor) {
        TipoVariavel tipo1;
        TipoVariavel tipo2;
        if(!identificadores.containsKey(variavel)) {
            System.out.println("\n\nErro: Variável \"" + variavel + "\" precisa ser declarada antes de ser usada.");
            return null;
        }      
        tipo1 = identificadores.get(variavel).tipo;
              
		if(valor.ehVariavel){
            if(!identificadores.containsKey(valor.resultado)){
                System.out.println("\n\nErro: Variável \"" + valor.resultado + "\" precisa ser declarada antes de ser usada.");
                return null;
		    }            
            if(!identificadores.get(valor.resultado).estaAtribuido){
                System.out.println(String.format("\n\nErro: Variável \"" + valor.resultado + "\" precisa ser atribuida antes de ser usada."));
                return null;
            }
            tipo2 = identificadores.get(valor.resultado).tipo;
        }
        else{
            tipo2 = valor.tipoResultado;
        }

        if(tipo1 != tipo2 && tipo1 != TipoVariavel.INT && tipo1 != TipoVariavel.FLOAT && tipo2 != TipoVariavel.INT && tipo2 != TipoVariavel.FLOAT){
            switch(tipo1){  
                case BOOL:
                    System.out.println(String.format("\n\nErro: O tipo boolean da variável \"" + variavel + "\" só pode receber \"true\" ou \"false\" na atribuição"));
                    break;
                case INT:
                    System.out.println(String.format("\n\nErro: O tipo inteiro da variável \"" + variavel + "\" só pode receber números inteiros na atribuição"));
                    break;
                case FLOAT:
                    System.out.println(String.format("\n\nErro: O tipo float da variável \"" + variavel + "\" só pode receber números decimais na atribuição"));
                    break;
                case CHAR:
                    System.out.println(String.format("\n\nErro: O tipo char da variável \"" + variavel + "\" só pode receber um caractere por vez"));
                    break;
            } 
        }        

        identificadores.get(variavel).estaAtribuido = true;

		return variavel.toString() + "=" + valor.resultado + ";";
	}

    //testa uma expressão, se não for valida vai imprimir o erro e retornar nulo
    //se for valida cria uma expressão nova combinando os duas expressões que foram comparadas
    //e definindo como tipo numerico se for uma expressão +-*/ e tipo bool para qualquer outro operador
	public Expressao testarExpressao(Expressao var1, Expressao var2, String operador) {
        TipoVariavel tipo1;
        TipoVariavel tipo2;
        if(var1.ehVariavel){
            if(!identificadores.containsKey(var1.resultado)) {
                System.out.println("\n\nErro: Variável \"" + var1.resultado + "\" precisa ser declarada antes de ser usada.");
                return null;
		    }            
            if(!identificadores.get(var1.resultado).estaAtribuido){
                System.out.println(String.format("\n\nErro: Variável \"" + var1.resultado + "\" precisa ser atribuida antes de ser usada."));
                return null;
            }
            tipo1 = identificadores.get(var1.resultado).tipo;
        }
        else{
            tipo1 = var1.tipoResultado;
        }
		if(var2.ehVariavel){
            if(!identificadores.containsKey(var2.resultado)){
                System.out.println("\n\nErro: Variável \"" + var2.resultado + "\" precisa ser declarada antes de ser usada.");
                return null;
		    }            
            if(!identificadores.get(var2.resultado).estaAtribuido){
                System.out.println(String.format("\n\nErro: Variável \"" + var2.resultado + "\" precisa ser atribuida antes de ser usada."));
                return null;
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
                    System.out.println("\n\nErro: Operadores aritméticos não podem ser executados com o tipo char");
                    return null;
                }
                if(tipo1 == TipoVariavel.BOOL || tipo2 == TipoVariavel.BOOL){
                    System.out.println("\n\nErro: Operadores aritméticos não podem ser executados com o tipo bool");
                    return null;
                }
                if(operador == "+" || operador == "-" || operador == "*" || operador == "/"){
                    return new Expressao(var1.resultado + operador + var2.resultado,tipo1,false);
                }
                break;                
            case "==":
            case "!=":
                if(tipo1 != tipo2){
                    System.out.println("\n\nErro: É apenas possivel comparar variáveis de mesmo tipo(" + tipo1 + " != " + tipo2 +")");
                    return null;
                }
                break;
            case "&&":
            case "||":
            case "^":
                if(tipo1 != TipoVariavel.BOOL || tipo2 != TipoVariavel.BOOL){
                    System.out.println("\n\nErro: operadores lógicos só podem ser realizados entre tipos booleanos");
                    return null;
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