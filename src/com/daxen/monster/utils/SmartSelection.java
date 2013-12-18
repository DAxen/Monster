/**
 * 
 */
package com.daxen.monster.utils;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

import com.daxen.monster.PreferActivity;

/**
 * @author ʥ��
 * SmartSelection���ݾ����ʱ��Σ��жϳ�Ӧ��ѡ��ļ��˷��࣬�����û����˵Ĳ����ɱ�
 */
public class SmartSelection {
	// ֧������ID����Щ����Ĭ��ֵ��IDд�������
	private static final int EXPEND_TYPE_ID_BREAKFAST = 1;   // ���
	private static final int EXPEND_TYPE_ID_LUNCH     = 2;   // ���
	private static final int EXPEND_TYPE_ID_DINNER    = 3;   // ���
	private static final int EXPEND_TYPE_ID_SNACKS    = 4;   // ��ʳ
	private static final int EXPEND_TYPE_ID_FRUIT     = 5;   // ˮ��
	private static final int EXPEND_TYPE_ID_DRINK     = 6;   // ����
	private static final int EXPEND_TYPE_ID_POST      = 7;   // ����
	private static final int EXPEND_TYPE_ID_TRANSP    = 8;   // ��ͨ
	private static final int EXPEND_TYPE_ID_BOOKS     = 9;   // ͼ��
	private static final int EXPEND_TYPE_ID_MIDSNACKS = 10;  // ҹ��
	private static final int EXPEND_TYPE_ID_SHOPPING  = 11;  // ����
	private static final int EXPEND_TYPE_ID_ENTERTAIN = 12;  // ����
	private static final int EXPEND_TYPE_ID_MEDICAL   = 13;  // ҽҩ
	
	// �������ID
	private static final int INCOME_TYPE_ID_SALARY   = 1;    // ����
	private static final int INCOME_TYPE_ID_BONUS    = 2;    // ����
	private static final int INCOME_TYPE_ID_INTEREST = 3;    // �ֺ�
	private static final int INCOME_TYPE_ID_STOCK    = 4;    // ��Ʊ
	private static final int INCOME_TYPE_ID_GIFT     = 5;    // ���
	private static final int INCOME_TYPE_ID_SALES    = 6;    // ��������
	private static final int INCOME_TYPE_ID_REIMBURS = 7;    // ����
	
	// �˻�����ID
	private static final int ACCOUNT_ID_CASH     = 1;        // �ֽ�  
	private static final int ACCOUNT_ID_DEPOSIT  = 2;        // ���
	private static final int ACCOUNT_ID_CREDIT   = 3;        // ���ÿ�
	private static final int ACCOUNT_ID_CATERING = 4;        // ����
	private static final int ACCOUNT_ID_TRANSIT  = 5;        // ������
	
	private static SmartSelection _instance;
	
	SmartSelection() {
		// do nothing
	}
	
	public static SmartSelection Instance(Context ctx) {
		// �Ȳ�ѯ����ʹ��״̬����ʹ�ܷ��ؿ�
		SharedPreferences sp = ctx.getSharedPreferences(PreferActivity.SHARED_PREFER_NAME, Context.MODE_PRIVATE);
		if (!(sp.getBoolean(PreferActivity.SP_KEY_SMART_SELECTION, false))) {
			return null;
		}
		
		if (null == _instance) {
			_instance = new SmartSelection();
		}
		return _instance;
	}
	
	public int GetExpendTypeId() {
		// ��ȡ��ǰϵͳʱ��
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		
		// ��ȡ�ĵ�ǰСʱ
		int curHour = cal.get(Calendar.HOUR_OF_DAY);
		
		// ����Сʱ������ʱ���
		// 0.0�㵽4��59 ��ҹ
		if (0 <= curHour && curHour < 5) {
			return EXPEND_TYPE_ID_MIDSNACKS;
		}
		// 1.5�㵽9��59 ���
		if (4 < curHour && curHour < 10) {
			return EXPEND_TYPE_ID_BREAKFAST;
		}
		// 2.10�㵽10��59 ˮ��
		else if (9 < curHour && curHour < 11) {
			return EXPEND_TYPE_ID_FRUIT;
		}
		// 3.11�㵽13��59 ���
		else if (10 < curHour && curHour < 14) {
			return EXPEND_TYPE_ID_LUNCH;
		}
		// 4.14�㵽16��59 ����
		else if (13 < curHour && curHour < 17) {
			return EXPEND_TYPE_ID_SHOPPING;
		}
		// 5.17�㵽19��59 ���
		else if (16 < curHour && curHour < 20) {
			return EXPEND_TYPE_ID_DINNER;
		}
		// 6.20�㵽21��59 ����
		else if (19 < curHour && curHour < 22) {
			return EXPEND_TYPE_ID_SHOPPING;
		}
		// 7.22�㵽24�� ��ҹ
		else if (21 < curHour && curHour <= 24) {
			return EXPEND_TYPE_ID_MIDSNACKS;
		}
		
		return 0;
	}
}
