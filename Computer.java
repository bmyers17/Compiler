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
			case "0000":	//HLT
				System.exit(0);
				break;

			case "0001":	//NOP
				break;

			case "0010":	//LTA
				membuffer = Integer.parseInt(ins.substring(4), 2);
				break;

			case "0011":	//ACA
				membuffer = ac;
				break;

			case "0100":	//FCH
				ac = data[membuffer];
				break;

			case "0101":	//NND
				ac = ~(ac & data[membuffer]);
				break;

			case "0110":	//ADD
				ac = ac + data[membuffer];
				break;

			case "0111":	//SUB
				ac = data[membuffer] - ac;
				break;

			case "1000":	//RDV
				ac = input.nextInt();
				break;

			case "1001":	//WRT		
				System.out.println(ac);
				break;

			case "1010":	//DAT
				databuffer = ac;
				break;

			case "1011":	//STR
				data[membuffer] = databuffer;
				break;

			case "1100":	//LOD
				ac = Integer.parseInt(program[pc], 2);
				pc++;
				break;

			case "1101":	//BLT
			{
				if (data[membuffer] - ac < 0)
					pc = Integer.parseInt(program[pc], 2);
				else
					pc++;
				break;
			}

			case "1110":	//BEQ
			{
				if (ac == data[membuffer])
					pc = Integer.parseInt(program[pc], 2);
				else
					pc++;
				break;
			}

			case "1111":	//JMP
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