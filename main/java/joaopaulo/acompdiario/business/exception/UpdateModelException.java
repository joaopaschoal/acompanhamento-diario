package joaopaulo.acompdiario.business.exception;

public class UpdateModelException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public UpdateModelException() {
		super();
	}

	public UpdateModelException(String msg) {
		super(msg);
	}
	
	public UpdateModelException(Throwable ex) {
		super(ex);
	}
	
	public UpdateModelException(String msg, Throwable ex) {
		super(msg, ex);
	}

}
