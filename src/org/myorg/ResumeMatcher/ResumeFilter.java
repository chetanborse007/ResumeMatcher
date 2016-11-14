/******************************************************************************//*!
* @File          ResumeFilter.java
* 
* @Title         Application for creating a resume filter.
* 
* @Author        Chetan Borse
* 
* @Created on    11/13/2016
* 
*//*******************************************************************************/


package org.myorg.ResumeMatcher;


import java.io.File;
import java.io.FileFilter;


/* Resume Filter Class */
public class ResumeFilter implements FileFilter {

	@Override
	public boolean accept(File file) {
		return file.getName().toLowerCase().endsWith(".txt");
	}

}

