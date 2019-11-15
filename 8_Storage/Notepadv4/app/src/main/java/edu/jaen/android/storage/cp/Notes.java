package edu.jaen.android.storage.cp;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Notes implements BaseColumns {
	public static final String AUTHORITY = "com.diana.android";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notes");

	public static final String DEFAULT_SORT_ORDER = "title";
	public static final String TITLE = "title";
	public static final String BODY = "body";
}
