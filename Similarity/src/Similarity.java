import java.text.DecimalFormat;
import java.util.*;
import static java.lang.Math.sqrt;

public class Similarity {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter text 1:");
        String text1 = scanner.nextLine();
        System.out.println();

        System.out.println("Enter text 2:");
        String text2 = scanner.nextLine();
        System.out.println();

        double cosineSimilarity = findCosineSimilarity(text1, text2);

        DecimalFormat df;

        if(cosineSimilarity != 0){
            df = new DecimalFormat(".##%");
            System.out.println("The similarity is: " + df.format(cosineSimilarity));
        }
        else{
            System.out.println("The similarity is: 0%");
        }
    }

    public static double findCosineSimilarity(String text1, String text2){

        String[] tokenized1 = tokenize(text1);
        String[] tokenized2 = tokenize(text2);

        Map<String, Integer> tf1 = findTermFrequency(tokenized1);
        Map<String, Integer> tf2 = findTermFrequency(tokenized2);

        Collection<Integer> tfVals1 = tf1.values();
        Collection<Integer> tfVals2 = tf2.values();

        double magA = findMagnitude(tfVals1);
        double magB = findMagnitude(tfVals2);

        List<List<Integer>> map = combineTF(tf1, tf2);

        long numerator = findVectorDotProduct(map.get(0), map.get(1));

        double denominator = magA * magB;

        double cosineSimilarity = numerator / denominator;

        return cosineSimilarity;
    }

    public static String[] tokenize(String text){

        // Remove non-alphanumeric
        text = text.replaceAll("[^A-Za-z0-9]", " ");

        // Whitespace delimited
        String[] textArray = text.split("\\s+");

        return textArray;
    }

    public static double findMagnitude(Collection<Integer> tfs){

        int sum = 0;

        for(int tf : tfs){

            sum += tf * tf;
        }

        return(sqrt(sum));
    }

    public static long findVectorDotProduct(List<Integer> list1, List<Integer> list2){

        long vectorDotProduct = 0;

        for(int i = 0; i < list1.size(); i++){
            int x = list1.get(i);
            int y = list2.get(i);
            vectorDotProduct += x * y;
        }

        return vectorDotProduct;
    }

    public static Map<String, Integer> findTermFrequency(String[] terms){

        Map<String, Integer> termFreqs = new HashMap<>();

        for(String term : terms){

            if(termFreqs.containsKey(term)){

                int currentFreq = termFreqs.get(term);

                termFreqs.put(term, currentFreq + 1);
            }
            else{
                termFreqs.put(term, 1);
            }
        }
        return termFreqs;
    }

    public static List<List<Integer>> combineTF(Map<String, Integer> termFreqs1, Map<String, Integer> termFreqs2){

        Map<String, Integer> allTerms = new HashMap<>();
        allTerms.putAll(termFreqs1);
        allTerms.putAll(termFreqs2);

        List<List<Integer>> columns = new ArrayList<>();
        List<Integer> column1 = new ArrayList<>();
        List<Integer> column2 = new ArrayList<>();

        for(String key : allTerms.keySet()){
            if(termFreqs1.containsKey(key) && termFreqs2.containsKey(key)) {
                int val1 = termFreqs1.get(key);
                int val2 = termFreqs2.get(key);
                column1.add(val1);
                column2.add(val2);
            }
            else if(termFreqs1.containsKey(key)) {
                int val1 = termFreqs1.get(key);
                column1.add(val1);
                column2.add(0);
            }
            else{ //termFreqs2.containsKey(key){
                int val2 = termFreqs2.get(key);
                column1.add(0);
                column2.add(val2);
            }
        }

        columns.add(column1);
        columns.add(column2);
        return columns;
    }
}
