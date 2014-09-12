package topic_model;

import Utils.*;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.json.JSONException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class testTFIDF {

    public static void main(String[] args) {
//        List<Term> parse = NlpAnalysis.parse("柳州的柳江河特别清澈，壮哉我大北邮！");
//        System.out.println(parse);
        try {
            testDemo();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // StopWords.LoadDict();
    }

    public static void testDemo() throws JSONException, IOException {
        long time = System.currentTimeMillis();
        // JiebaSegmenter segmenter = new JiebaSegmenter();
        // time = System.currentTimeMillis() - time;
        // System.out.println("读取词典用时：" + time);
        // String[] sentences = new String[] {
        // "这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。",
        // "雷猴回归人间。", "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作",
        // "结果婚的和尚未结过婚的" };
//        WordsManager words = new WordsManager(new StopWords());
        String filepath = "D:\\CS\\Java\\DataMining\\NLP\\sougou数据\\SogouC\\ClassFile";
        File fileroot = new File(filepath);
        int docCount = 0;
        IDFCaculator idfCaculator = new IDFCaculator("D:\\CS\\Java\\DataMining\\topic_model\\data\\sougouIDF值.txt");

        File output = new File("./data/sougeTF-IDF分类排行");
        FileOutputStream fos = new FileOutputStream(output);

        for (String str : fileroot.list()) {//种类名遍历
            System.out.println("文件夹名：" + str);
            File contentFile = new File(filepath + "\\" + str);
            String classText = "";
            TrieTree TFtree = new TrieTree();
            int wordCount = 0;
            for (String txtFilename : contentFile.list()) {//用户名文件遍历
                //解析xml文件
                docCount++;
                try {
//                    File txtFile = new File(contentFile.getPath() + "\\" + txtFilename);
                    String txtStr = new String(FileUtils.File2byte(contentFile.getPath() + "\\" + txtFilename), "GBK");
                    List<Term> terms = NlpAnalysis.parse(txtStr);
                    new NatureRecognition(terms).recognition();
                    wordCount += terms.size();
                    for (Term term : terms) {
                        if (term.getNatureStr().matches("n"))
                            TFtree.addWord(term.getName(), term.natrue().natureStr);
                    }

//                    Document doc = Jsoup.parse(txtStr, "", Parser.xmlParser());
//                    Elements comments = doc.select("comment");
//                    for (Element comment : comments) {
//                        Elements content = comment.select("content");
//                        List<Term> parse = NlpAnalysis.parse(content.text());
//                        for (Term term : parse) {
//                            if (svTree.getWordNode(term.getName()) == null) {
//                                svTree.addWord(term.getName());//在监督树中添加该词，表示在该文档中已收录
//                            }
//                        }
//                    classText = classText + "\r\n" + txtStr;
//                        System.out.println(content.text());
//                    }
                    //单个文件统计完毕，进行整体DF统计更新

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            //进行TF-IDF值计算
//            ArrayList<WordNode> idfList = idfCaculator.CalTFIDF(classText);
            double tfidf = 0;
            ArrayList<WordNode> idfList = new ArrayList<WordNode>();
            for (WordNode wn : TFtree.word_list) {
//            System.out.println((double) IDFtree.getWordNode(wn.getWord()).obj);
                if (idfCaculator.IDFtree.getWordNode(wn.getWord()) != null)//语料IDF数据库中存在该词
                {
                    double idf = (Double) idfCaculator.IDFtree.getWordNode(wn.getWord()).obj;
//                if (idf != 0) {
//                    System.out.println(wn.getWord());
//                }
                    wn.tfidf = (double) wn.getFreq() / wordCount * idf;
                } else {
                    wn.tfidf = (double) wn.getFreq() / wordCount * (double) Math.log(80000);
                }
                idfList.add(wn);
            }
            Collections.sort(idfList, TrieTree.TFIDF_dowmSortor);
            String text = str + "的TF-IDF排行：\n";
            System.out.println(str + "的TF-IDF排行：");
            for (int i = 0; i < 200; i++) {
                text = text + idfList.get(i).getWord() + "\tTF-IDF= " + idfList.get(i).tfidf + "\tNature= " + idfList.get(i).getNature() + "\n";
                System.out.println(idfList.get(i).getWord() + "\tTF-IDF= " + idfList.get(i).tfidf + "\tNature= " + idfList.get(i).getNature());
            }
            System.out.println("\n");
            fos.write(text.getBytes("utf-8"));
        }
        System.out.println("********************************\n统计完毕！");

    }
}
