package edu.scsa.android.testmemo;

import android.util.Log;
import android.widget.Toast;

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
        if (getMemo(m.getTitle()) != null) return -1;

        memoList.add(0,m);
        return memoList.size();
    }

    void updateMemo(String title, Memo m) {
        int i;
        Memo t = getMemo(title);
        if(t == null) return;

        i = memoList.indexOf(t);
        memoList.remove(i);
        memoList.add(i, m);
    }

    void deleteMemo(String title){

        if(getMemo(title) != null) {
            memoList.remove(getMemo(title));
        };

    }

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
