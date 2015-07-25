package joaopaulo.acompdiario.persistence.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JoaoPaulo on 07/07/2015.
 */
public class Projeto extends Model implements Comparable<Projeto> {


    public Projeto() {
        this.naturezaProjeto = new NaturezaProjeto();
        this.donoProduto = new DonoProduto();
        this.tecnologiasProjeto = new ArrayList<ProjetoTecnologia>();
    }

    public static final String ACTUAL_NAME = "Projeto";
    private String nome;
    private Date dataCriacao;
    private NaturezaProjeto naturezaProjeto;
    private DonoProduto donoProduto;
    private List<ProjetoTecnologia> tecnologiasProjeto;


    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Date getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    public NaturezaProjeto getNaturezaProjeto() {
        return naturezaProjeto;
    }
    public void setNaturezaProjeto(NaturezaProjeto naturezaProjeto) {
        this.naturezaProjeto = naturezaProjeto;
    }
    public DonoProduto getDonoProduto() {
        return donoProduto;
    }
    public void setDonoProduto(DonoProduto donoProduto) {
        this.donoProduto = donoProduto;
    }
    public List<ProjetoTecnologia> getTecnologiasProjeto() {
        return tecnologiasProjeto;
    }
    public void setTecnologiasProjeto(List<ProjetoTecnologia> tecnologiasProjeto) {
        this.tecnologiasProjeto = tecnologiasProjeto;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int compareTo(Projeto another) {
        return another.getNome().compareTo(this.nome);
    }
}
