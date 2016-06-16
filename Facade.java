import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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

		String[] assembled = assemble(source);
		String[] translated = interpret(assembled);

		writeFile(destinationPaths[0], assembled);
		writeFile(destinationPaths[1], translated);
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

			line = parser.nextLine();
		}

		ArrayList<String> macros = new ArrayList<String>();

		line = parser.nextLine();
		while (!line.equals("BREAK"))
		{
			if (!line.equals(""))
				macros.add(line);

			line = parser.nextLine();
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
		while(source.hasNext())
			sourceCode.add(source.nextLine());

		String[] finalSourceCode = new String[sourceCode.size()];

		for (int k = 0; k < finalSourceCode.length; k++)
			finalSourceCode[k] = sourceCode.get(k);

		return finalSourceCode;
	}

	private static void initializeMachine(Scanner parser)
	{
		ArrayList[] machineInformation = new ArrayList[] {new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>()};

		String token = parser.next();
		while (!token.equals("BREAK"))
		{
			machineInformation[0].add(token);
			machineInformation[1].add(parser.next());
			machineInformation[2].add(parser.next());

			token = parser.next();
		}

		Interpreter.initialize(machineInformation);
		Assembler.initializeImplementations(machineInformation[0]);
	}

	private static void writeFile(String filePath, String[] data)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));

			for (String line : data)
			{
				writer.write(line);
				writer.newLine();
			}

			writer.close();
		}
		catch (IOException e) {}
	}

	public static void main(String[] args)
	{
		compile("InstructionFile", "source.txt", new String[] {"assembled.txt", "translated.txt"});
	}
}
