/******************************************************************************//*!
* @File          ResumeMatcher.java
* 
* @Title         Application for creating an inverted index over resumes and 
* 				 retrieving a set of matching resumes by searching across 
* 				 an inverted index generated.
* 
* @Usage		 java ResumeMatcher <Inverted Index Path> <Input Path> <Search Query>
* 				 e.g.
* 				 	java ResumeMatcher "./inverted_index" "./input" "java"
* 
* @Author        Chetan Borse
* 
* @Created on    11/13/2016
* 
*//*******************************************************************************/


package org.myorg.ResumeMatcher;


import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;


public class ResumeMatcher {

   /* Entry point */
   public static void main(String[] args) {
	   ResumeMatcher resumeMatcher = new ResumeMatcher();

	   try {
		   resumeMatcher.createInvertedIndex(args[0], args[1]);
		   resumeMatcher.search(args[0], args[2]);
	   } catch (IOException e) {
		   e.printStackTrace();
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
   }

	/* Create an inverted index over resumes */
   public void createInvertedIndex(String invertedIndexPath, String inputPath) 
		   throws IOException {
	   ResumeIndexer resumeIndexer;
	   int indexedCount;

	   System.out.println("");

	   resumeIndexer = new ResumeIndexer(invertedIndexPath, "A");

	   long startTime = System.currentTimeMillis();
	   indexedCount = resumeIndexer.createInvertedIndex(inputPath, null);
	   long endTime = System.currentTimeMillis();
	   
	   resumeIndexer.close();
	   
	   System.out.println("Total resumes indexed: " + indexedCount);
	   System.out.println("Time taken: " + (endTime - startTime) + " ms");
   	}

   	/* Retrieve a set of matching resumes by searching across an inverted index */
   	public void search(String invertedIndexPath, String searchQuery) 
   			throws IOException, QueryParseException {
   		ResumeSearcher resumeSearcher;
   		TreeMap<String, String> topResult = new TreeMap<String, String>();

   		System.out.println("");

   		resumeSearcher = new ResumeSearcher(invertedIndexPath, 10);

   		long startTime = System.currentTimeMillis();
   		topResult = resumeSearcher.search(searchQuery);
   		long endTime = System.currentTimeMillis();

   		resumeSearcher.close();

		for (Entry<String, String> resume : topResult.entrySet()) {
   			System.out.println("Resume: " + resume.getKey() + "\t" + 
   							   "Score: " + resume.getValue());
		}

		System.out.println("Time taken: " + (endTime - startTime) + " ms");
   	}
}

