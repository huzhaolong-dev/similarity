package org.xm.similarity.bm25;

import com.google.common.collect.Lists;
import org.xm.ConceptSimilarityTest;
import org.xm.Similarity;
import org.xm.similarity.bm25.BM25Data;
import org.xm.similarity.bm25.BM25Similarity;
import org.xm.similarity.util.SogouUtil;
import org.xm.similarity.util.StringUtil;
import org.xm.similarity.util.WordLibrary;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hzl
 */
public class BM25SimilarityTest {
    public static void main(String[] args) throws IOException {
        BM25Similarity bm25Similarity = new BM25Similarity();
        BM25Data<String, List<String>> bm25Data = new BM25Data(getTermList(), getDocMap());

        Map<String, Float> scoreMap = bm25Similarity.scoreMap(bm25Data);
        for (Map.Entry<String, Float> entry :
                scoreMap.entrySet()) {
            System.out.println(String.format("scoreMap{[key:%s],[value:%s]}", entry.getKey(), entry.getValue()));
        }

        Map.Entry<String, Float> maxScoreEle = bm25Similarity.maxScoreEle(bm25Data);
        System.out.println(String.format("[key:%s],[value:%s]", maxScoreEle.getKey(), maxScoreEle.getValue()));

    }

    public static List<String> getTermList() {
        List<String> inputList = Lists.newArrayList();
        inputList.add("查询");
        inputList.add("车");
        return inputList;
    }

    public static Map<String, List<String>> getDocMap() throws IOException {
        Map<String, List<String>> docMap = new HashMap<>();
        docMap.put("二人转词汇大全【官方推荐】", keywordsList1());
        docMap.put("气象词汇大全【官方推荐】", keywordsList2());
        docMap.put("王者荣耀【官方推荐】", keywordsList3());
        docMap.put("围棋【官方推荐】", keywordsList4());
        docMap.put("五金日用", keywordsList5());
        docMap.put("西餐用语", keywordsList6());
        docMap.put("象棋【官方推荐】", keywordsList7());
        docMap.put("医生常用词汇", keywordsList8());
        docMap.put("中医中药", keywordsList9());
        docMap.put("开发大神专用词库【官方推荐】", keywordsList10());
        return docMap;
    }

    public static List<String> keywordsList1() throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(BM25SimilarityTest.class.getResource("/").getPath() + "sogou/二人转词汇大全【官方推荐】.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        return keywordsList;
    }

    public static List<String> keywordsList2() throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(BM25SimilarityTest.class.getResource("/").getPath() + "sogou/气象词汇大全【官方推荐】.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        return keywordsList;
    }

    public static List<String> keywordsList3() throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(BM25SimilarityTest.class.getResource("/").getPath() + "sogou/王者荣耀【官方推荐】.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        return keywordsList;
    }

    public static List<String> keywordsList4() throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(BM25SimilarityTest.class.getResource("/").getPath() + "sogou/围棋【官方推荐】.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        return keywordsList;
    }

    public static List<String> keywordsList5() throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(BM25SimilarityTest.class.getResource("/").getPath() + "sogou/五金日用.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        return keywordsList;
    }

    public static List<String> keywordsList6() throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(BM25SimilarityTest.class.getResource("/").getPath() + "sogou/西餐用语.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        return keywordsList;
    }

    public static List<String> keywordsList7() throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(BM25SimilarityTest.class.getResource("/").getPath() + "sogou/象棋【官方推荐】.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        return keywordsList;
    }

    public static List<String> keywordsList8() throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(BM25SimilarityTest.class.getResource("/").getPath() + "sogou/医生常用词汇.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        return keywordsList;
    }

    public static List<String> keywordsList9() throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(BM25SimilarityTest.class.getResource("/").getPath() + "sogou/中医中药.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        return keywordsList;
    }

    public static List<String> keywordsList10() throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(BM25SimilarityTest.class.getResource("/").getPath() + "sogou/开发大神专用词库【官方推荐】.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        return keywordsList;
    }

    /**
     * @param inputList  输入的拆分词List
     * @param keywordsList 同一组关键词List
     * @return
     */
    public static double scoreInner(List<String> inputList, List<String> keywordsList) {
        double result = 0;

        if (StringUtil.isBlank(inputList) || StringUtil.isBlank(keywordsList)) {
            return result;
        }
        int statisticsCount = (int) Math.ceil(inputList.size() / 2d); //取一半作为统计值
        Deque<Double> inputScoreDeque = new LinkedList();
        for (String input :
                inputList) {
            double maxScore = 0;
            String maxScoreWord = "";
            for (String keyword :
                    keywordsList) {
                double calScore = Similarity.conceptSimilarity(input, keyword);
                if (calScore > maxScore) {
                    maxScoreWord = keyword;
                }
                maxScore = calScore > maxScore ? calScore : maxScore;
            }
            System.out.println("maxScore: " + maxScore + ";maxScoreWord: " + maxScoreWord);

            Double firstScore = inputScoreDeque.peekFirst();
            if (firstScore == null) { //队列中没有值，加入队列
                inputScoreDeque.addFirst(maxScore);
                continue;
            }
            if (firstScore.compareTo(maxScore) < 0) { //sum计算值比之前存在的大，添加到头部
                inputScoreDeque.addFirst(maxScore);
            } else { //否则添加到尾部
                inputScoreDeque.addLast(maxScore);
            }

        }

        for (int i = 0; i < statisticsCount; i++) {
            result = result + inputScoreDeque.pop();
        }

        return result;
    }
}
