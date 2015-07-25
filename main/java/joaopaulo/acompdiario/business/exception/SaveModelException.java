package joaopaulo.acompdiario.business.exception;

public class SaveModelException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public SaveModelException() {
		super();
	}
	
	public SaveModelException(String msg) {
		super(msg);
	}
	
	public SaveModelException(Throwable ex) {
		super(ex);
	}
	
	public SaveModelException(String msg, Throwable ex) {
		super(msg, ex);
	}

}
