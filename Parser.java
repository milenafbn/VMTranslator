
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    List<String[]> commands;
    private int currentPosition;

    Parser (String input) {
        final String eol = System.getProperty("line.separator");
        var output = input.split(eol);
        commands = Arrays.stream(output)
        .map(String::strip)
        .filter(  (s) ->  s.indexOf("//") != 0 && s != "")
        //.map ( (s) -> s.substring(0, s.indexOf("//")) )
        .map ( (s) ->s.split(" ")  )
        .collect(Collectors.toList());

    }

    public String[] command() {
        return commands.remove(0);
    }

    public boolean hasMoreCommands() {
        return !commands.isEmpty();
    }

    public void advance() {
        if (hasMoreCommands()) {
            commands.remove(0);
        }
    }

    public Command.Type commandType() {
        var command = commands.get(currentPosition)[0];
        if (command.equals("add")) {
            return Command.Type.ADD;
        } else if (command.equals("sub")) {
            return Command.Type.SUB;
        } else if (command.equals("neg")) {
            return Command.Type.NEG;
        } else if (command.equals("eq")) {
            return Command.Type.EQ;
        } else if (command.equals("gt")) {
            return Command.Type.GT;
        } else if (command.equals("lt")) {
            return Command.Type.LT;
        } else if (command.equals("and")) {
            return Command.Type.AND;
        } else if (command.equals("or")) {
            return Command.Type.OR;
        } else if (command.equals("not")) {
            return Command.Type.NOT;
        } else if (command.equals("push")) {
            return Command.Type.PUSH;
        } else if (command.equals("pop")) {
            return Command.Type.POP;
        } else {
            throw new UnsupportedOperationException("Invalid command type: " + command);
        }
    }

    public String arg1() {
        var command = commands.get(currentPosition)[0];
        if (command.equals("return")) {
            throw new UnsupportedOperationException("arg1() should not be called for return command.");
        }
        if (command.equals("add") || command.equals("sub") || command.equals("neg") ||
                command.equals("eq") || command.equals("gt") || command.equals("lt") ||
                command.equals("and") || command.equals("or") || command.equals("not")) {
            return command;
        } else {
            return commands.get(currentPosition)[1];
        }
    }

    public int arg2() {
        var command = commands.get(currentPosition)[0];
        if (command.equals("push") || command.equals("pop") || command.equals("function") || command.equals("call")) {
            return Integer.parseInt(commands.get(currentPosition)[2]);
        } else {
            throw new UnsupportedOperationException("arg2() should not be called for this command type: " + command);
        }
    }
}