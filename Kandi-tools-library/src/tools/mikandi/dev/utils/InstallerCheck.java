package tools.mikandi.dev.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

public final class InstallerCheck {

	public static boolean checkInstaller(Context ctx) {
		try {
			Log.e("CheckInstaller", "just after try");
			PackageManager pm = ctx.getPackageManager();
			Log.e("CheckInstaller", "Retreived package manager");
			Log.i("pm out ", " : " + pm.toString());
			final String name = pm
					.getInstallerPackageName("com.example.testapp");

			Log.e("CheckInstaller", "printing name: " + name);
			Log.e("check installer, null is pc install, com.mikandi.vending is sucessful install intent",
					" printing out installer :" + name);
			Toast.makeText(
					ctx,
					"package installed by (null means installed by system not mikandi) : "
							+ name, Toast.LENGTH_SHORT).show();

			if (name == null) {
				Log.e("checkinstaller", "checkinstaller is null");
				return false;
			}
			return name.equals(MiKandiUtils.installer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
