import java.util.HashMap;
import java.util.Scanner;
import java.util.Iterator;

public class Assembler
{
	private HashMap<String, String[]> implementations;
	private HashMap<String, String> symbols;

	public Assembler(HashMap<String, String[]> insImplementations, HashMap<String, String> memLocations)
	{
		implementations = insImplementations;
		symbols = new HashMap<String, String>();

		Iterator<String> iterator = memLocations.keySet().iterator();

		while (iterator.hasNext())
		{
			String key = iterator.next();
			symbols.put(key, memLocations.get(key));
		}
	}

	public String[] assemble(String[] source)
	{
		initialPass(source);
	}
}