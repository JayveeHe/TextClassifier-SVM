package Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * 单词管理器，存储了停用词表和已检测词表
 *
 * @author Jayvee
 */
public class WordsManager {
    Map<String, word> word_map;
    // Map<String, String> stop_map;
    StopWords stopWords;
    static Comparator<word> upSortor = new Comparator<WordsManager.word>() {

        @Override
        public int compare(word o1, word o2) {
            // TODO Auto-generated method stub
            return o1.freq - o2.freq;
        }
    };
    static Comparator<word> downSortor = new Comparator<WordsManager.word>() {

        @Override
        public int compare(word o1, word o2) {
            // TODO Auto-generated method stub
            return o2.freq - o1.freq;
        }
    };

    /**
     * 单词管理器的构造函数
     *
     * @param stopWords 停用词表
     */
    public WordsManager(StopWords stopWords) {
        // this.stop_map = stop_map;
        word_map = new HashMap<String, word>();
        this.stopWords = stopWords;
    }

    /**
     * 检查一个词是否在已存储的Map中，并做相应的处理
     *
     * @param str
     */
    public void checkWord(String str) {
        if (!stopWords.isStopword(str)) {
            if (word_map.containsKey(str)&&!str.equals(" ")&&str.length()>1) {
                word w = word_map.get(str);
                w.freq++;
                word_map.put(str, w);
            } else {
                word w = new word(str);
                word_map.put(str, w);
            }
        }
//        else
//            System.out.println(str);
    }

    public void deleteWord(String str) {
        if (word_map.containsKey(str)) {
            word_map.remove(str);
        } else {
            System.out.println("deleteWord异常，没有找到该词。");
        }
    }

    public ArrayList<word> sortWord() {
        ArrayList<word> sorted_list = new ArrayList<WordsManager.word>();
        for (word w : this.word_map.values()) {
            sorted_list.add(w);
        }
        // Collections.sort(sorted_list);
        Collections.sort(sorted_list, WordsManager.downSortor);
        System.out.println("排序完成！");
        return sorted_list;
    }

    public Map<String, word> getWordsMap() {
        return word_map;
    }

    public class word implements Comparable<word> {

        String word;
        String property;
        int freq;

        public word(String str) {
            this.word = str;
            this.freq = 1;
            this.property = null;
        }

        public String getWord() {
            return word;
        }

        public String getProperty() {
            return property;
        }

        @Override
        public int compareTo(word o) {

            return o.freq - this.freq;
        }

        @Override
        public String toString() {
            return "word [word=" + word + ", property=" + property + ", freq="
                    + freq + "]";
        }

    }
}
