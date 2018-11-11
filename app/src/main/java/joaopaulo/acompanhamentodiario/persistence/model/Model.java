package joaopaulo.acompanhamentodiario.persistence.model;


import java.io.Serializable;

public abstract class Model implements Serializable {

	protected Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Model)) {
			return false;
		}
		Model m = (Model)o;
		return m.getId() == this.id;
	}
}
