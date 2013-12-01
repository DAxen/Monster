package com.daxen.monster.utils;

/**
 * 
 * @author Gsy
 * @version 1.0
 * 
 * class TallyRaw
 * ԭʼ����Ŀ��Ϣ���ɼ�ԭʼ�ģ�δ���ӹ�������Ŀ��Ϣ
 * 
 * History: 2013-03-24
 * 
 */

public class TallyRaw {
	public static final int TALLY_MODE_INCOME = 0;
	public static final int TALLY_MODE_EXPEND = 1;
	
	public int    mId;         // ��Ŀ��ϢID
	public int    mTallyMode;  // ��Ŀ���ͣ�����/֧��
	public int    mAccount;    // �˻�
	public int    mType;       // ��������
	public double mAmount;     // ���
	public String mSeller;     // �̼�
	public String mPayer;      // ֧����
	public String mTime;       // ����ʱ��
	public String mRemark;     // ��ע��Ϣ
	public String mLendto;     // ���
	public String mFrom;       // ��XX������
	public String mDeadline;   // �黹����
	
	public TallyRaw() {
		mTallyMode = 0;
		mAccount   = 0;
		mType      = 0;
		mAmount    = 0;
		mSeller    = null;
		mPayer     = null;
		mTime      = null;
		mRemark    = null;
		mLendto    = null;
		mFrom      = null;
		mDeadline  = null;
	}
}
