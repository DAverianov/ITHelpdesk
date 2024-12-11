package de.lewens_markisen.utils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class FileUtilsLss {
	public static Optional<File> createTempFile(String pref, String suff) {
		try {
			return Optional.of(File.createTempFile(pref, suff));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
}
