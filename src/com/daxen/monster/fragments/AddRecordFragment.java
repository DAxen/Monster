package com.daxen.monster.fragments;

import com.daxen.monster.utils.TallyRaw;

import android.app.Fragment;

public abstract class AddRecordFragment extends Fragment {

	public abstract TallyRaw GetTallyRaw();
	public abstract void ClearContent();
}
