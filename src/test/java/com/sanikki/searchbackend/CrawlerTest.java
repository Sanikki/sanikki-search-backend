package com.sanikki.searchbackend;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sanikki.searchbackend.model.entity.Picture;
import com.sanikki.searchbackend.model.entity.Post;
import com.sanikki.searchbackend.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.lang.Console.log;

/**
 * @ClassName CrawlerTest
 * @Description: TODO
 * @Author: Sanikki
 * @Date: 2024/5/11
 */
@SpringBootTest
public class CrawlerTest {
    @Autowired
    private PostService postService;

    @Test
    void testFetchPassage() {
        // 1. 获取数据源
        String url = "https://api.code-nav.cn/api/post/search/page/vo";
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"随笔\",\"tags\":[\"随笔\"],\"reviewStatus\":1}";
        String result2 = HttpRequest
                .post(url)
                .body(json)
                .execute().body();
        // 2. json转对象
        Map<String, Object> map = JSONUtil.toBean(result2,Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> postList = new ArrayList<>();
        for (Object record: records) {
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            postList.add(post);
        }
        boolean b = postService.saveBatch(postList);
        Assertions.assertTrue(b);
        System.out.println(postList);
    }

    @Test
    void testFetchPicture() throws IOException {
        int current = 1;
        String url = String.format("https://cn.bing.com/images/search?q=鹿目圆&first=" + current);
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictures = new ArrayList<>();
        for (Element element : elements) {
            // 取图片地址
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictures.add(picture);
        }
        System.out.println(pictures);
    }
}

