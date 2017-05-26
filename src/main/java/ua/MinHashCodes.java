package ua;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class MinHashCodes {


    private static final List<Function<String, Integer>> functions = new ArrayList<>();

    private static final int function_count = 200;

    static {
        for (int i = 0; i < function_count; i++) {
            int seed = (int) Math.floor(Math.random() * function_count * function_count) + 32;
            System.out.println(seed);
            functions.add(getFunction(seed));
        }
    }

    private static Function<String, Integer> getFunction(int seed) {
        return string -> {
            int result = 1;
            for (int i = 0; i < string.length(); i++) {
                result = (seed * result + string.charAt(i));
            }
            return result;
        };
    }

    private static int find_min(List<String> set, Function<String, Integer> function) {
        int min = 0xFFFFFFFF;
        for (int i = 0; i < set.toArray().length; i++) {
            int hash = function.apply(set.get(i));
            if (hash < min) min = hash;
        }
        return min;
    }

    private static int[] signature(Set<String> set) {
        int[] result = new int[function_count];
        for (int i = 0; i < function_count; i++) {
            result[i] = find_min(new ArrayList(set), functions.get(i));
        }
        return result;
    }

    private static double similarity(int[] signA,  int[] signB) {
        int equal_count = 0;
        for (int i = 0; i < signA.length; ++i)
            if (signA[i] == signB[i]) ++equal_count;

        return 1.0 * equal_count / signA.length;
    }

    public static void main(String[] args) {
        Set<String> setA = new HashSet<>();
        setA.add("9329a332a44cba9dc17fbb8d42fdc9a9");
        setA.add("099447520a369898d907e293f8f3eccc");
        setA.add("108ed8e0dda0e965eb90137d16ff36d2");
        setA.add("64065253825b6dc1563121d2ca48f264");
        setA.add("1ddacca3325d7cb1acfee7313f83510e");
        setA.add("a48b85257f23dd1225d5b30445296013");
        setA.add("2e53e5eeeacc170a1663440bae09f618");
        setA.add("341d2a375fda864fa8fb984495baa9f8");
        setA.add("5c0129f105660fb6cc8b956dcf899de9");
        setA.add("1de0713b6e3ec72601bbbdbc83823004");
        setA.add("6bc1112f4d89a2fca7dc28e5a132a181");
        setA.add("6debbd33856a4697ed4836a06290b476");
        setA.add("f4c917adfd2114c47af1edd2c2db88cd");
        setA.add("ee05bae88c20ad302b82326711408320");
        setA.add("3ae0b632c4e9c2cafe1e7b4c054d110d");
        setA.add("058bcc24af8a9df326b3d43de1d7d487");
        setA.add("479e36d96f7d30ec9650e1546f25ab9c");
        setA.add("04211acf3c2641d743d7e8021c0b25dd");
        setA.add("161dfeed636dfa70b1d737717b1c1270");
        setA.add("208cd1769df0952c0d2f06dce1a693a8");
        setA.add("e051efb8ff66aebe1cf0ebdc2554ee89");
        setA.add("3d5b8e76306ea04d39a88f460213ab39");
        setA.add("05eb09cab6b23c27e7de37b4dcd42e79");
        setA.add("68f7f73866f38b35fb56585fee5cc473");
        setA.add("ba0a55710e4d4f49e340863bb38552e7");
        setA.add("06a6b77bb8138c96aaefa22b6e7a2e5a");
        setA.add("4c3d74497726b55768804703fa8d2ca3");
        setA.add("3e7e6834064befa188f91ffaa344fcdb");
        setA.add("c4f1e5bd5cb2042bf2af3113500449e4");
        setA.add("70db138a0ae9ab9ed93bf60710364250");
        setA.add("9c3cdbad34f7ede4cddc664a8c95135f");
        setA.add("c7c46dabbabfacc6e46b96f979ff3f37");
        setA.add("667490cad8710164c5f27fa5f86c4deb");
        setA.add("c1796fb5e7babb1bc9c1b73f07ef98a2");
        setA.add("446f2893d84856b926136b1f20073c94");
        setA.add("a46f88ce26370002a057fdcf2fb01cf0");
        setA.add("2329d319a758e56e0c744318760913b1");
        setA.add("b12761dff623abd30f6a11968f4387bc");
        setA.add("fa50c95de3ab5d8c8258e71d7c3ec965");
        setA.add("f264120baa4f40a1f6de00ef0d0d325b");
        setA.add("e4022bb52e55ffea49506f6693a85fa6");
        setA.add("160447333c353c28e548d90da2786112");
        setA.add("475fc1b68fc98be3cadedf5b43e9afcc");

        Set<String> setB = new HashSet<>();
        setB.add("apple");
        setB.add("peach");
        setB.add("elephant");
        setB.add("cat");
        setB.add("dog");
        setB.add("banana");

        int[] signA = signature(setA);
        int[] signB = signature(setB);

        System.out.println("Similarity: " + similarity(signA, signB));

    }

}
