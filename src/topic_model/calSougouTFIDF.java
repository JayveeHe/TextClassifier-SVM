package topic_model;

import Utils.*;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jayvee on 2014/9/5.
 */
public class calSougouTFIDF {
    static String NEWLINE = System.getProperty("line.separator");

    //计算每个类别下每个子文件的TFIDF向量
    public static void main(String[] args) throws IOException {
        File root = new File("D:\\CS\\Java\\DataMining\\NLP\\sougou数据\\SogouC\\ClassFile");
        IDFCaculator idfCaculator = new IDFCaculator("D:\\CS\\Java\\DataMining\\topic_model\\data\\IDF值.txt");
        TrieTree resultTree = new TrieTree();
        TrieTree metaVecTree = getMetaVec("D:\\CS\\Java\\DataMining\\topic_model\\data\\sougouTF-IDF分类排行.txt");
        File output = new File("./data/sougouC_TF-IDF_VecData_Reduced.txt");
        FileOutputStream fos = new FileOutputStream(output);
        FileOutputStream classIDfos = new FileOutputStream(new File("./data/sougouC_TF-IDF_ClassID.txt"));
        if (null != output && null != fos) {
            System.out.println("output文件创建完毕，路径：" + output.getAbsolutePath());
        }
        //写入向量头
//        fos.write("序号：".getBytes("utf-8"));
//        for (WordNode nn : metaVecTree.word_list) {
//            fos.write((nn.getWord() + "\t").getBytes("utf-8"));
//        }
//        System.out.println("向量头填写完毕");
        //处理每个类别下的单个文件，计算他们各自的单词TF-IDF值
        int label = 1;
        for (File classfolder : root.listFiles()) {
            String className = classfolder.getName();
            System.out.println("正在读取    " + className);
            classIDfos.write((label + ":" + className + NEWLINE).getBytes("utf-8"));//写入类别名
            for (File txtFile : classfolder.listFiles()) {
                String txt = FileUtils.File2str(txtFile.getAbsolutePath(), "gbk");
                resultTree = idfCaculator.CalTFIDF2Tree(txt);
                String txtNo = txtFile.getName().replaceAll(".txt", "");
                System.out.println(className+"\t" + txtNo);
//                String resultLine = txtNo + ":";//获取文件序号
                String resultLine = label + "";
                int index = 1;
                for (WordNode wn : metaVecTree.word_list) {
                    //若在result中有某个向量词，则取之
                    WordNode wordnode = resultTree.getWordNode(wn.getWord());
                    if (null != wordnode) {
                        float tfidf = (float) wordnode.tfidf;
                        resultLine = resultLine + "\t" + index + ":" + tfidf;
                    }
//                    else {
//                        resultLine = resultLine + "\t" + index + ":" + 0;
//                    }
                    index++;
                }
                resultLine = resultLine + NEWLINE;
                fos.write(resultLine.getBytes("utf-8"));
            }
            label++;
        }
        System.out.println("输出完毕");
    }

    //获取特征值单位向量
    public static TrieTree getMetaVec(String filepath) {
        String txt = FileUtils.File2str(filepath, "utf-8");
//        String[] classTexts = txt.split("\\r\\n\\r\\n");//划分所有子类
        TrieTree metaVecTree = new TrieTree();
        //提取类别名
//        for (String text : classTexts) {
        Matcher m = Pattern.compile("[\\u4e00-\\u9fa5].*\\tT").matcher(txt);
        while (m.find()) {
            String tep = m.group();
            tep = tep.replaceAll("\\tT", "");
//            System.out.println("检测到的特征值——————\t"+tep);
//            null == metaVecTree.getWordNode(tep)&&
            if (tep.length()>1) {//排除重复的特征值
//                System.out.println("选择的特征值：\t"+tep);
                metaVecTree.addWord(tep, 0);
            }
        }
        System.out.println("共选择"+metaVecTree.word_list.size()+"个特征值");
        return metaVecTree;


    }

}
