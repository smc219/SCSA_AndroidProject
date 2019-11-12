package edu.scsa.android.testmemo;

import java.util.ArrayList;

public class MemoManager {
    ArrayList<Memo> memoList;

    public MemoManager() {
        memoList = new ArrayList<>();
    }

    void addMemo(Memo m) {
        memoList.add(m);
    }

    ArrayList<Memo> getAllMemo() {
        return memoList;
    }
}
