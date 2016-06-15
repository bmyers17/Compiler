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

	private static void initialize(String filePath)
	{
		String[] file = null;

		try
		{
			file = inputFile(filePath);
		}
		catch (Exception e) {}
	}

	private static String[] inputFile(String filePath) throws FileNotFoundException
	{
		File file = new File(filePath);
		Scanner input = new Scanner(file);

		ArrayList<String> fileIn = new ArrayList<String>();

		while (input.hasNext())
			fileIn.add(input.next());

		String[] inputFile = new String[fileIn.size()];

		for (int k = 0; k < inputFile.length; k++)
			inputFile[k] = fileIn.get(k);

		return inputFile;
	}
}
