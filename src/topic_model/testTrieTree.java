package topic_model;

import Utils.TrieTree;
import Utils.WordNode;

import java.util.ArrayList;

/**
 * Created by Jayvee on 2014/8/13.
 */
public class testTrieTree {
    public static void main(String[] args) {
        TrieTree tt = new TrieTree();
        tt.addWord("我们工人有力量！");
        tt.addWord("我们");
        tt.addWord("你们");
        tt.addWord("你们");
        tt.addWord("你们");
        tt.addWord("我们");
        ArrayList<WordNode> al = tt.getSortedList(TrieTree.downSortor);
        for (WordNode wn : al) {
            System.out.println(wn);
        }
    }
}
