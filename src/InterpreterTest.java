import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class InterpreterTest {

	private ByteArrayOutputStream outContent;

	@Before
	public void setUp()
	{
		outContent = new ByteArrayOutputStream();
	}

	@After
	public void tearDown()
	{
		outContent = null;
	}

	@Test
	public void operationOrder()
	{
		parseString("print(10-5*2);");
		assertEquals("0.0", outContent.toString().trim());
	}

	@Test
	public void can_have_negative_numbers()
	{
		parseString("print(-2.3);");
		assertEquals("-2.3", outContent.toString().trim());
	}

	@Test
	public void can_use_not_equals_comparison()
	{
		parseString("print(1 != 1);");
		assertEquals("0.0", outContent.toString().trim());
	}

	@Test
	public void can_use_equals_comparison()
	{
		parseString("print(1 == 1);");
		assertEquals("1.0", outContent.toString().trim());
	}

	@Test
	public void can_use_less_than_comparison()
	{
		parseString("print(1 < 2);");
		assertEquals("1.0", outContent.toString().trim());
	}

	@Test
	public void can_use_greater_than_comparison()
	{
		parseString("print(1 > 2);");
		assertEquals("0.0", outContent.toString().trim());
	}

	@Test
	public void can_use_greater_than_or_equals_comparison()
	{
		parseString("print(1 >= 2);");
		assertEquals("0.0", outContent.toString().trim());
	}

	@Test
	public void can_use_greater_than_or_equals_where_equals_comparison()
	{
		parseString("print(1 >= 1);");
		assertEquals("1.0", outContent.toString().trim());
	}

	@Test
	public void can_use_less_than_or_equals_comparison()
	{
		parseString("print(1 <= 2);");
		assertEquals("1.0", outContent.toString().trim());
	}

	@Test
	public void can_use_less_than_or_equals_where_equals_comparison()
	{
		parseString("print(1 >= 1);");
		assertEquals("1.0", outContent.toString().trim());
	}

	@Test
	public void can_make_a_if_else_condition()
	{
		parseString("if(1==1) { print(2);} else {print(1);};");
		assertEquals("2.0", outContent.toString().trim());
	}

	@Test
	public void can_make_a_if_else_where_else_runs_condition()
	{
		parseString("if(1==0) { print(2);} else {print(1);};");
		assertEquals("1.0", outContent.toString().trim());
	}

	@Test
	public void can_make_a_if_condition()
	{
		parseString("if(1==1) { print(2);};");
		assertEquals("2.0", outContent.toString().trim());
	}

	@Test
	public void can_make_a_while_loop()
	{
		parseString("I = 0; while(I<3) {I=I+1;}; print(I);");
		assertEquals("3.0", outContent.toString().trim());
	}

	@Test
	public void can_have_single_line_comments()
	{
		parseString("print(1); #prints 1 out \n print(2);");
		assertEquals("1.0\n2.0", outContent.toString().trim());
	}

	@Test
	public void can_have_multi_line_comments()
	{
		parseString("print(2); /* first line \n second line \n */ print(3);");
		assertEquals("2.0\n3.0", outContent.toString().trim());
	}

	@Test
	public void can_have_inline_comment()
	{
		parseString("print(/* inline comment */ 2);");
		assertEquals("2.0", outContent.toString().trim());
	}

	@Test
	public void can_run_a_sequence_of_commands()
	{
		parseString("print(1); print(2);");
		assertEquals("1.0\n2.0", outContent.toString().trim());
	}

	@Test
	public void can_convert_double_to_boolean()
	{
		parseString("print(not not 12.3);");
		assertEquals("1.0", outContent.toString().trim());
	}

	@Test
	public void and_weights_higher_than_or()
	{
		parseString("print( 0 and 1 or 1);");
		assertEquals("1.0", outContent.toString().trim());
	}

	private void parseString(String program)
	{
		// create a lexer/scanner
		simpleCalcLexer lex = new simpleCalcLexer(CharStreams.fromString(program));

		// get the stream of tokens from the scanner
		CommonTokenStream tokens = new CommonTokenStream(lex);

		// create a parser
		simpleCalcParser parser = new simpleCalcParser(tokens);

		// and parse anything from the grammar for "start"
		ParseTree parseTree = parser.start_();

		// Construct an interpreter and run it on the parse tree
		Interpreter interpreter = new Interpreter(new PrintStream(outContent));
		interpreter.visit(parseTree);
	}
}