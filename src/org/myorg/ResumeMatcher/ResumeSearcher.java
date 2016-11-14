/******************************************************************************//*!
* @File          ResumeSearcher.java
* 
* @Title         Application for searching a query across an inverted index 
* 				 over resumes.
* 
* @Author        Chetan Borse
* 
* @Created on    11/13/2016
* 
*//*******************************************************************************/


package org.myorg.ResumeMatcher;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;


/* Query Parse Exception Class */
class QueryParseException extends Exception {

    public QueryParseException(String message) {
        super(message);
    }

}


/* Score Comparator Class */
class ScoreComparator implements Comparator<String> {
	Map<String, String> base;
	
	public ScoreComparator(Map<String, String> base) {
		this.base = base;
	}
	
	public int compare(String a, String b) {
		if (Double.parseDouble(base.get(a)) >= 
				Double.parseDouble(base.get(b))) {
			return -1;
		} else {
			return 1;
		}
	}
}


/* Resume Searcher Class */
public class ResumeSearcher {

	private String invertedIndexPath;
	private int maxSearch;
	private IndexReader indexReader;
	private IndexSearcher indexSearcher;
	private QueryParser queryParser;

	// Constructor
	public ResumeSearcher(String invertedIndexPath) throws IOException {
		this.invertedIndexPath = invertedIndexPath;
		this.maxSearch = 50;

		// Create an inverted index searcher and a query parser
		createIndexSearcher();
		createQueryParser();
	}

	// Constructor
	public ResumeSearcher(String invertedIndexPath,
						  int maxSearch) throws IOException {
		this.invertedIndexPath = invertedIndexPath;
		this.maxSearch = maxSearch;
		
		// Create an inverted index searcher and a query parser
		createIndexSearcher();
		createQueryParser();
	}
	
	// Public setters
	public void setInvertedIndexPath(String invertedIndexPath) {
		this.invertedIndexPath = invertedIndexPath;
	}
	public void setMaxSearch(int maxSearch) {
		this.maxSearch = maxSearch;
	}

	// Public getters
	public String getInvertedIndexPath() {
		return invertedIndexPath;
	}
	public int getMaxSearch() {
		return maxSearch;
	}

	/* Create an inverted index searcher */
	private void createIndexSearcher() throws IOException {
		if (invertedIndexPath == null) {
			System.err.println("Inverted index path is not specified!");
			System.exit(1);
		}

		// Create an inverted index reader
		indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(invertedIndexPath)));
		
		// Create an inverted index searcher
		indexSearcher = new IndexSearcher(indexReader);
	}
	
	/* Create a query parser */
	private void createQueryParser() {
		queryParser = new QueryParser("contents",
									  new StandardAnalyzer());
	}

	/* Search a query across an inverted index over resumes */
	public TreeMap<String, String> search(String searchQuery) 
			throws IOException, QueryParseException {
		Query query;
		TopDocs hits;
		Document doc;
		String resume;
		String score;
		HashMap<String, String> result = new HashMap<String, String>();
		ScoreComparator cmp = new ScoreComparator(result);
		TreeMap<String, String> topResult = new TreeMap<String, String>(cmp);
		
		searchQuery = searchQuery.trim();
		if (searchQuery == null) {
			System.err.println("Search query is not specified!");
			System.exit(1);
		}

        System.out.println("Searching: " + searchQuery);

        // Construct a query
        try {
        	query = queryParser.parse(searchQuery);
        } catch (ParseException e) {
        	throw new QueryParseException("Parsing search query failed!");
        }
		
        // Retrieve search results
        hits = indexSearcher.search(query, maxSearch);
		
        // Store path and score of resumes into HashMap
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			doc = indexSearcher.doc(scoreDoc.doc);
			resume = doc.get("filepath");
			score = Float.toString(scoreDoc.score);
			
			result.put(resume, score);
		}
		
		// Sort resumes in decreasing order of scores
		if (!result.isEmpty()) {
			topResult.putAll(result);
		}
		
		return topResult;
	}

	/* Close index reader */
	public void close() throws IOException {
		indexReader.close();
	}

}

