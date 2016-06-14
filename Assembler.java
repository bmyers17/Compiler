import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;
import java.io.File;

public class Assembler
{
	private static HashMap<String, String[]> implementations;
	private static HashMap<String, String> symbols;
	private static int memoryLocation;

	private static void initializeImplementations(File insImplementations)
	{
		implementations = new HashMap<String, String[]>();
	}

	public static String[] assemble(String[] source, File instructionFile)
	{
		initializeImplementations(instructionFile);
		initialPass(source);

		return null;
	}

	private static void initialPass(String[] source)
	{
		source = parseInitial(source);
	}

	private static String[] parseInitial(String[] source)
	{
		memoryLocation = Integer.parseInt(source[0].substring(1), 16);

		int macros = 1;

		while (source[macros].substring(0, source[macros].indexOf(" ")).equals("MACRO"))
		{
			String code = source[macros].substring(0, source[macros].indexOf(" "));
			ArrayList<String> implementation = new ArrayList<String>();

			macros++;

			while (!source[macros].substring(0, source[macros].indexOf(" ")).equals("END"))
			{
				implementation.add(source[macros]);
				macros++;
			}

			String[] finalImplementation = new String[implementation.size()];

			for (int k = 0; k < implementation.size(); k++)
				finalImplementation[k] = implementation.get(k);

			implementations.put(code, finalImplementation);
		}

		String[] adjusted = new String[source.length - macros];

		for (int k = 0; k < adjusted.length; k++)
			adjusted[k] = source[macros + k];

		return adjusted;
	}
}