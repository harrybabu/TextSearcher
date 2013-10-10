/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.harry;

import java.io.File;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


/**
 *
 * @author hari
 */
public class TextSearcher {
        public String searchIndex(File indexDir, String queryStr, int maxHits) throws Exception {
                String files = new String();
                String field = "contents";
                IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir));
                IndexSearcher searcher = new IndexSearcher(reader);
                Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
                
                Query query = new FuzzyQuery(new Term(field, queryStr));
//		Query query = parser.parse(queryStr);	
		TopDocs topDocs = searcher.search(query, maxHits);
		
		ScoreDoc[] hits = topDocs.scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
                        files = files + d.get("filename") + "\n";
			System.out.println(d.get("filename"));
		}
		files = files + String.valueOf(hits.length)+ " matches found" +"\n";
		System.out.println("Found " + hits.length);
		
                return files;
		
	}
}
