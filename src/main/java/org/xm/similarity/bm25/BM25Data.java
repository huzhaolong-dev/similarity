package org.xm.similarity.bm25;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName BM25Data
 * @Description BM25相似度计算的数据结构
 * @Author Hu
 * @Date 2021/4/25 15:09
 * @Version 1.0
 **/
public class BM25Data<ID, E> {

    private final List<E> termList;

    private final Map<ID, List<E>> docMap;

    /**
     * 第n个term，在termList中的出现频次数
     */
    private Map<Integer, Integer> queryFreqMap = new HashMap<>();

    /**
     * 第n个term，在每个文档中出现的频次数
     */
    private Map<Integer, Map<ID, Integer>> termEveryDocFreqMap = new HashMap<>();

    /**
     * 第n个term，文档命中条数
     */
    private Map<Integer, Integer> docFreqMap = new HashMap<>();

    /**
     * 每个文档的单词数（长度）
     */
    private Map<ID, Integer> termDocLengthMap = new HashMap<>();

    /**
     * 文档的平均单词数（长度）
     */
    private float averageTermDocLength;

    /**
     * 文档总数
     */
    private int docCount;

    public BM25Data(List<E> termList, Map<ID, List<E>> docMap) {
        if (Objects.isNull(termList)) {
            throw new IllegalArgumentException("termList must not null");
        }
        if (Objects.isNull(docMap)) {
            throw new IllegalArgumentException("docMap must not null");
        }
        this.termList = termList;
        this.docMap = docMap;
        calIndex();
    }

    /**
     * 计算相关的指标
     */
    private void calIndex() {
        boolean isCalAverageTermDocLength = false; //是否计算过averageTermDocLength
        for (int i = 0; i < termList.size(); i++) {
            E term = termList.get(i);

            //---------计算queryFreqMap开始------------
            int queryFreqCount = 0;
            for (E e :
                    termList) {
                if (term == e || term.equals(e)) {
                    queryFreqCount++;
                }
            }
            queryFreqMap.put(i, queryFreqCount);
            //---------计算queryFreqMap结束------------

            //---------计算termDocFreqMap、docFreqMap开始------------
            int termTotalDocFreq = 0; //所有文档中的频次数
            int docFreq = 0;
            int tempTermTotalDcoFreq;
            Map<ID, Integer> everyDocFreqMap = Maps.newHashMap();
            int sumTermDocLength = 0;
            for (Map.Entry<ID, List<E>> entry :
                    docMap.entrySet()) {
                tempTermTotalDcoFreq = termTotalDocFreq;
                ID docMapKey = entry.getKey();
                List<E> docMapValue = entry.getValue();

                if (!isCalAverageTermDocLength) {
                    sumTermDocLength = sumTermDocLength + (docMapValue != null ? docMapValue.size() : 0);
                    termDocLengthMap.put(docMapKey, docMapValue != null ? docMapValue.size() : 0);
                }
                if (docMapValue == null) {
                    continue;
                }
                for (E e :
                        docMapValue) {
                    if (term == e || term.equals(e)) {
                        termTotalDocFreq++;
                    }
                }
                everyDocFreqMap.put(docMapKey, termTotalDocFreq - tempTermTotalDcoFreq);
                if (termTotalDocFreq > tempTermTotalDcoFreq) {
                    docFreq++;
                }
            }
            termEveryDocFreqMap.put(i, everyDocFreqMap);
            docFreqMap.put(i, docFreq);
            //---------计算termDocFreqMap、docFreqMap结束------------
            if (!isCalAverageTermDocLength) {
                averageTermDocLength = (float) (sumTermDocLength / (double) docMap.size());
                isCalAverageTermDocLength = true;
            }
        }

        docCount = docMap.size();
    }

    public List<E> getTermList() {
        return termList;
    }

    public Map<ID, List<E>> getDocMap() {
        return docMap;
    }

    public Map<Integer, Integer> getQueryFreqMap() {
        return queryFreqMap;
    }

    public Map<Integer, Map<ID, Integer>> getTermEveryDocFreqMap() {
        return termEveryDocFreqMap;
    }

    public Map<Integer, Integer> getDocFreqMap() {
        return docFreqMap;
    }

    public Map<ID, Integer> getTermDocLengthMap() {
        return termDocLengthMap;
    }

    public float getAverageTermDocLength() {
        return averageTermDocLength;
    }

    public int getDocCount() {
        return docCount;
    }
}
