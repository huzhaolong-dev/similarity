package org.xm.similarity.bm25;

import com.google.common.collect.Lists;

import java.util.*;

/**
 * @ClassName BM25Similarity
 * @Description BM25相似度算法
 * @Author Hu
 * @Date 2021/4/25 14:46
 * @Version 1.0
 **/
public class BM25Similarity {

    private final float k1;

    private final float k2;

    private final float b;

    public BM25Similarity(float k1, float k2, float b) {
        if (Float.isFinite(k1) == false || k1 < 0) {
            throw new IllegalArgumentException("illegal k1 value: " + k1 + ", must be a non-negative finite value");
        }
        if (Float.isFinite(k2) == false || k2 < 0) {
            throw new IllegalArgumentException("illegal k2 value: " + k2 + ", must be a non-negative finite value");
        }

        if (Float.isNaN(b) || b < 0 || b > 1) {
            throw new IllegalArgumentException("illegal b value: " + b + ", must be between 0 and 1");
        }
        this.k1 = k1;
        this.k2 = k2;
        this.b  = b;
    }

    public BM25Similarity() {
        this(1.2f, 200f, 0.75f);
    }

    /**
     * @param docFreq 文档命中的条数
     * @param docCount 文档总数
     * @return
     */
    protected float idf(int docFreq, int docCount) {
        return (float) Math.log(1 + (docCount - docFreq + 0.5f) / (docFreq + 0.5f));
    }

    /**
     * @param dl 文档分词长度
     * @param avgdl 文档平均长度
     * @return
     */
    protected float k(int dl, float avgdl) {
        return k1 * ((1 - b) + b * dl / avgdl);
    }

    /**
     * @param termDocFreq 查询词在一条分词文档中命中的频次
     * @param queryFreq 查询词在查询分词中的频次
     * @param dl 文档分词长度
     * @param avgdl 文档平均长度
     * @return
     */
    protected float s(int termDocFreq, int queryFreq, int dl, float avgdl) {
        return ((k1 + 1) * termDocFreq / (k(dl, avgdl) + termDocFreq)) * ((k2 + 1) * queryFreq / (k2 + queryFreq));
    }

    /**
     * 计算每个文档的得分
     * @param bm25Data
     * @param <ID>
     * @param <E>
     * @return
     */
    protected <ID, E> Map<ID, Float> scoreMap(BM25Data<ID, E> bm25Data) {
        Map<ID, Float> scoreMap = new HashMap<>();
        List<?> termList = bm25Data.getTermList();
        Map<ID, List<E>> docMap = bm25Data.getDocMap();
        Map<Integer, Integer> queryFreqMap = bm25Data.getQueryFreqMap();
        Map<Integer, Map<ID, Integer>> termEveryDocFreqMap = bm25Data.getTermEveryDocFreqMap();
        Map<Integer, Integer> docFreqMap = bm25Data.getDocFreqMap();
        Map<ID, Integer> termDocLengthMap = bm25Data.getTermDocLengthMap();
        float averageTermDocLength = bm25Data.getAverageTermDocLength();
        int docCount = bm25Data.getDocCount();
        for (Map.Entry<ID, List<E>> docEntry :
                docMap.entrySet()) {
            float sumScore = 0f;
            ID docEntryKey = docEntry.getKey();
            for (int i = 0; i < termList.size(); i++) {
                sumScore = sumScore + idf(docFreqMap.get(i), docCount) * s(termEveryDocFreqMap.get(i).get(docEntryKey),
                        queryFreqMap.get(i), termDocLengthMap.get(docEntryKey), averageTermDocLength);
            }
            scoreMap.put(docEntryKey, sumScore);
        }
        return scoreMap;
    }

    /**
     * 获取计算后的最大得分的文档
     * @param bm25Data
     * @param <ID>
     * @param <E>
     * @return
     */
    public <ID, E> Map.Entry<ID, Float> maxScoreEle(BM25Data<ID, E> bm25Data) {
        List<Map.Entry<ID, Float>> entryList = Lists.newLinkedList(scoreMap(bm25Data).entrySet());
        Collections.sort(entryList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        return entryList.get(0);
    }
}
