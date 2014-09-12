package Utils;

import java.util.*;

/**
 * Created by Jayvee on 2014/8/1.
 */
public class TrieTree {
    protected final static int SUCCESS = 1;
    protected final static int FAIL = -1;

    private WordNode root;
    public ArrayList<WordNode> word_list;

    public static Comparator<WordNode> upSortor = new Comparator<WordNode>() {

        @Override
        public int compare(WordNode o1, WordNode o2) {
            // TODO Auto-generated method stub
            return o1.freq - o2.freq;
        }
    };

    public static Comparator<WordNode> downSortor = new Comparator<WordNode>() {

        @Override
        public int compare(WordNode o1, WordNode o2) {
            // TODO Auto-generated method stub
            return o2.freq - o1.freq;
        }
    };

    public static Comparator<WordNode> TFIDF_dowmSortor = new Comparator<WordNode>() {
        @Override
        public int compare(WordNode o1, WordNode o2) {
            if (o2.tfidf - o1.tfidf > 0) {
                return 1;
            } else if (o2.tfidf - o1.tfidf == 0) {
                return 0;
            } else {
                return -1;
            }
        }
    };


    public TrieTree() {
        this.root = new WordNode();
        this.word_list = new ArrayList<WordNode>();
    }

    public WordNode addWord(String word) {
        WordNode wn = root;
        for (int i = 0; i < word.length(); i++) {
            wn = wn.addChild(word.charAt(i));
        }
        wn.freq++;
        if (wn.freq == 1) {
            word_list.add(wn);
        }
//        System.out.println(wn.word + "add完毕！");
        return wn;
    }

    public void addWord(String word, int freq) {
        WordNode wn = root;
        for (int i = 0; i < word.length(); i++) {
            wn = wn.addChild(word.charAt(i));
        }
        wn.freq++;
        if (wn.freq == 1) {
            word_list.add(wn);
        }
        wn.freq = freq;
//        System.out.println(wn.word + "add完毕！");
    }


    public void addWord(String word, String nature, int freq) {
        WordNode wn = root;
        for (int i = 0; i < word.length(); i++) {
            wn = wn.addChild(word.charAt(i));
        }
        wn.freq++;
        if (wn.freq == 1) {
            word_list.add(wn);
        }
        wn.freq = freq;
        wn.nature = nature;
//        System.out.println(wn.word + "add完毕！");
    }

    public void addWord(String word, String nature) {
        WordNode wn = root;
        for (int i = 0; i < word.length(); i++) {
            wn = wn.addChild(word.charAt(i));
        }
        wn.freq++;
        if (wn.freq == 1) {
            word_list.add(wn);
        }
        wn.nature = nature;
//        System.out.println(wn.word + "add完毕！");
    }

    /**
     * 获取某个待查单词的wordnode,如果在TrieTree中有则返回改节点，否则为null;
     *
     * @param word 待查单词
     * @return 代表该单词的WordNode
     */
    public WordNode getWordNode(String word) {
        WordNode wn = root;
        for (int i = 0; i < word.length(); i++) {
            if (!wn.childs.containsKey(word.charAt(i))) {
                return null;
            }
            wn = wn.childs.get(word.charAt(i));
        }
        if (wn.freq != 0) {
            return wn;
        } else {
            return null;
        }
    }


    public void deleteWord(String word) {
        WordNode wn = getWordNode(word);
        if (wn.childs.size() == 0) {
            wn.parent.childs.remove(wn.ch);//若为叶子节点，则母节点清除该叶子节点
        } else {
            wn.freq = 0;//词频清零代表删除该词
        }
    }

    public ArrayList<WordNode> getSortedList(Comparator comparator) {
        Collections.sort(this.word_list, TrieTree.downSortor);
        return word_list;
    }


}
