package topic_model;

import Spider.RateSpider;
import Utils.IDFCaculator;
import Utils.TrieTree;
import Utils.WordNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ITTC-Jayvee on 2014/9/16.
 */
public class testTaobao {
    public static void main(String[] a) {
//        RateSpider.getRateByURL()
    }

    public static void processRates(String URL) {
        //获取商品评价列表（JSON形式）
        JSONObject root = null;
        try {
            root = RateSpider.getRateByURL(URL, 50);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //计算各评价文本的TFIDF排行
        JSONArray rateList = root.optJSONArray("comments");
        if (null != rateList) {
            for (int i = 0; i < rateList.length(); i++) {
                JSONObject rate = null;
                try {
                    rate = (JSONObject) rateList.get(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String content = null;
//                if (null != rate) {
                content = rate.optString("content");
//                }
                IDFCaculator idfCaculator = new IDFCaculator("./data/IDF值.txt");
                ArrayList<WordNode> wordNodes = idfCaculator.CalTFIDF(content);

            }
        }
    }
}
