import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

public class Interpreter
{
	private static HashMap<String, String> codes;
	private static HashMap<String, String> types;

	public static void initialize(ArrayList[] machineInformation)
	{
		codes = new HashMap<String, String>();
		types = new HashMap<String, String>();

		ArrayList<String> acronym = machineInformation[0];
		ArrayList<String> binary = machineInformation[1];
		ArrayList<String> insType = machineInformation[2];

		for (int k = 0; k < machineInformation[0].size(); k++)
		{
			codes.put(acronym.get(k), binary.get(k));
			types.put(acronym.get(k), insType.get(k));
		}
	}

	public static String[] translate(String lines[])
	{
		int length = lines.length;

		for (String instruction : lines)
			if (types.get(instruction.substring(0, instruction.indexOf(" "))).equals("J"))
				length++;

		String[] machineCode = new String[length];
		int index = 0;

		for (int k = 0; k < lines.length; k++)
		{
			Scanner parser = new Scanner(lines[k]);

			String opcode = parser.next();
			String translated = codes.get(opcode);

			if (parser.hasNext())
			{
				String argument = parser.next();

				if (types.get(opcode).equals("J"))
				{
					machineCode[index] = translated + format("x0", 12);
					index++;

					translated = format(argument, 16);
				}
				else
					translated += format(argument, 12);
			}
			else
				translated += format("x0", 12);

			machineCode[index] = translated;
			index++;
		}

		return machineCode;
	}

	private static String format(String number, int bits)
	{
		char base = number.charAt(0);
		String value = number.substring(1);

		int parsed = 0;

		if (base == 'x')
			parsed = Integer.parseInt(value, 16);
		else if (base == 'b')
			parsed = Integer.parseInt(value, 2);
		else if (base == 'd')
			parsed = Integer.parseInt(value, 10);

		String formatted = Integer.toString(parsed, 2);

		while (formatted.length() < bits)
			formatted = "0" + formatted;

		return formatted;
	}
}