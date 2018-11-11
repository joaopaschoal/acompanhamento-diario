package joaopaulo.acompanhamentodiario.persistence.dao.exception;

public class ModelNotPersistedException extends Exception {

	private static final long serialVersionUID = 1L;

	public ModelNotPersistedException(String message) {
		super(message);
	}
	
	public ModelNotPersistedException(String message, Throwable ex) {
		super(message, ex);
	}
}
