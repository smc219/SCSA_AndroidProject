package edu.scsa.android.testmemo;

import java.util.ArrayList;

public class MemoManager {
    ArrayList<Memo> memoList;
    static MemoManager instance;
    private MemoManager() {
        memoList = new ArrayList<>();
    }

    public static MemoManager getInstance() {
        if (instance == null) {
           instance = new MemoManager();
        }
        return instance;
    }

    int addMemo(Memo m) {
        for (Memo memo : memoList) {
            if(memo.getTitle().equals(m.getTitle())) {
                return -1;
            }
        }
        memoList.add(0,m);
        return memoList.size();
    }

    void updateMemo(String title, Memo m) {
        int i;
        for (i = 0; i < memoList.size(); i++) {
            if (memoList.get(i).getTitle().equals(title)) break;
        }
        memoList.remove(i);
        memoList.add(i, m);
    }

    void deleteMemo(String title){}

    Memo getMemo(String title) {
        for (int i = 0; i < memoList.size(); i++) {
            if (memoList.get(i).getTitle().equals(title)) return memoList.get(i);
        }
        return null;
    }



    ArrayList<Memo> getAllMemo() {
        return memoList;
    }
}
