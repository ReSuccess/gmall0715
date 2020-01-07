package com.atguigu.gmall0715.gmalllistservice.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall0715.bean.SkuLsInfo;
import com.atguigu.gmall0715.bean.SkuLsParams;
import com.atguigu.gmall0715.bean.SkuLsResult;
import com.atguigu.gmall0715.service.ListService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sujie
 * @date 2020-01-06-20:13
 */
@Service
public class ListServiceImpl implements ListService {
    /**
     * es 中的 index 相当于 数据库名
     */
    private static final String ES_INDEX = "gmall";
    /**
     *  es 中的 type 相当于 表名
     */
    private static final String ES_TYPE = "SkuInfo";

    @Autowired
    JestClient jestClient;

    @Override
    public void saveSkuLsInfo(SkuLsInfo skuLsInfo) {
        //动作，增加数据
        Index index = new Index.Builder(skuLsInfo).index(ES_INDEX).type(ES_TYPE).id(skuLsInfo.getId()).build();
        try {
            //执行
            jestClient.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SkuLsResult search(SkuLsParams skuLsParams) {
        //dsl
        String query = makeQueryStringForSearch(skuLsParams);
        //动作 查询
        Search build = new Search.Builder(query).addIndex(ES_INDEX).addType(ES_TYPE).build();
        //获取结果集
        SearchResult searchResult = null;
        try {
            searchResult = jestClient.execute(build);
            //返回结果集
            return makeResultForSearch(searchResult,skuLsParams);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回结果集
        return null;
    }

    /**
     * 根据 SearchResult 获取结果集
     * @param searchResult
     * @param skuLsParams
     * @return
     */
    private SkuLsResult makeResultForSearch(SearchResult searchResult, SkuLsParams skuLsParams) {
        SkuLsResult skuLsResult = new SkuLsResult();
        // 保存商品的！ skuLsInfoList
        List<SkuLsInfo> skuLsInfoList = new ArrayList<>();
        List<SearchResult.Hit<SkuLsInfo, Void>> hits = searchResult.getHits(SkuLsInfo.class);
        if(hits != null && hits.size() > 0){
            for (SearchResult.Hit<SkuLsInfo, Void> hit : hits) {
                SkuLsInfo skuLsInfo = hit.source;
                //判断高亮，如果是keyword则显示高亮
                if(hit.highlight != null && hit.highlight.size() > 0){
                    List<String> skuNames = hit.highlight.get("skuName");
                    //因为只有一个所以取第一个即可
                    String skuName = skuNames.get(0);
                    skuLsInfo.setSkuName(skuName);
                }
                //添加到集合
                skuLsInfoList.add(skuLsInfo);
            }
        }
        skuLsResult.setSkuLsInfoList(skuLsInfoList);
        //获取记录数
        skuLsResult.setTotal(searchResult.getTotal());
        //获取总页数
        Long totalPages = (searchResult.getTotal() + skuLsParams.getPageSize() - 1 )/skuLsParams.getPageSize();
        skuLsResult.setTotalPages(totalPages);
        // 平台属性值Id集合 显示平台属性，平台属性值
        List<String> attrValueIdList = new ArrayList<>();
        TermsAggregation termsAggregation = searchResult.getAggregations().getTermsAggregation("groupby_attr");
        List<TermsAggregation.Entry> buckets = termsAggregation.getBuckets();
        if(buckets != null && buckets.size() > 0 ){
            for (TermsAggregation.Entry bucket : buckets) {
                String vauleId = bucket.getKey();
                attrValueIdList.add(vauleId);
            }
        }
        skuLsResult.setAttrValueIdList(attrValueIdList);
        return skuLsResult;
    }

    /**
     * 根据用户查询条件生成dsl语句
     * @param skuLsParams
     * @return
     */
    private String makeQueryStringForSearch(SkuLsParams skuLsParams) {
        // 定义查询器 { }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // {query --- bool }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 按照三级分类Id 过滤
        if(skuLsParams.getCatalog3Id() != null && skuLsParams.getCatalog3Id().length()>0){
            // filter --- term {"term": {"catalog3Id": "61"}}
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",skuLsParams.getCatalog3Id());
            boolQueryBuilder.filter(termQueryBuilder);
        }
        // 判断平台属性值Id 过滤
        if(skuLsParams.getValueId() != null && skuLsParams.getValueId().length > 0){
            // {"term":{ "skuAttrValueList.valueId": "81"}}
            for (String valueId : skuLsParams.getValueId() ) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",valueId);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }
        // 判断是否有skuName --- keyword
        if(skuLsParams.getKeyword() != null && skuLsParams.getKeyword().length() > 0){
            // bool -- must
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",skuLsParams.getKeyword());
            boolQueryBuilder.must(matchQueryBuilder);
            // 设置高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.preTags("<span style=color:red>");
            highlightBuilder.postTags("</span>");
            highlightBuilder.field("skuName");
            searchSourceBuilder.highlight(highlightBuilder);
        }
        //过滤
        searchSourceBuilder.query(boolQueryBuilder);
        // 排序
        searchSourceBuilder.sort("hotScore", SortOrder.DESC);
        // 分页
        //  int from = skuLsParams.getPageSize()
        int from = skuLsParams.getPageSize()*(skuLsParams.getPageNo() - 1);
        searchSourceBuilder.from(from);
        //默认20页
        searchSourceBuilder.size(skuLsParams.getPageSize());
        // 聚合
        /*
          "aggs": {
                "groupby_attr": {
                  "terms": {
                    "field": "skuAttrValueList.valueId"
                  }
            }
         */
        TermsBuilder termsBuilder = AggregationBuilders.terms("groupby_attr").field("skuAttrValueList.valueId");
        searchSourceBuilder.aggregation(termsBuilder);

        String query = searchSourceBuilder.toString();
        System.out.println("query:" + query);
        return query;
    }

}
