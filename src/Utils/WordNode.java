package Utils;

import java.util.HashMap;

/**
 * Created by Jayvee on 2014/8/14.
 */
public class WordNode {
    protected int freq;//词频
    public int documentFreq;//文档频率
    protected int precount;//前缀词数目
    protected WordNode parent;
    protected HashMap<Character, WordNode> childs;
    protected String word;
    protected char ch;

    public String getNature() {
        return nature;
    }

    protected String nature;//词性
    public Object obj;//待定的对象信息
    public double tfidf;//TFIDF值

    /**
     * 创建子节点
     *
     * @param parent
     * @param character
     */
    WordNode(WordNode parent, char character) {
        this.parent = parent;
        this.childs = new HashMap<Character, WordNode>();
        this.word = parent.word + character;
        this.ch = character;
        this.freq = 0;
        this.documentFreq = 0;
        this.precount = parent.word.length();
    }

    /**
     * 创建根节点专用的构造函数
     */
    WordNode() {
        this.parent = null;
        this.childs = new HashMap<Character, WordNode>();
        this.freq = 0;
        this.documentFreq = 0;
        this.precount = 0;
        this.word = "";
    }


    protected WordNode addChild(char character) {
        if (!this.childs.containsKey(character)) {
            WordNode child = new WordNode(this, character);
            this.childs.put(character, child);
            return child;
        } else {
            return this.childs.get(character);
        }
    }

    public String getWord() {
        return this.word;
    }

    public int getFreq() {
        return this.freq;
    }

    @Override
    public String toString() {
        return "word=" + word + "\tfreq=" + freq + "\tnature=" + nature;
    }
}
