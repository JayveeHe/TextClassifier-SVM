package Utils;

import java.io.UnsupportedEncodingException;

/**
 * 停用词
 *
 * @author Jayvee
 */
public class StopWords {
    TrieTree stopTree;

    public StopWords(String filepath) {
        try {
            this.stopTree = LoadDict(filepath);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public  TrieTree LoadDict(String filepath) throws UnsupportedEncodingException {
        String Stopwords = null;
        TrieTree st = new TrieTree();
        try {
            Stopwords = new String(FileUtils.File2byte(filepath),
                    "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String[] swords = new String[0];
        if (Stopwords != null) {
            swords = Stopwords.split(System.getProperty("line.separator"));
        }
        for (String w : swords) {
            st.addWord(new String(w.getBytes("utf-8"), "utf-8"));
        }
        System.out.println("停用词读取完毕，共读取" + swords.length + "个停用词");
        return st;
    }

    public boolean isStopword(String str) {
//        for (String s : stop_map.keySet()) {
//            if (s.equals(str)) {
//                return true;
//            }
//        }
        return stopTree.getWordNode(str)!=null;
    }

}
