package tools.mikandi.dev.login;

import java.util.List;

public interface ILoginReturnable {
	public boolean isValid();

	public String getAuthHash();

	public int getUserId();

	public List<String> getTokens();

	public String getAuthExpires();

	public String getDisplayName();
	
	public List<String> getPurchases();
}
