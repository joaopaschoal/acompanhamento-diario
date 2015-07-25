package joaopaulo.acompdiario.persistence.model;

/**
 * Created by JoaoPaulo on 07/07/2015.
 */
public class DonoProduto extends Model implements Comparable<DonoProduto> {
    public static final String ACTUAL_NAME = "Dono do Produto";

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int compareTo(DonoProduto another) {
        return another.getNome().compareTo(this.nome);
    }
}
