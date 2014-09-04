package tools.mikandi.dev.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.mikandi.dev.exceptions.IncorrectFieldTypeException;
import tools.mikandi.dev.exceptions.MissingRequiredFieldException;
import android.util.Log;

import com.saguarodigital.returnable.IReturnable;

/**
 * A helper class to wrap JSONObject and provide more detailed exceptions
 * for missing fields or fields with the incorrect types.
 */
public final class ParserUtils {
	private JSONObject mJo;
	/**
	 * Constructs a new {@link ParserUtils} wrapping the specified
	 * {@link JSONObject}.
	 * @param jo The {@link JSONObject} to wrap.
	 */
	public ParserUtils(final JSONObject jo) {
		this.mJo = jo;
	}
	/**
	 * Loads the specified tag as a {@link Boolean}, if available or else
	 * uses the fallback value.
	 * @param tag The field name to load.
	 * @param fallback The fallback value in the event that the tag cannot be found.
	 * @return The specified {@link Boolean} or fallback.
	 */
	public final Boolean loadBoolean(final String tag, final Boolean fallback) {
		return this.mJo.optBoolean(tag, fallback);
	}
	/**
	 * Required the specified tag as a {@link Boolean}, or raises an exception
	 * if the tag doesn't exists or cannot be coerced a {@link Boolean}.
	 * @param tag The field name to load.
	 * @return The specified {@link Boolean}.
	 * @throws IncorrectFieldTypeException
	 * @throws MissingRequiredFieldException
	 */
	public final Boolean requireBoolean(final String tag) throws IncorrectFieldTypeException, MissingRequiredFieldException {
		if (this.mJo.has(tag)) {
			try {
				return this.mJo.getBoolean(tag);
			} catch (final JSONException e) {
				throw new IncorrectFieldTypeException(tag, Boolean.class);
			}
		} else {
			throw new MissingRequiredFieldException(tag);
		}
	}
	
	public final long requireLong(final String tag) {
			
		try {
			long temp = this.mJo.getLong(tag);
			if (temp != 0) {
				return this.mJo.optLong(tag);
			}

		} catch (JSONException e) {

			e.printStackTrace();
		}
		return 0;

	}
	
	public final long loadLong(final String tag, final long fallbackLong) { 
		return this.mJo.optLong(tag, fallbackLong);
	}
	
	
	public final Integer loadInteger(final String tag, final Integer fallback) {
		return this.mJo.optInt(tag, fallback);
	}
	public final Integer requireInteger(final String tag) throws IncorrectFieldTypeException, MissingRequiredFieldException {
		if (this.mJo.has(tag)) {
			try {
				return this.mJo.getInt(tag);
			} catch (final JSONException e) {
				throw new IncorrectFieldTypeException(tag, Integer.class);
			}
		} else {
			throw new MissingRequiredFieldException(tag);
		}
	}
	public final String loadString(final String tag, final String fallback) {
		return this.mJo.optString(tag, fallback);
	}
	
	public final double loadDouble(String doubleVal,double fallback) { 
		return this.mJo.optDouble(doubleVal, fallback);
	}
	
	public final String requireString(final String tag) throws IncorrectFieldTypeException, MissingRequiredFieldException {
		if (this.mJo.has(tag)) {
			try {
				return this.mJo.getString(tag);
			} catch (final JSONException e) {
				throw new IncorrectFieldTypeException(tag, String.class);
			}
		} else {
			throw new MissingRequiredFieldException(tag);
		}
	}
	public final List<String> loadStringList(final String tag, final String... fallbacks) {
		final List<String> ret = new ArrayList<String>();
		JSONArray arr = null;
		if ( (arr = this.mJo.optJSONArray(tag)) != null && arr.length() > 0) {
			for (int i = 0; i < arr.length(); i += 1) {
				try {
					ret.add(arr.getString(i));
				} catch (final Exception e) {}
			}
		} else if (arr != null && arr.length() < fallbacks.length) {
			for (final String s : fallbacks) {
				ret.add(s);
			}
		}
		return ret;
	}
	
	
	public final List<Integer> loadIntegerList(final String tag , final int ... fallbacks) { 
		
		final List<Integer> ret = new ArrayList<Integer>(); 
		JSONArray arr = null;
		if ( (arr = this.mJo.optJSONArray(tag)) != null && arr.length() > 0) {
			for (int i = 0; i < arr.length(); i += 1) {
				try {
					ret.add(arr.getInt(i));
				} catch (final Exception e) {}
			}
		} else if (arr != null && arr.length() < fallbacks.length) {
			for (final Integer s : fallbacks) {
				ret.add(s);
			}
		}
		return ret;
	}
	
	/**
	 * Retrieves an ArrayList instance of strings from the passed JSON
	 * array.
	 * 
	 * @param jsonArray
	 *            the JSON array to parse
	 * @return the ArrayList populated with the data retrieved from the
	 *         parameter.
	 */
	protected ArrayList<String> getArrayList(JSONArray jsonArray) {
		int len = jsonArray == null ? 0 : jsonArray.length();
		ArrayList<String> list = new ArrayList<String>(len);
		try {
			for (int i = 0; i < len; i++) {
				list.add(jsonArray.getString(i));
			}
		} catch (Exception e) {
			if (Logger.parserDebug)
				Log.e("parser:" , "getArrayList Error with :"
						+ jsonArray.toString(), e);
		}
		return list;
	}

	/**
	 * Parses and returns a typed ArrayList from the passed JSON array
	 * 
	 * @param clazz
	 *            the generic type of the resulting ArrayList
	 * @param jsonArray
	 *            the JSON array which has to be parsed
	 * @return an
	 *         <code>ArrayList&lt;clazz&gt;<code> retrieved from the JSON array parameter
	 */
	public <T extends IReturnable> ArrayList<T> getGenericArrayList(
			Class<T> clazz, JSONArray jsonArray) {

		int len = jsonArray == null ? 0 : jsonArray.length();
		ArrayList<T> list = new ArrayList<T>(len);
		try {
			for (int i = 0; i < len; i++) {
				final T instance = clazz.newInstance();
				instance.getParser().parse(jsonArray.getJSONObject(i),
						instance);
				list.add(instance);
			}
		} catch (Exception e) {
			if (Logger.parserDebug)
				Log.e("parser utils ", "getGenericArrayList error", e);
		}
		return list;
	}	
}
