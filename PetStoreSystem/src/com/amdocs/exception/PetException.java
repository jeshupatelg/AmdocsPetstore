package com.amdocs.exception;

@SuppressWarnings("serial")
public class PetException extends Exception {
	String issue;

	public PetException(String issue) {
		super(issue);
		this.issue = issue;
		System.out.println(issue);
	}
	
}
