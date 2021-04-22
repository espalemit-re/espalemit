import com.offcn.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by travelround on 2019/2/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-solr.xml")
public class TestTemplate {

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testAdd(){
        TbItem item = new TbItem();
        item.setId(1L);
        item.setBrand("华为");
        item.setCategory("手机");
        item.setGoodsId(1L);
        item.setSeller("华为2号专卖店");
        item.setTitle("华为Mate9");
        item.setPrice(new BigDecimal(2000));

        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }
    @Test
    public void testFindOne(){
        TbItem byId = solrTemplate.getById(1, TbItem.class);
        System.out.println(byId.getTitle());
    }

    @Test
    public void  testDelete(){
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    @Test
    public void testAddList(){
        List<TbItem> list =new ArrayList<>();
        for (int i = 0 ; i < 100 ;i++){
            TbItem item = new TbItem();
            item.setId(i + 1L);
            item.setBrand("华为");
            item.setCategory("手机");
            item.setGoodsId(1L);
            item.setSeller("华为2号专卖店");
            item.setTitle("华为Mate" + i);
            item.setPrice(new BigDecimal(2000 + i));
            list.add(item);

            solrTemplate.saveBeans(list);
            solrTemplate.commit();

        }
    }

    @Test
    public void testPageQuery(){
        Query query = new SimpleQuery("*:*");
        query.setOffset(0);
        query.setRows(200);
        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        System.out.println(tbItems.getTotalElements());
        List<TbItem> content = tbItems.getContent();
        showList(content);

    }

    private void showList(List<TbItem> content) {
        for (TbItem item : content) {
            System.out.println(item.getTitle() + " " + item.getPrice() + " " + item.getImage());
        }
    }

    @Test
    public  void testPageQueryMutil(){
        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_title").contains("2");
        criteria = criteria.and("item_title").contains("5");
        query.addCriteria(criteria);
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        System.out.println(page.getTotalElements());
        List list = page.getContent();
        showList(list);
    }


}
