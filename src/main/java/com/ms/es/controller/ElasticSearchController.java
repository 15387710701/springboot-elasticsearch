package com.ms.es.controller;

import com.ms.es.domain.UserEntity;
import com.ms.es.utils.ElasticSearchUtils;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * ElasticSearch控制器
 *
 * @author 154594742@qq.com
 * @date 2021/3/5 10:02
 */

// @Api(tags = "ElasticSearch控制器")
@RestController
@RequestMapping("elasticSearch")
public class ElasticSearchController {

    @Autowired
    private ElasticSearchUtils elasticSearchUtils;

    /**
     * 新增索引
     *
     * @param index 索引
     * @return ResponseVo
     */
    //@ApiOperation("新增索引")
    @PostMapping("index")
    public void createIndex(String index) throws IOException {
        elasticSearchUtils.createIndex(index);
    }

    /**
     * 索引是否存在
     *
     * @param index index
     * @return ResponseVo
     */
    @GetMapping("index/{index}")
    public void existIndex(@PathVariable String index) throws IOException {
         elasticSearchUtils.isIndexExist(index);
    }

    /**
     * 删除索引
     *
     * @param index index
     * @return ResponseVo
     */
    @DeleteMapping("index/{index}")
    public void deleteIndex(@PathVariable String index) throws IOException {
        elasticSearchUtils.deleteIndex(index);
    }


    /**
     * 新增/更新数据
     *
     * @param entity 数据
     * @param index  索引
     * @param esId   esId
     * @return ResponseVo
     */
    //@ApiOperation("新增/更新数据")
    @PostMapping("data")
    public void submitData(UserEntity entity, String index, String esId) throws IOException {
        elasticSearchUtils.submitData(entity, index, esId);
    }

    /**
     * 通过id删除数据
     *
     * @param index index
     * @param id    id
     * @return ResponseVo
     */
    //@ApiOperation("通过id删除数据")
    @DeleteMapping("data/{index}/{id}")
    public String deleteDataById(@PathVariable String index, @PathVariable String id) throws IOException {
       return elasticSearchUtils.deleteDataById(index, id);
    }

    /**
     * 通过id查询数据
     *
     * @param index  index
     * @param id     id
     * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
     * @return ResponseVo
     */
    //@ApiOperation("通过id查询数据")
    @GetMapping("data")
    public void searchDataById(String index, String id, String fields) throws IOException {
        elasticSearchUtils.searchDataById(index, id, fields);
    }

    /**
     * 分页查询（这只是一个demo）
     *
     * @param index index
     * @return ResponseVo
     */
    //@ApiOperation("分页查询")
    @GetMapping("data/page")
    public List<Map<String, Object>> selectPage1(String index) throws IOException {
        //构建查询条件
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //精确查询
        //boolQueryBuilder.must(QueryBuilders.wildcardQuery("name", "张三"));
        // 模糊查询
        boolQueryBuilder.filter(QueryBuilders.wildcardQuery("name", "张"));
        // 范围查询 from:相当于闭区间; gt:相当于开区间(>) gte:相当于闭区间 (>=) lt:开区间(<) lte:闭区间 (<=)
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("age").from(18).to(32));
        SearchSourceBuilder query = new SearchSourceBuilder();
        query.query(boolQueryBuilder);
        //需要查询的字段，缺省则查询全部
        String fields = "";
        //需要高亮显示的字段
        String highlightField = "name";
        if (StringUtils.hasText(fields)) {
            //只查询特定字段。如果需要查询所有字段则不设置该项。
            query.fetchSource(new FetchSourceContext(true, fields.split(","), Strings.EMPTY_ARRAY));
        }
        //分页参数，相当于pageNum
        Integer from = 0;
        //分页参数，相当于pageSize
        Integer size = 2;
        //设置分页参数
        query.from(from);
        query.size(size);

        //设置排序字段和排序方式，注意：字段是text类型需要拼接.keyword
        //query.sort("age", SortOrder.DESC);
        query.sort("name" + ".keyword", SortOrder.ASC);
        return elasticSearchUtils.searchListData(index, query, highlightField);
    }

    @GetMapping("data/page1")
    public List<Map<String, Object>> selectPage(String index,String name,String sortField) throws IOException {
        //构建查询条件
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        SearchSourceBuilder query = new SearchSourceBuilder();
        boolQueryBuilder.filter(QueryBuilders.wildcardQuery("name", name));
        query.query(boolQueryBuilder);
        if (!StringUtils.isEmpty(sortField)){
            query.sort(sortField,SortOrder.ASC);
        }

        //需要查询的字段，缺省则查询全部

        //需要高亮显示的字段
        String highlightField = "name";

        return elasticSearchUtils.searchListData(index, query, highlightField);
    }
}
