package com.czp.opensrource.dlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest extends TestCase {
	
	public AppTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public void testApp() {
		assertTrue(true);
	}

	public static void main(String[] args) {
		Logger log = LoggerFactory.getLogger(AppTest.class);
		log.debug("dgfdfgfdgfdhh");
	}
}
