package tools.mikandi.dev.utils;

import tools.mikandi.dev.library.KandiLibs;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

public final class InstallerCheck {
	/**
	 * This function checks if the current application was installed by MiKandi. Returns 
	 * true if it was, false if it was not.
	 * @param ctx
	 * @return
	 */
	public static boolean checkInstaller(Context ctx) {
		try {

			PackageManager pm = ctx.getPackageManager();
			String packagename = ctx.getPackageName();
			if (KandiLibs.debug)
				Log.e("CheckInstaller", "Printing out package Name: "
						+ packagename);
			if (KandiLibs.debug)
				Log.e("CheckInstaller",
						"Retreived package manager" + pm.toString());
			final String name = pm.getInstallerPackageName(packagename);

			if (name == null) {
				if (KandiLibs.debug)
					Log.e("checkinstaller", "checkinstaller is null");
				Toast.makeText(
						ctx,
						"package installed by (null means installed by system not mikandi) : "
								+ name, Toast.LENGTH_SHORT).show();
				return false;
			}
			Toast.makeText(
					ctx,
					"package installed by (null means installed by system not mikandi) : "
							+ name, Toast.LENGTH_SHORT).show();
			final boolean installer = name.equals(MiKandiUtils.installer);
			if (KandiLibs.debug) Log.e("Check installer" , "returning : " + installer);
			return installer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
