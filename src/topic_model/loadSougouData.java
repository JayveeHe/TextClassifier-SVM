package topic_model;

import Utils.FileUtils;
import Utils.TrieTree;
import Utils.WordNode;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Jayvee on 2014/8/22.
 */
public class loadSougouData {
    public static void main(String[] args) {
        load();
    }

    public static void load() {
        TrieTree DFtree = new TrieTree();//用于存储所有的词语的DF值
        String filepath = "D:\\CS\\Java\\DataMining\\NLP\\sougou数据\\SogouC\\ClassFile";
        File fileroot = new File(filepath);
        int docCount = 0;
        for (String str : fileroot.list()) {//种类名遍历
            System.out.println("文件夹名：" + str);
            File contentFile = new File(filepath + "\\" + str);
            for (String txtFilename : contentFile.list()) {//用户名文件遍历
                //解析xml文件
                System.out.println(txtFilename);
                docCount++;
                try {
//                    File txtFile = new File(contentFile.getPath() + "\\" + txtFilename);
                    String txtStr = new String(FileUtils.File2byte(contentFile.getPath() + "\\" + txtFilename), "GBK");
//                    Document doc = Jsoup.parse(txtStr, "", Parser.xmlParser());
//                    Elements comments = doc.select("comment");
                    TrieTree svTree = new TrieTree();
//                    for (Element comment : comments) {
//                        Elements content = comment.select("content");
                    List<Term> parse = NlpAnalysis.parse(txtStr);
                    for (Term term : parse) {
                        if (svTree.getWordNode(term.getName()) == null) {
                            svTree.addWord(term.getName());//在监督树中添加该词，表示在该文档中已收录
                        }
                    }
//                        System.out.println(content.text());
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
        File output = new File("D:\\CS\\Java\\DataMining\\topic_model\\data\\sougouIDF值.txt");
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
}