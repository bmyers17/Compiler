import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class Facade
{
	private static String[] assemble(String[] source)
	{
		return Assembler.assemble(source);
	}

	private static String[] interpret(String[] source)
	{
		return Interpreter.translate(source);
	}

	private static void compile(String configurationPath, String sourcePath, String[] destinationPaths)
	{
		Scanner configurator = initializeScanner(configurationPath);
		Scanner reader = initializeScanner(sourcePath);

		initializeMachine(configurator);

		String[] source = addPreface(configurator, reader);
	}

	private static Scanner initializeScanner(String filePath)
	{
		Scanner parser = null;

		try
		{
			parser = new Scanner(new File(filePath));
		}
		catch (FileNotFoundException e) {}

		return parser;
	}

	private static String[] addPreface(Scanner parser, Scanner source)
	{
		ArrayList<String> symbols = new ArrayList<String>();

		String line = parser.nextLine();
		while (!line.equals("BREAK"))
		{
			if (!line.equals(""))
				symbols.add(line);
		}

		ArrayList<String> macros = new ArrayList<String>();

		line = parser.nextLine();
		while (!line.equals("BREAK"))
		{
			if (!line.equals(""))
				macros.add(line);
		}

		ArrayList<String> sourceCode = new ArrayList<String>();

		line = source.nextLine();
		while (line.charAt(0) != '@')
		{
			sourceCode.add(line);
			line = source.nextLine();
		}
		sourceCode.add(line);

		while (macros.size() != 0)
			sourceCode.add(macros.remove(0));
		while (symbols.size() != 0)
			sourceCode.add(symbols.remove(0));

		String[] finalSourceCode = new String[sourceCode.size()];

		for (int k = 0; k < finalSourceCode.length; k++)
			finalSourceCode[k] = sourceCode.get(k);

		return finalSourceCode;
	}

	private static void initializeMachine(Scanner parser)
	{
		ArrayList[] machineInformation = new ArrayList[3];

		String token = parser.next();
		while (!token.equals("BREAK"))
		{
			machineInformation[0].add(token);
			machineInformation[1].add(parser.next());
			machineInformation[2].add(parser.next());

			token = parser.next();
		}

		Interpreter.initialize(machineInformation);
	}
}
