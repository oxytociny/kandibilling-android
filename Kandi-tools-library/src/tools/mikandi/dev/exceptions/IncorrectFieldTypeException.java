package tools.mikandi.dev.exceptions;

public final class IncorrectFieldTypeException extends Exception {
	private static final long serialVersionUID = -5351652617233283132L;

	private String mTag;
	@SuppressWarnings("rawtypes")
	private Class mClass;

	@SuppressWarnings("rawtypes")
	public IncorrectFieldTypeException(final String tag, final Class clazz) {
		super("The required field " + tag
				+ " could not be coerced to the required type "
				+ clazz.getSimpleName());
	}

	public String getTag() {
		return this.mTag;
	}

	@SuppressWarnings("rawtypes")
	public Class getRequiredClass() {
		return this.mClass;
	}
}
