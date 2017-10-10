grammar simpleCalc;

/* A grammar for arithmetic expressions

Variables: start, expr
Terminals: EOF (end of file, a builtin symbol),
           '+', '*',  NUM, ID, and the ignored WHITESPACE

ANTLR Notation: 

Instead of the usual grammar notation
  Variable -> alpha_1 | ... | alpha_n
the ANTLR notation is
  Variable :  alpha_1 | ... | alpha_n ;

The labels like "#Addition" help later in processing the parse tree.

*/


start_: sequence_ EOF;

declaration_ : ID '=' operation_ # Declaration;

summator_ :
    '+'
    |'-'
;

partitioner_ :
    '*'
    |'/'
;

relation_comparison_ :
    '<'
    | '>'
    | '<='
    | '>='
;

equality_comparison_ :
    '=='
    |'!='
;

operation_ :
    NUM # Number
    | ID # Identifier
    | '(' operation_ ')'  # Parenthesis
    | '-' operation_ #Negativ
    | 'not' operation_ # Not
    | operation_ partitioner_ operation_ # Partition
    | operation_ summator_ operation_ # Summation
    | operation_ relation_comparison_ operation_ #RelationComparison
    | operation_ equality_comparison_ operation_ #EqualityComparison
    | operation_ 'and' operation_ # And
    | operation_ 'or' operation_ # Or
    | operation_ 'if' operation_ 'else' operation_ # Ternary
;

condition_ :
    'if' '(' operation_ ')' '{' sequence_ '}' #ConditionIf
    | 'if' '(' operation_ ')' '{' sequence_ '}' 'else' '{' sequence_ '}' #ConditionIfElse
;

while_ : 'while' '(' operation_ ')' '{' sequence_ '}';

print_ : 'print(' expression_ ')';

expression_ :
    operation_
    | declaration_
    | condition_
    | while_
    | print_
;

sequence_ :
    sequence_ sequence_ #ChainedSequence
    | expression_ ';' #SingleSequence
    | ';' #EmptySequence
;

NUM : ('0'..'9')+('.'('0'..'9')*)?;
ID : ('A'..'Z');
WHITESPACE : [ \n\t\r] -> skip;
COMMENT :
    (
    '#' .+? '\n' | '\r'
    | '/*' .+? '*/'
    )
-> skip;
