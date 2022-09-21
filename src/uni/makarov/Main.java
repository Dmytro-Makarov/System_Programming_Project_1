package uni.makarov;

import java.io.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    /*
    Завдання В.4:
    Знайти слова в файлі, що були розмежовані символами
    Слова не мають бути довші за 30 символів
    Вивести тільки ті слова, що мають не повторюванні літери
    */

    //Checks if words have repeating letters
    public static boolean checkIfRepeatingLetters(String str) {
        //Takes out hyphen and uppercase letters before splitting by characters
        Map<String, Long> map = Arrays.stream(str.replaceAll("-", "").toLowerCase(Locale.ROOT).split(""))
                .collect(Collectors.groupingBy(Function.identity(),
                        Collectors.counting()));
        return map.values().stream().anyMatch(count -> count > 1);
    }

    public static void main(String[] args) {
        ArrayList<String> checkedWords = new ArrayList<>();

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("UnfilteredText.txt"));
            String read = null;
            while ((read = in.readLine()) != null) {
                //Divide by spaces
                String[] splited = read.split("\\s+");
                for (String part : splited) {
                    //Check if the word is a dash to avoid deleting hyphens
                    if (part.equals("-")){
                        continue;
                    }
                    else {
                        String filteredWord = Normalizer.normalize(part, Normalizer.Form.NFD);          //Replace accent letters (ex. 'è')
                        filteredWord = filteredWord.replaceAll("[^a-zA-Z0-9-]", "");    //Only leave alpha-numeric letters and hyphen
                        filteredWord = filteredWord.substring(0, Math.min(filteredWord.length(), 30));  //Trim words
                        if(filteredWord.equals("")){    //Skip if word is empty
                            continue;
                        }
                        else {
                            if(checkedWords.contains(filteredWord)){    //Skip if word was already checked
                                continue;
                            }
                            else if (checkIfRepeatingLetters(filteredWord)){    //Skip if word has repeating letters
                                continue;
                            } else {
                                checkedWords.add(filteredWord);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("There was a problem: " + e);
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }

        System.out.println(checkedWords);
    }
}
