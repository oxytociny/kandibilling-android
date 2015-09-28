package tools.mikandi.dev.utils;

import java.util.Map;

import android.content.Context;

import com.saguarodigital.returnable.IParser;
import com.saguarodigital.returnable.IReturnableCache;
import com.saguarodigital.returnable.annotation.Type;
import com.saguarodigital.returnable.defaultimpl.AutoParser;
import com.saguarodigital.returnable.defaultimpl.EmptyCache;

/**
* A convenience contract for {@link Type} objects which also
* specify their networking endpoint, parser and caching engine.
* While {@link Type} annotated objects can be manually passed to
* parsers and caches by the application code, developers are
* <em>strongly</em> encouraged to implement IReturnable instead.
*
* Note: A contained class reference of a returnable if *required*
* to implement IReturnable in order to guarantee parser functionality.
* @author Christopher O'Connell
*/
public interface IReturnable {
/**
* Gets the preferred parser for this returnable.
* @return A parser capable of deserializing this object
* @see AutoParser
*/
public IParser<? extends IReturnable> getParser();
/**
* The URL to hit in order to fetch this
* @param args A list of additional arguments to use in constructing
* the url. Implementations should be robust to a value of <code>null</code>.
* @return
*/
public String getUri(Map<String, String> args);
/**
* The caching engine for this type.
* @param context
* @return A caching engine suitable for storing this object
* @see EmptyCache
*/
public IReturnableCache<? extends IReturnable> getCache(Context context);



}