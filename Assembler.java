import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;
import java.io.File;

public class Assembler
{
	private static HashMap<String, String[]> implementations;
	private static HashMap<String, String> symbols;
	private static ArrayList<String> machineCodes;
	private static int memoryLocation;

	public static void initializeImplementations(ArrayList<String> codes)
	{
		machineCodes = codes;
	}

	public static String[] assemble(String[] source)
	{
		implementations = new HashMap<String, String[]>();
		symbols = new HashMap<String, String>();

		source = initialPass(source);
		
		return finalPass(source);
	}

	private static String[] initialPass(String[] source)
	{
		for (int k = 0; k < source.length; k++)
			source[k] += " ";

		source = removeComments(source);
		source = parseInitial(source);
		source = decompress(source);
		source = mapSymbols(source);

		return source;
	}

	private static String[] finalPass(String[] source)
	{
		replaceSymbols(source);
		return source;
	}

	private static String[] removeComments(String[] source)
	{
		ArrayList<String> updatedSource = new ArrayList<String>();

		for (String line : source)
		{
			int index = line.indexOf("//");

			if (index > 0)
				updatedSource.add(line.substring(0, index));
			else if (index == -1 && line.length() > 0)
				updatedSource.add(line);
		}

		String[] sourceNoComments = new String[updatedSource.size()];

		for (int k = 0; k < sourceNoComments.length; k++)
			sourceNoComments[k] = updatedSource.get(k);

		return sourceNoComments;
	}

	private static String[] parseInitial(String[] source)
	{
		memoryLocation = Integer.parseInt(source[0].substring(1, source[0].indexOf(" ")), 16);

		int macros = 1;

		while (source[macros].substring(0, source[macros].indexOf(" ")).equals("MACRO"))
		{
			String code = source[macros].substring(source[macros].indexOf(" ")).trim();
			ArrayList<String> implementation = new ArrayList<String>();

			macros++;

			while (!source[macros].substring(0, source[macros].indexOf(" ")).equals("END"))
			{
				implementation.add(source[macros]);
				macros++;
			}

			macros++;

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

	private static boolean machineSupported(String[] source)
	{
		for (String line : source)
			if (!machineSupported(line))
				return false;

		return true;
	}

	private static boolean machineSupported(String line)
	{
		String code = line.substring(0, line.indexOf(" "));

		if (!code.equals("label") && !machineCodes.contains(code))
			return false;

		return true;
	}

	private static String[] decompress(String[] source)
	{
		if (machineSupported(source))
			return source;

		ArrayList<String> decompressedSource = new ArrayList<String>();

		for (String line : source)
			if (machineSupported(line))
				decompressedSource.add(line);
			else
			{
				String[] implementation = decompress(line);
				implementation = decompress(implementation);

				for (String instruction : implementation)
					decompressedSource.add(instruction);
			}

		String[] updatedSource = new String[decompressedSource.size()];

		for (int k = 0; k < updatedSource.length; k++)
			updatedSource[k] = decompressedSource.get(k);

		return updatedSource;
	}

	private static String[] decompress(String line)
	{
		Scanner parser = new Scanner(line);
		String code = parser.next();
		String[] implementation = null;

		try
		{
			implementation = new String[implementations.get(code).length];
		}
		catch (NullPointerException e) {System.out.println(code); System.exit(0);}

		for (int k = 0; k < implementation.length; k++)
			implementation[k] = implementations.get(code)[k];

		int arg = 0;

		while (parser.hasNext())
		{
			replaceAll(implementation, "@" + arg, parser.next());
			arg++;
		}

		return implementation;
	}

	private static void replaceAll(String[] array, String target, String replace)
	{
		for (int k = 0; k < array.length; k++)
			array[k] = array[k].replace(target, replace);
	}

	private static String[] mapSymbols(String[] source)
	{
		ArrayList<String> updatedSource = new ArrayList<String>();

		for (String line : source)
		{
			if (line.substring(0, line.indexOf(" ")).equals("label"))
			{
				String id = line.substring(line.indexOf(" ") + 1, line.indexOf("=") - 1);
				String value = line.substring(line.indexOf("=") + 1).trim();

				if (value.equals("here"))
					value = "x" + Integer.toString(memoryLocation + updatedSource.size(), 16);
				else if (symbols.containsKey(value))
					value = symbols.get(value);

				symbols.put(id, value);
			}
			else
				updatedSource.add(line);
		}

		String[] sourceNoSymbols = new String[updatedSource.size()];

		for (int k = 0; k < sourceNoSymbols.length; k++)
			sourceNoSymbols[k] = updatedSource.get(k);

		return sourceNoSymbols;
	}

	private static void replaceSymbols(String[] source)
	{
		Iterator<String> keys = symbols.keySet().iterator();

		while (keys.hasNext())
		{
			String target = keys.next();
			String replace = symbols.get(target);

			for (int k = 0; k < source.length; k++)
				source[k] = source[k].replace(target, replace);
		}
	}
}