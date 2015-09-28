package tools.mikandi.dev.exceptions;

public final class MissingRequiredFieldException extends Exception {
	private static final long serialVersionUID = 8974580661778050541L;

	private String mTag;
	public MissingRequiredFieldException(final String tag) {
		super("The required field " + tag + " could not be found");
		this.mTag = tag;
	}
	
	public String getTag() {
		return this.mTag;
	}
}
