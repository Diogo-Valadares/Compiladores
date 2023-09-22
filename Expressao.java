public class Expressao{
    public String resultado;
    public boolean ehVariavel;
    public TipoVariavel tipoResultado;
    public Expressao(String resultado, TipoVariavel tipoResultado, boolean ehVariavel){
        this.resultado = resultado;
        this.tipoResultado = tipoResultado;
        this.ehVariavel = ehVariavel;
    }
}