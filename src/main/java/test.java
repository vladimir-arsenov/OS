import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class test {
    public static void main(String[] args) throws IOException {
        String s = """
                [a, a, a]
                [s, s, s]
                [Buffer: [a, a, a, s, s, s], Buffer: [a, a, a, s, s, s]]
                """;
        System.out.println(Arrays.toString(s.replace("\n", "").split("Buffer:.*]")));
    }
}
