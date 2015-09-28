package tools.mikandi.dev.ads;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import tools.mikandi.dev.library.KandiLibs;

public class AdUtils {
	
	public static boolean isANiche(String s) { 
		for (String n : KandiLibs.niches) { 
			if (n.equalsIgnoreCase(s)) { 
				return true;
			}
		}
		return false;
	}
	
	public static String[] cleanUpList(String[] desired) { 	
		List<String> l = new LinkedList<String>(); 
		
		for (String d : desired) { 
			if (isANiche(d)) { 
				l.add(d);
			}
		}	
		return l.toArray(new String[l.size()]);
	}
	
	
	public static String getNiche(String[] arr) { 
		arr = cleanUpList(arr);
		int i = arr.length;
		
		if (i == 1) { 
			return arr[0];
		}
		
		// chose random in list and return it
		Random r = new Random(); 	
		return arr[r.nextInt(i)];
	}
	
	
	
	
}
