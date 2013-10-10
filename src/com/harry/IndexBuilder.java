/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.harry;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author hari
 */
public class IndexBuilder {
    
        public int index(File indexDir, File dataDir, String suffix) throws Exception {
                int numIndexed = 0;
                Directory dir = FSDirectory.open(indexDir);
                Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
		IndexWriterConfig in_config = new IndexWriterConfig(Version.LUCENE_44,analyzer );
		if (!indexDir.exists()) {
                    in_config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
                    IndexWriter indexWriter = new IndexWriter(dir,in_config);
                    indexDirectory(indexWriter, dataDir, suffix);		
                    numIndexed = indexWriter.maxDoc();
                    indexWriter.close();
                }
		dir.close();
		return numIndexed;
		
	}
    
        private void indexDirectory(IndexWriter indexWriter, File dataDir, String suffix) throws IOException {
		File[] files = dataDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				indexDirectory(indexWriter, f, suffix);
			}
			else {
				indexFileWithIndexWriter(indexWriter, f, suffix);
			}
		}
	}
	
	private void indexFileWithIndexWriter(IndexWriter indexWriter, File f, String suffix) throws IOException {
                if (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()) {
			return;
		}
		if (suffix!=null && !f.getName().endsWith(suffix)) {
			return;
		}
		System.out.println("Indexing file " + f.getCanonicalPath());
		Field fld = new TextField("contents", new FileReader(f));
                Field file = new StringField("filename",f.getCanonicalPath(), Field.Store.YES);
		Document doc = new Document();
		doc.add(fld);		
                doc.add(file);
		
		indexWriter.addDocument(doc);
	}
        
//        public void deleteIndexes(File dir){
//            File[] files = dir.listFiles();
//            for (File file : files) {
//                file.delete();
//            }
//        }
}
