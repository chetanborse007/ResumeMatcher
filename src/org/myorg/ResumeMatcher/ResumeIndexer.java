/******************************************************************************//*!
* @File          ResumeIndexer.java
* 
* @Title         Application for creating an inverted index over resumes.
* 
* @Author        Chetan Borse
* 
* @Created on    11/13/2016
* 
*//*******************************************************************************/


package org.myorg.ResumeMatcher;


import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


/* Resume Indexer Class */
public class ResumeIndexer {

	private String invertedIndexPath;
	private String mode;
	private boolean isForceMerge;
	private int maxNumSegments;
	private IndexWriter indexWriter;

	// Constructor
	public ResumeIndexer(String invertedIndexPath,
			 			 String mode) {
		this.invertedIndexPath = invertedIndexPath;
		this.mode = mode;
		this.isForceMerge = false;
		this.maxNumSegments = Integer.MAX_VALUE;
	}

	// Constructor
	public ResumeIndexer(String invertedIndexPath,
						 String mode,
						 boolean isForceMerge,
						 int maxNumSegments) {
		this.invertedIndexPath = invertedIndexPath;
		this.mode = mode;
		this.isForceMerge = isForceMerge;
		this.maxNumSegments = maxNumSegments;
	}

	// Public setters
	public void setInvertedIndexPath(String invertedIndexPath) {
		this.invertedIndexPath = invertedIndexPath;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public void setForceMerge(boolean isForceMerge) {
		this.isForceMerge = isForceMerge;
	}
	public void setMaxNumSegments(int maxNumSegments) {
		this.maxNumSegments = maxNumSegments;
	}

	// Public getters
	public String getInvertedIndexPath() {
		return invertedIndexPath;
	}
	public String getMode() {
		return mode;
	}
	public boolean isForceMerge() {
		return isForceMerge;
	}
	public int getMaxNumSegments() {
		return maxNumSegments;
	}

	/* Create an Index Writer */
	public void createIndexWriter() throws IOException {
		Directory invertedIndexDirectory;
		Analyzer analyzer;
		IndexWriterConfig iwc;
		
		if (invertedIndexPath == null) {
			System.err.println("Inverted index path is not specified!");
			System.exit(1);
		}

		// Inverted index directory
		invertedIndexDirectory = FSDirectory.open(Paths.get(invertedIndexPath));

		// Index writer configuration
		analyzer = new StandardAnalyzer();
		iwc = new IndexWriterConfig(analyzer);

        // If mode is "W", then set mode = "CREATE".
        // If mode is "A", then set mode = "CREATE_OR_APPEND".
		if (mode.equalsIgnoreCase("W")) {
			iwc.setOpenMode(OpenMode.CREATE);
		} else if (mode.equalsIgnoreCase("A")) {
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		} else {
			System.err.println("Index writer mode is not specified!");
			System.exit(1);
		}

		// Create an index writer
		indexWriter = new IndexWriter(invertedIndexDirectory, iwc);
	}

	/* Create an Inverted Index */
	public int createInvertedIndex(String inputPath, FileFilter filter) 
			throws IOException{
		// Create an index writer
		createIndexWriter();
		
		if (inputPath == null) {
			System.err.println("Input path is not specified!");
			System.exit(1);
		}

		// List all files in the input folder
		File[] files = new File(inputPath).listFiles();

		for (File file : files) {
			if(!file.isDirectory()
			   && !file.isHidden()
			   && file.exists()
			   && file.canRead()
			   && (filter == null
			   	   || (filter != null
			   	   	   && filter.accept(file)))) {
				indexFile(file);
			}
		}
		
		// For maximizing search performance, call forceMerge.
		// Note: This can be a terribly costly operation, 
		// 		 so it's worth only when inverted index is relatively static
		// 		 i.e. new documents are not being added/updated.
		if (isForceMerge) {
			indexWriter.forceMerge(maxNumSegments);
		}
		
		return indexWriter.numDocs();
	}

	/* Index a single file */
	private void indexFile(File file) throws IOException {
		Document document = new Document();

		System.out.println("Indexing: " + file.getCanonicalPath());

		// Index entire file name as a single token without tokenization
		Field fileNameField = new StringField("filename",
											  file.getName(),
											  Field.Store.YES);
		document.add(fileNameField);

		// Index entire file path as a single token without tokenization
		Field filePathField = new StringField("filepath",
											  file.getCanonicalPath(),
											  Field.Store.YES);
		document.add(filePathField);

		// Index file contents with tokenization
		Field fileContentField = new TextField("contents", 
										   new FileReader(file));
		document.add(fileContentField);
		
        // If mode is "CREATE", 
		// then create a new inverted index over a given file.
        // If mode is "CREATE_OR_APPEND", 
		// then update existing inverted index for a given file.
		if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE) {
			indexWriter.addDocument(document);
        } else if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE_OR_APPEND) {
        	indexWriter.updateDocument(new Term("filepath", file.getCanonicalPath()), 
        							   document);
        }
	}

	/* Close index writer */
	public void close() throws CorruptIndexException, IOException {
		indexWriter.close();
	}

}

