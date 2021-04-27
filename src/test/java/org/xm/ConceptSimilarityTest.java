package org.xm;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.xm.Similarity;
import org.xm.similarity.util.DicReader;
import org.xm.similarity.util.SogouUtil;
import org.xm.similarity.util.StringUtil;
import org.xm.similarity.util.WordLibrary;
import org.xm.similarity.word.hownet.concept.Concept;
import org.xm.similarity.word.hownet.concept.ConceptSimilarity;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hzl
 */
public class ConceptSimilarityTest {

    public static void main(String[] args) throws IOException {

        List<String> inputList = Lists.newArrayList();
        inputList.add("查询");
        inputList.add("车");

        double score1 = test1(inputList);
        double score2 = test2(inputList);
        double score3 = test3(inputList);
        double score4 = test4(inputList);
        double score5 = test5(inputList);
        double score6 = test6(inputList);
        double score7 = test7(inputList);
        double score8 = test8(inputList);
        double score9 = test9(inputList);
        double score10 = test10(inputList);

        System.out.println("score1:" + score1);
        System.out.println("score2:" + score2);
        System.out.println("score3:" + score3);
        System.out.println("score4:" + score4);
        System.out.println("score5:" + score5);
        System.out.println("score6:" + score6);
        System.out.println("score7:" + score7);
        System.out.println("score8:" + score8);
        System.out.println("score9:" + score9);
        System.out.println("score10:" + score10);
    }

    public static double test1(List<String> inputList) throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(ConceptSimilarityTest.class.getResource("/").getPath() + "sogou/二人转词汇大全【官方推荐】.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        double score = scoreInner(inputList, keywordsList);
        return score;
    }

    public static double test2(List<String> inputList) throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(ConceptSimilarityTest.class.getResource("/").getPath() + "sogou/气象词汇大全【官方推荐】.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        double score = scoreInner(inputList, keywordsList);
        return score;
    }

    public static double test3(List<String> inputList) throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(ConceptSimilarityTest.class.getResource("/").getPath() + "sogou/王者荣耀【官方推荐】.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        double score = scoreInner(inputList, keywordsList);
        return score;
    }

    public static double test4(List<String> inputList) throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(ConceptSimilarityTest.class.getResource("/").getPath() + "sogou/围棋【官方推荐】.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        double score = scoreInner(inputList, keywordsList);
        return score;
    }

    public static double test5(List<String> inputList) throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(ConceptSimilarityTest.class.getResource("/").getPath() + "sogou/五金日用.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        double score = scoreInner(inputList, keywordsList);
        return score;
    }

    public static double test6(List<String> inputList) throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(ConceptSimilarityTest.class.getResource("/").getPath() + "sogou/西餐用语.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        double score = scoreInner(inputList, keywordsList);
        return score;
    }

    public static double test7(List<String> inputList) throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(ConceptSimilarityTest.class.getResource("/").getPath() + "sogou/象棋【官方推荐】.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        double score = scoreInner(inputList, keywordsList);
        return score;
    }

    public static double test8(List<String> inputList) throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(ConceptSimilarityTest.class.getResource("/").getPath() + "sogou/医生常用词汇.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        double score = scoreInner(inputList, keywordsList);
        return score;
    }

    public static double test9(List<String> inputList) throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(ConceptSimilarityTest.class.getResource("/").getPath() + "sogou/中医中药.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        double score = scoreInner(inputList, keywordsList);
        return score;
    }

    public static double test10(List<String> inputList) throws IOException {
        List<WordLibrary> wordLibraries = SogouUtil.getWordLibraries(ConceptSimilarityTest.class.getResource("/").getPath() + "sogou/开发大神专用词库【官方推荐】.scel");
        List<String> keywordsList = wordLibraries.stream().map(WordLibrary::getWord).collect(Collectors.toList());
        double score = scoreInner(inputList, keywordsList);
        return score;
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

    private static void getConcepts() {

        ConceptSimilarity conceptSimilarity = ConceptSimilarity.getInstance();

        Map<String, List<String>> keywordsMap;
        try {
            Class<?> conceptSimilarityClazz = Class.forName("org.xm.similarity.word.hownet.concept.ConceptSimilarity");

            Constructor constructor = conceptSimilarityClazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object conceptSimilarityObj = constructor.newInstance();
            Field concepts = conceptSimilarityClazz.getSuperclass().getDeclaredField("CONCEPTS");
            concepts.setAccessible(true);
            Multimap<String, Concept> conceptsMultimap = (Multimap<String, Concept>) concepts.get(conceptSimilarityObj);
            Map<String, Collection<Concept>> collectionMap = conceptsMultimap.asMap();
            System.out.println(collectionMap);

            keywordsMap = conceptsMultimap.asMap().entrySet().stream().collect(Collectors.toMap(entry -> StringUtil.toString(entry.getKey()),
                    entry -> Lists.newArrayList(((Concept) ((Collection) (entry.getValue())).iterator().next()).getSecondSememes())));

            System.out.println(keywordsMap);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void testData() {
        double s1 = Similarity.conceptSimilarity("车", "白楼");
        double s2 = Similarity.conceptSimilarity("车", "暴");
        double s3 = Similarity.conceptSimilarity("车", "安禄山");
        double s4 = Similarity.conceptSimilarity("车", "安井");
        double s5 = Similarity.conceptSimilarity("车", "独轮车");
        double s6 = Similarity.conceptSimilarity("车", "东阴宫");
        double s7 = Similarity.conceptSimilarity("车", "马");
        double s8 = Similarity.conceptSimilarity("车", "华佗痔疮");
        double s9 = Similarity.conceptSimilarity("车", "艾");
        double s10 = Similarity.conceptSimilarity("车", "常");


    }
}
