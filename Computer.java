import java.util.Scanner;
import java.io.*;

public class Computer
{
	private int[] data = new int[(int) Math.pow(2, 16)];
	private String[] program;
	private int pc = 1;
	private int ac;
	private int membuffer;
	private int databuffer;
	private Scanner input = new Scanner(System.in);
	private static final int CLOCK = 200;

	public Computer(String[] mem) { program = mem; }

	public void execute() { while (true) step(); }

	private void step()
	{
		try
		{
			Thread.sleep(CLOCK);
		}
		catch (Exception e) {}

		String ins = program[pc];
		String opcode = ins.substring(0, 4);
		System.out.println(pc + " : " + ac);
		pc++;

		switch(opcode)
		{
			case "0000":
				System.exit(0);
				break;

			case "0001":
				break;

			case "0011":
				System.out.println(ac);
				break;

			case "0100":
				ac = data[membuffer];
				break;

			case "0101":
				ac = ac + data[membuffer];
				break;

			case "0110":
				ac = ~(ac & data[membuffer]);
				break;

			case "0111":
				ac = input.nextInt();
				break;

			case "1000":
				databuffer = ac;
				break;

			case "1001":
				membuffer = ac;
				break;

			case "1010":
				membuffer = Integer.parseInt(ins.substring(4), 2);
				break;

			case "1011":
				data[membuffer] = databuffer;
				break;

			case "1100":
				ac = Integer.parseInt(program[pc], 2);
				pc++;
				break;

			case "1101":
			{
				if (ac + data[membuffer] < 0)
					pc = Integer.parseInt(program[pc], 2);
				else
					pc++;
				break;
			}

			case "1110":
			{
				if (ac == data[membuffer])
					pc = Integer.parseInt(program[pc], 2);
				else
					pc++;
				break;
			}

			case "1111":
				pc = Integer.parseInt(program[pc], 2);
				break;
		}
	}

	public static void main(String[] args) throws IOException
	{
		Scanner reader = new Scanner(new File("translated.txt"));
		String[] pmem = new String[(int) Math.pow(2, 16)];
		int c = 1;

		while (reader.hasNext())
		{
			pmem[c] = reader.nextLine();
			c++;
		}

		Computer comp = new Computer(pmem);
		comp.execute();
	}
}