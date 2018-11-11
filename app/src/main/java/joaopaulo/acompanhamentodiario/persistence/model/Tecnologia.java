package joaopaulo.acompanhamentodiario.persistence.model;

/**
 * Created by JoaoPaulo on 07/07/2015.
 */
public class Tecnologia extends Model implements Comparable<Tecnologia> {

    public static final String ACTUAL_NAME = "Tecnologia";

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    @Override
    public int compareTo(Tecnologia another) {
        return another.getNome().compareTo(this.nome);
    }
}
