package com.thinkit.cloud.filecopytools.util;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;

public class MyIOFileFilter implements IOFileFilter{

	@Override
	public boolean accept(File file) {
		return true;
	}

	@Override
	public boolean accept(File dir, String name) {
		return true;
	}

}
