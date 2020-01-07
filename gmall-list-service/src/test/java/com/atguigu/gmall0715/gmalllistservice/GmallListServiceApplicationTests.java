package com.atguigu.gmall0715.gmalllistservice;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallListServiceApplicationTests {
    @Autowired
    private JestClient jestClient;

    @Test
    public void testES() throws IOException {
        /**
         * 1.定义dsl语句
         * 2.定义执行的动作
         * 3.执行
         * 4.获取结果
         */
        String query = "{\n" +
                "  \"query\": {\n" +
                "    \"match\": {\n" +
                "      \"actorList.name\": \"zhang yi\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        Search build = new Search.Builder(query).addIndex("movie_index").addType("movie").build();
        //执行
        SearchResult execute = jestClient.execute(build);
        // 获取数据
        List<SearchResult.Hit<Map, Void>> hits = execute.getHits(Map.class);
        for (SearchResult.Hit<Map, Void> hit : hits) {
            Map map = hit.source;
            System.out.println(map);
        }

    }

}
