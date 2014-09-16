package topic_model;

import Utils.FileUtils;
import Utils.IDFCaculator;
import Utils.TrieTree;
import Utils.WordNode;
import libsvm.*;

import java.io.*;

import static topic_model.calSougouTFIDF.NEWLINE;
import static topic_model.calSougouTFIDF.getMetaVec;

/**
 * Created by Jayvee on 2014/8/24.
 */
public class testSVM {
    public static void main(String[] args) throws IOException, InterruptedException {
//        File root = new File("D:\\CS\\Java\\DataMining\\NLP\\sougou数据\\SogouC\\ClassFile");
//        int j = 1;
//
//        for (File file : root.listFiles()) {
//            System.out.println(j + ":" + file.getName());
//            j++;
//        }


        String text = FileUtils.File2str("./data/sample.txt", "utf-8");
        predict(text);
//        predict("【12日零时至14日5时国家体育场北路管制】#社会管理#国际汽联电动方程式赛车世界锦标赛定于9月12日（星期五）、13日（星期六）在北京奥林匹克公园举行。自9月12日零时开始至9月14日5时止，交管部门将对国家体育场北路采取交通管制措施。途经国家体育场北路的机动车、非机动车和行人可绕行大屯路。");

//        String[] scaleArgs = {"-s",
//                "D:\\CS\\Java\\DataMining\\topic_model\\data\\sougouC_TF-IDF_VecData_v200_range.txt",
//                "D:\\CS\\Java\\DataMining\\topic_model\\data\\sougouC_TF-IDF_VecData_v200_Scaled.txt"};
//        svm_scale ss = new svm_scale();
//        ss.main(scaleArgs);
//        String[] rescaleArgs = {"-l","0",
//                "-r","D:\\CS\\Java\\DataMining\\topic_model\\data\\svm_data_range.txt",
//                "D:\\CS\\Java\\DataMining\\topic_model\\data\\svm_data.txt"};
//        ss.main(rescaleArgs);

//        训练模型
//        String[] trainArgs = {"-c", "128", "-g", "0.001953125", "D:\\CS\\Java\\DataMining\\topic_model\\data\\sougouC_TF-IDF_VecData_v200_Scaled.txt",
//                "D:\\CS\\Java\\DataMining\\topic_model\\data\\sougouC_TF-IDF_VecData_v200_Scaled_model.txt"};//directory of training file
//        svm_train st = new svm_train();
//        st.main(trainArgs);
////
//
//        svm_scale ss = new svm_scale();
//        String[] rescaleArgs = {"-l","0",
//                "-r","D:\\CS\\Java\\DataMining\\topic_model\\data\\svm_data_range.txt",
//                "D:\\CS\\Java\\DataMining\\topic_model\\data\\svm_data.txt"};
//        ss.main(rescaleArgs);


//        String[] preArgs = {"D:\\CS\\Java\\DataMining\\topic_model\\data\\svm_data_test.txt",
//                "D:\\CS\\Java\\DataMining\\topic_model\\data\\svm_data_model.txt",
//                "D:\\CS\\Java\\DataMining\\topic_model\\data\\svm_data_result.txt"};
//        svm_predict sp = new svm_predict();
//        sp.main(preArgs);


//        svm sv = new svm();
//        svm_model sm = svm.svm_load_model("D:\\CS\\Java\\DataMining\\topic_model\\data\\sougouC_TF-IDF_VecData_v200_Scaled_model.txt");
//
//
//

    }

    public static void predict(String text) throws IOException, InterruptedException {
//        String tet = FileUtils.File2str("./data/sample.txt", "utf-8");
//        double type = svm.svm_predict(sm, testPredic(tet));
        testPredic(text);
        String[] preArgs = {"./data/testPreData.txt",
                "./data/sougouC_TF-IDF_VecData_v200_Scaled_model.txt",
                "./data/testPreResult.txt"};
        svm_predict sp = new svm_predict();
        sp.main(preArgs);
        //输出分类结果
//        System.out.println(type);
        String result = FileUtils.File2str("./data/testPreResult.txt", "utf-8");
        int typeID = (int) (Double.valueOf(result) / 1);
        System.out.println(typeID);
        String type = "";
        switch (typeID) {
            case 1:
                type = "IT";
                break;
            case 2:
                type = "体育";
                break;
            case 3:
                type = "健康";
                break;
            case 4:
                type = "军事";
                break;
            case 5:
                type = "教育";
                break;
            case 6:
                type = "杂谈";
                break;
            case 7:
                type = "旅游";
                break;
            case 8:
                type = "汽车";
                break;
            case 9:
                type = "职场";
                break;
            case 10:
                type = "财经";
                break;
            default:
                type = "未知";
                break;
        }
        System.out.println("测试文本的类别为：" + type);
    }

    public static svm_node[] testPredic(String text) throws IOException, InterruptedException {
        IDFCaculator idfCaculator = new IDFCaculator("./data/IDF值.txt");
        TrieTree resultTree = new TrieTree();
        FileOutputStream fos = new FileOutputStream(new File("./data/testData.txt"));
        resultTree = idfCaculator.CalTFIDF2Tree(text);
        TrieTree metaVecTree = getMetaVec("./data/sougouTF-IDF分类排行.txt");
        int index = 1;
        svm_node node = new svm_node();
        String resultLine = "0";
        for (WordNode wn : metaVecTree.word_list) {
            //若在result中有某个向量词，则取之
            WordNode wordnode = resultTree.getWordNode(wn.getWord());
            if (null != wordnode) {
                float tfidf = (float) wordnode.tfidf;
                resultLine = resultLine + "\t" + index + ":" + tfidf;
                node.index = index;
                node.value = tfidf;
            }
//                    else {
//                        resultLine = resultLine + "\t" + index + ":" + 0;
//                    }
            index++;
        }
        fos.write((resultLine + "\n").getBytes("GBK"));
        //将测试的原始数据与训练集的原始数据合并，并进行缩放
        String trainingData = FileUtils.File2str("./data/sougouC_TF-IDF_VecData_v200_Reduced.txt", "utf-8");
        fos.write(trainingData.getBytes("GBK"));
        fos.close();

//        svm_scale ss = new svm_scale();
//        String[] scaleArgs = {"-r","D:\\CS\\Java\\DataMining\\topic_model\\data\\sougouC_TF-IDF_VecData_v200_range.txt",
//                "D:\\CS\\Java\\DataMining\\topic_model\\data\\testData.txt"};
        //调用cmd命令，使用svm_scale.exe进行数据缩放
        String cmdStr = "cmd /c D:\\CS\\Java\\支持库\\libsvm-3.18\\libsvm-3.18\\windows\\svm-scale.exe -l 0 -u 1" +
                " D:\\CS\\Git\\topic_model\\data\\testData.txt" +
                ">D:\\CS\\Git\\topic_model\\data\\testPreData.txt";
        Process p = Runtime.getRuntime().exec(cmdStr);
        //挂起等待进程执行结束
        p.waitFor();
        //释放文本占用
        p.destroy();
//        Runtime.getRuntime().exit(0);
        //将缩放后的结果第一行（待测试数据）提取出来
        String data = (FileUtils.File2str("./data/testPreData.txt", "GBK").split(NEWLINE))[0];
        FileOutputStream foss = new FileOutputStream(new File("./data/testPreData.txt"));
        foss.write((data).getBytes("GBK"));
        return new svm_node[]{node};
    }


}
