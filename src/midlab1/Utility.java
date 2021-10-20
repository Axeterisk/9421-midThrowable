package midlab1;


/*
    Provides utility functions for the Interface class
 */
public class Utility {
    static <T> void printStack(LinkedStack<T> stack) {

        if (stack.isEmpty()) return;

        T element = stack.peek();
        stack.pop();
        printStack(stack);

        System.out.print(element + " ");
        stack.push(element);
    }

    /*
    parseInput():
    reads the input of the user for infixToPostfixTable()
    reads the input of the user for postfixEvaluateTable()
    creates token objects and returns as stack of tokens.
    */
    static LinkedStack<Token> parseInput(String input) {
        Token token;
        String symbol = "";
        LinkedStack<Token> stack = new LinkedStack<>();
        /*
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            String line = reader.readLine();
            String symbol = "";
            for (int i = line.length() - 1; i != -1; i--) {
                char c = line.charAt(i);
                symbol = Character.toString(c);
                token = new Token(symbol);
                stack.push(token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stack;

         */

        for (int i = input.length() - 1; i != -1; i--) {
            char c = input.charAt(i);
            symbol = Character.toString(c);
            token = new Token(symbol);
            stack.push(token);
        }

        return stack;
    }

    /*  @Enrico @Clarence @Cj (less workload for Clarence, since assigned to parseInput())
        fromInfixtoPostfixTable():
            Take the stack of tokens from parseInput()
            Then output a table of values shown on PAGES 11-12
     */
    /**
 * Method Description: Create a method that converts an infix expression into a postfix expression through a stack data structure
 *                     From left to right, an operand is added to a postfix form. The operator and parenthesis will then be
 *                     added to the stack,
 * ALGORITHM:
 * Instantiate current = stack.getTop()
 * Instantiate operatorStack
 * Instantiate postFixStack
 * Instantiate d
 * print table headings
 * while current is not null
 *      print current
 *      if current contains a (
 *          print postFixStack
 *          push current to operator's stack
 *          print operatorStack
 *      if current contains a )
 *          while current does not contain (
 *              pop operatorStack and push to postFixStack
 *              if operatorSTack contains (
 *                  pop operatorStack and push to d
 *                  print postFixStack
 *                  print operatorStack
 *                  break
 *      if current contains an operator
 *          while icp of current < isp of operatorStack
 *              pop operatorStack and push to postFixStack
 *      else if icp of current > isp of operatorStack
 *          print postFixStack
 *          push current to operatorStack
 *          print operatorStack
 *      if current is an exponent operator
 *          if operatorStack is empty
 *              print postFixStack
 *              push current to operatorStack
 *              print operatorStack
 *      if current contains an operand
 *          print postFixStack
 *          push/concatenate current to operator's stack
 *          print operatorStack
 *      get link of current
 * if operatorStack is empty
 *      pop operatorStack and push it to postFixStack
 *      print postFixStack
 *      print operatorStack
 *
 *
 *
 */
    static void infixToPostfixTable(LinkedStack<Token> stack) {

        Node<Token> current = stack.getTop();
        LinkedStack<Token> operatorStack = new LinkedStack<>();
        LinkedStack<Token> postFixStack = new LinkedStack<>();
        LinkedStack<Token> d = new LinkedStack<>();

        System.out.printf("%-10s%-25s%-10s%n", "Symbol", "postfixExpression", "operatorStack");
        while(current != null) {
            System.out.printf("%-10s", current.getInfo());
            if (current.getInfo().toString().equals("(")){
                printStack(postFixStack);
                System.out.printf("%-25s", " ");
                operatorStack.push(current.getInfo());
                printStack(operatorStack);
                System.out.println();
            }
            if (current.getInfo().toString().equals(")")){
                while(!operatorStack.peek().toString().equals("(")) {
                    postFixStack.push(operatorStack.pop());

                    if (operatorStack.peek().toString().equals("(")){
                        d.push(operatorStack.pop());
                        printStack(postFixStack);
                        System.out.printf("%-25s", " ");
                        printStack(operatorStack);
                        System.out.println();
                        break;
                    }
                }
            }

            if (current.getInfo().isOperator) {

                if (current.getInfo().icp < operatorStack.peek().isp) {
                    while (current.getInfo().icp < operatorStack.peek().isp) {
                        postFixStack.push(operatorStack.pop());
                    }
                } else if (current.getInfo().icp > operatorStack.peek().isp) {
                    printStack(postFixStack);
                    System.out.printf("%-25s", " ");
                    operatorStack.push(current.getInfo());
                    printStack(operatorStack);
                    System.out.println();
                }
            }
            if(current.getInfo().isEx) {
                if (operatorStack.isEmpty()) {
                    printStack(postFixStack);
                    System.out.printf("%-25s", " ");
                    operatorStack.push(current.getInfo());
                    printStack(operatorStack);
                    System.out.println();
                }
            }
            if (current.getInfo().isOperand){

                postFixStack.push(current.getInfo());
                printStack(postFixStack);
                System.out.printf("%-25s", " ");
                printStack(operatorStack);
                System.out.println();

            }

            current = current.getLink();
        }
        if(!operatorStack.isEmpty()){
            postFixStack.push(operatorStack.pop());
            System.out.printf("%-10s", " ");
            printStack(postFixStack);
            System.out.printf("%-25s", " ");

            printStack(operatorStack);
            System.out.println();
        }
    }

    /*  @Jerome @Cj (less workload for CJ, since assigned to parseInput())
        fromPostfixToInfixTable():
            Take the stack of tokens from parseInput()
            Then output a table of values shown on PAGES 12-13
     */
    static void postfixEvaluateTable(LinkedStack<Token> stack) {
        Node<Token> current = stack.getTop();
        LinkedStack<Token> operandStack = new LinkedStack<>();
        Token newToken;
        String x= "";
        String y ="";
        double operand1, operand2;
        System.out.printf("%-10s%-10s%-10s%-10s%-10s%n", "Symbol", "operand1", "operand2", "value", "operandStack");
        while(current != null){
            if (current.getInfo().toString().equals(" ")) {
                current = current.getLink();
                x=y;
            }

            if (current.getInfo().toString().equals(" ")) {
                current = current.getLink();
            }
            if(current.getInfo().isOperand){
                if(!current.getLink().getInfo().toString().equals(" ")) {
                    x = (current.getInfo().toString());
                    current = current.getLink();
                    y = (current.getInfo().toString());
                    x = x+y;
                    newToken = new Token(x);
                    System.out.printf("%-10s", newToken);
                    operandStack.push(newToken);
                }else {
                    System.out.printf("%-10s", current.getInfo());
                    operandStack.push(current.getInfo());
                }
                System.out.printf("%-10s%-10s%-10s", " ", " ", " ");
                printStack(operandStack);
                System.out.println();
            }

            if(current.getInfo().isOperator | current.getInfo().isEx){
                System.out.printf("%-10s", current.getInfo() );
                operand2 = Double.parseDouble(operandStack.pop().toString());
                operand1 = Double.parseDouble(operandStack.pop().toString());
                System.out.printf("%-10.0f", operand1 );
                System.out.printf("%-10.0f", operand2 );
                if(current.getInfo().toString().equals("*")){
                    double result = operand1*operand2;
                    Token tempToken = new Token(String.valueOf(result));
                    System.out.printf("%-10.0f", result );
                    operandStack.push(tempToken);
                }
                if(current.getInfo().toString().equals("/")){
                    double result = operand1/operand2;
                    Token tempToken = new Token(String.valueOf(result));
                    System.out.printf("%-10.0f", result );
                    operandStack.push(tempToken);
                }
                if(current.getInfo().toString().equals("+")){
                    double result = operand1+operand2;
                    Token tempToken = new Token(String.valueOf(result));
                    System.out.printf("%-10.0f", result );
                    operandStack.push(tempToken);
                }
                if(current.getInfo().toString().equals("-")){
                    double result = operand1-operand2;
                    Token tempToken = new Token(String.valueOf(result));
                    System.out.printf("%-10.0f", result );
                    operandStack.push(tempToken);
                }
                if(current.getInfo().toString().equals("^")){
                    double result = Math.pow(operand1,operand2);
                    Token tempToken = new Token(String.valueOf(result));
                    System.out.printf("%-10.0f", result );
                    operandStack.push(tempToken);
                }
                printStack(operandStack);
            }
            System.out.println();
            current = current.getLink();
        }
    }
    public static void main(String[] args)  {
    }
}
