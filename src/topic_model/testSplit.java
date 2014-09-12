package topic_model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utils.*;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class testSplit {

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
        }
        // StopWords.LoadDict();
    }

    public static void testDemo() throws JSONException, UnsupportedEncodingException {
        long time = System.currentTimeMillis();
        // JiebaSegmenter segmenter = new JiebaSegmenter();
        // time = System.currentTimeMillis() - time;
        // System.out.println("读取词典用时：" + time);
        // String[] sentences = new String[] {
        // "这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。",
        // "雷猴回归人间。", "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作",
        // "结果婚的和尚未结过婚的" };
//        WordsManager words = new WordsManager(new StopWords());
        Manager manager = new Manager();
        String text = FileUtils.File2str("D:\\CS\\Java\\DataMining\\topic_model\\data\\Roudan.txt", "utf-8");
        JSONTokener tokener = new JSONTokener(text);
        JSONObject root;
        root = (JSONObject) tokener.nextValue();
        String username = (String) root.get("username");
        JSONArray statuses = root.getJSONArray("statuses");
        for (int i = 0; i < statuses.length(); i++) {
            JSONObject s = statuses.getJSONObject(i);
            boolean isRepost = s.getBoolean("isRepost");
            String str;
            if (isRepost) {
                str = s.getString("source_text") + s.getString("repost_reason");
            } else {
                str = s.getString("oringin_text");
            }
            // 处理掉 // @人名 [表情] 和无意义的“转发微博”
            Matcher matcher = Pattern
                    .compile("(@[\\u4e00-\\u9fa5]+ )|(//@[\\u4e00-\\u9fa5]+:)|(\\[[\\u4e00-\\u9fa5]+\\])|(转发微博)|(微博)").matcher(str);
            if (matcher.find()) {
//                String tmp = matcher.group();
//                System.out.println("去掉的词："+tmp);
                str = matcher.replaceAll(" ");
                Matcher tmp = Pattern.compile("微博").matcher(str);
                if (tmp.find()) {
                    System.out.println(tmp.group());
                }
//                str = matcher.group();
            }
            List<Term> parse = NlpAnalysis.parse(str);
            new NatureRecognition(parse).recognition();
            for (Term term : parse) {
                System.out.println(term.getName() + "词性=" + term.getNatureStr());
                if (term.getNatureStr().equals("nw") || term.getNatureStr().equals("n"))
                    manager.checkWord(term.getName(), term.getNatureStr());
                // System.out.println();
            }
        }
        ArrayList<WordNode> sorted = manager.root.getSortedList(TrieTree.downSortor);

        System.out.println(sorted);
        System.out.println(username + "的微博中出现次数最多的词汇：");
        for (int i = 0; i < 10; i++) {
            System.out.println(sorted.get(i).getWord());
        }

    }
}
