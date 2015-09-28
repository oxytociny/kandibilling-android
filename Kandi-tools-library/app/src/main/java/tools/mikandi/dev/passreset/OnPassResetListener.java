package tools.mikandi.dev.passreset;

public interface OnPassResetListener {

		/**
		 * Called when the Password reset was successful. 
		 */
		void onResetSuccess();
		
		/**
		 * Called when the login failed.
		 */
		void onResetFailed();
	}

	
	
	
	

