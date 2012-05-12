package com.sillycat.easyhunter.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.apache.lucene.document.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sillycat.easyhunter.plugin.lucene.LuceneObject;
import com.sillycat.easyhunter.plugin.lucene.LuceneService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"file:src/test/resources/test-context.xml" }) 
public class ArticleLuceneServiceTest {

	@Autowired  
    @Qualifier("articleLuceneService")  
	private LuceneService articleLuceneService;


	@Test
	public void dumy() {
		Assert.assertTrue(true);
	}

	@Test
	public void search() throws Exception {
		List<LuceneObject> list = new ArrayList<LuceneObject>();
		Article a1 = new Article();
		a1.setAuthor("罗华");
		a1.setContent("罗华用中文写的一篇文章");
		a1.setGmtCreate(new Date());
		a1.setId("1");
		a1.setTitle("中文的技术BLOG");
		a1.setWebsiteURL("http://sillycat.iteye.com");
		Article a2 = new Article();
		a2.setAuthor("弓飞箭");
		a2.setContent("罗华用中文1写的一篇文章");
		a2.setGmtCreate(new Date());
		a2.setId("2");
		a2.setTitle("英文的技术BLOG");
		a2.setWebsiteURL("http://hi.baidu.com/luohuazju");
		list.add(a1);
		list.add(a2);
		articleLuceneService.buildIndex(list,true);
		List<Document> results = articleLuceneService.search("logonName", "中文1",true);
		assertEquals(1, results.size());
		Document doc = results.get(0);
		assertEquals("弓飞箭", doc.get("author"));
		assertEquals("2", doc.get("id"));
		assertEquals("英文的技术BLOG", doc.get("title"));
		
		results = articleLuceneService.search("logonName", "中",true);
		assertEquals(2, results.size());

	}
	
}
