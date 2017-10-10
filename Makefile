antlr4 = java -jar /usr/local/lib/antlr-4.7-complete.jar
SRCFILES = src.Main.java
GENERATED = simpleCalcListener.java simpleCalcBaseListener.java simpleCalcParser.java simpleCalcBaseVisitor.java simpleCalcVisitor.java simpleCalcLexer.java

all:
	make src.Main.class

src.Main.class:	$(SRCFILES) $(GENERATED) simpleCalc.g4
	javac  $(SRCFILES) $(GENERATED)

simpleCalcListener.java:	simpleCalc.g4
	$(antlr4) -visitor simpleCalc.g4

test:	src.Main.class
	java src.Main simpleCalc_input.txt
