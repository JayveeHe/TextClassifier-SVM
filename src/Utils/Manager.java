package Utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by Jayvee on 2014/8/14.
 */
public class Manager {
    public TrieTree root;
    public TrieTree stopTree;
    public StopWords stopWords;

    public Manager() {
        this.root = new TrieTree();
        this.stopWords = new StopWords("D:\\CS\\Java\\DataMining\\topic_model\\data\\stopwords.txt");
    }

    public void NostopCheckWord(String word) {
            root.addWord(word);
    }
    public void checkWord(String word) {
        if (!stopWords.isStopword(word)) {
            root.addWord(word);
        } else {
            System.out.println(word + "\t是停用词！");
        }
    }

    public void checkWord(String word, String nature) {
        if (!stopWords.isStopword(word)) {
            root.addWord(word, nature);
        } else {
            System.out.println(word + "\t是停用词！");
        }
    }
}
