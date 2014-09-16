package Utils;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jayvee on 2014/8/15.
 */
public class IDFCaculator {
    public static void main(String[] args) {
        IDFCaculator idfCaculator = new IDFCaculator("D:\\CS\\Java\\DataMining\\topic_model\\data\\IDF值.txt");
        String text = FileUtils.File2str("./data/sample.txt", "utf-8");
        ArrayList<WordNode> test = idfCaculator.CalTFIDF(text);
        for (WordNode wn : test) {
            if (wn.getNature().equals("n") || wn.getNature().equals("vn"))
                System.out.println(wn.getWord() + "\ttfidf=" + wn.tfidf);
        }
    }

    static int docCount = 0;
    public TrieTree IDFtree;

    public IDFCaculator(String filepath) {
        this.IDFtree = loadIDFtree(filepath);
    }

    public static void loadContent2IDF() {
        TrieTree DFtree = new TrieTree();//用于存储所有的词语的DF值
        String filepath = "D:\\CS\\Java\\DataMining\\微博消息数据集（197810条）\\617821\\weibodata";
        File fileroot = new File(filepath);
        int docCount = 0;
        for (String str : fileroot.list()) {//种类名遍历
            System.out.println("文件夹名：" + str);
            File contentFile = new File(filepath + "\\" + str + "\\content");
            for (String txtFilename : contentFile.list()) {//用户名文件遍历
                //解析xml文件
                docCount++;
                try {
//                    File txtFile = new File(contentFile.getPath() + "\\" + txtFilename);
                    String txtStr = new String(FileUtils.File2byte(contentFile.getPath() + "\\" + txtFilename), "GBK");
                    Document doc = Jsoup.parse(txtStr, "", Parser.xmlParser());
                    Elements comments = doc.select("comment");
                    TrieTree svTree = new TrieTree();
                    for (Element comment : comments) {
                        Elements content = comment.select("content");
                        List<Term> parse = NlpAnalysis.parse(content.text());
                        for (Term term : parse) {
                            if (svTree.getWordNode(term.getName()) == null) {
                                svTree.addWord(term.getName());//在监督树中添加该词，表示在该文档中已收录
                            }
                        }
//                        System.out.println(content.text());
                    }
                    //单个文件统计完毕，进行整体DF统计更新
                    for (WordNode wordNode : svTree.word_list) {
                        DFtree.addWord(wordNode.getWord());

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("统计完毕！");
        //所有文件统计完毕，进行IDF计算和文档输出
        File output = new File("D:\\CS\\Java\\DataMining\\topic_model\\data\\IDF值.txt");
        try {
            FileOutputStream fos = new FileOutputStream(output);
            for (WordNode wordNode : DFtree.word_list) {
                double IDF = Math.log(docCount / (wordNode.getFreq() + 1));
                String text = wordNode.getWord() + "\t" + IDF + System.getProperty("line.separator");
                fos.write(text.getBytes("utf-8"));
            }
            System.out.println("输出完毕！路径：" + output.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TrieTree loadIDFtree(String IDFpath) {
        String IDFtext = null;
        TrieTree IDFtree = new TrieTree();
        try {
            IDFtext = new String(FileUtils.File2byte(IDFpath), "utf-8");
            String[] terms = IDFtext.split(System.getProperty("line.separator"));
            for (String term : terms) {
                String[] word = term.split("\t");
                if (word.length == 2) {
                    WordNode wn = IDFtree.addWord(word[0]);
                    wn.obj = Double.valueOf(word[1]);//存入IDF值
                } else System.out.println(term);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return IDFtree;
    }

    /**
     * 给定的一段文本，返回相应的包含TF-IDF值的wordNode数组.
     *
     * @param text
     */
    public ArrayList<WordNode> CalTFIDF(String text) {
        TrieTree TFIDFtree = new TrieTree();//用于存储TF-IDF信息的树
        List<Term> terms = NlpAnalysis.parse(text);//进行分词
        int wordCount = terms.size();
        for (Term term : terms)//进行词频统计
        {
            TFIDFtree.addWord(term.getName(), term.natrue().natureStr);
        }
        double tfidf = 0;
        ArrayList<WordNode> result = new ArrayList<WordNode>();
        for (WordNode wn : TFIDFtree.word_list) {
//            System.out.println((double) IDFtree.getWordNode(wn.getWord()).obj);
            if (IDFtree.getWordNode(wn.getWord()) != null)//语料IDF数据库中存在该词
            {
                double idf = (Double) IDFtree.getWordNode(wn.getWord()).obj;
//                if (idf != 0) {
//                    System.out.println(wn.getWord());
//                }
                wn.tfidf = (double) wn.getFreq() / wordCount * idf;
            } else {
                wn.tfidf = (double) wn.getFreq() / wordCount * (double) Math.log(400);
            }
            result.add(wn);
        }
        Collections.sort(result, TrieTree.TFIDF_dowmSortor);
        return result;
    }


    /**
     * 给定的一段文本，返回相应的包含TF-IDF值的wordNode树.
     *
     * @param text
     */
    public TrieTree CalTFIDF2Tree(String text) {
        TrieTree TFIDFtree = new TrieTree();//用于存储TF-IDF信息的树
        List<Term> terms = NlpAnalysis.parse(text);//进行分词
        int wordCount = terms.size();
        for (Term term : terms)//进行词频统计
        {
            TFIDFtree.addWord(term.getName(), term.natrue().natureStr);
        }
        double tfidf = 0;
//        ArrayList<WordNode> result = new ArrayList<WordNode>();
        //开始计算TF-IDF值
        for (WordNode wn : TFIDFtree.word_list) {
//            System.out.println((double) IDFtree.getWordNode(wn.getWord()).obj);
            if (IDFtree.getWordNode(wn.getWord()) != null)//语料IDF数据库中存在该词
            {
                double idf = (Double) IDFtree.getWordNode(wn.getWord()).obj;
//                if (idf != 0) {
//                    System.out.println(wn.getWord());
//                }
                wn.tfidf = (double) wn.getFreq() / wordCount * idf;
            } else {
                wn.tfidf = (double) wn.getFreq() / wordCount * (double) Math.log(400);
            }
//            result.add(wn);
        }
//        Collections.sort(result, TrieTree.TFIDF_dowmSortor);
        return TFIDFtree;
    }

}
