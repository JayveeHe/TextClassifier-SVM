package Utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by Jayvee on 2014/8/14.
 */
public class TFIDF_Utils {
    TrieTree dictTree;
    TrieTree wordTree;


    public TFIDF_Utils() {
        dictTree = new TrieTree();
        wordTree = new TrieTree();
    }

    public void loadDict(String filepath) {
        String text;
        try {
            text = new String(FileUtils.File2byte(filepath), "uft-8");
            String[] terms = text.split(System.getProperty("line.separator"));
            for (String str : terms) {
                String[] term = str.split("\t");
                dictTree.addWord(term[0], term[1], Integer.valueOf(term[2]));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("字词库读取完毕");
    }

//    public void
}
