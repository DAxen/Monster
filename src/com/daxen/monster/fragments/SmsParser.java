/**
 * 
 */
package com.daxen.monster.fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.format.Time;
import android.util.Log;

import com.daxen.monster.utils.TallyRaw;

/**
 * @author ʥ��
 * �����û�����
 */
public class SmsParser {
	private static final String TAG = "SmsParser";
/*
 * ��������ģ�壺
 * 1.֧��
 * ��β��XXXX��XX��XX:XXPOS֧��(����)XX.XXԪ���������ÿ����������С�
 * 
 * 2.����
 * ��β��XXXX��XX��XX:XX������������(����)XXXXXXԪ�����������С�
 * 
 * ģʽ��
 * 1.β��XXXX��
 * 2.XX��XX:XX
 * 3.����/֧��XXXXԪ
 */
	
	private static final String ICBC_REG_CARD   = "β��[0-9]{4}��";
	private static final String ICBC_REG_TIME   = "[0-9]{1,2}��[0-9]{1,2}:[0-9]{1,2}";
	private static final String ICBC_REG_EXPEND = "֧��.*Ԫ";
	private static final String ICBC_REG_INCOME = "����.*Ԫ";
	
	public static TallyRaw ParseTally(String str) {
		Log.v(TAG, "PARSING:"+str);
		String strCard = "";
		double dAmount = 0;
		String strTime = "";
		int    type    = TallyRaw.TALLY_MODE_EXPEND;
		
		// card no
		Pattern pCard = Pattern.compile(ICBC_REG_CARD);
		Matcher mCard = pCard.matcher(str);
		if (mCard.find()) {
			String regCardNo = "[0-9]{4}";
			Pattern pCardNo = Pattern.compile(regCardNo);
			Matcher mCardNo = pCardNo.matcher(mCard.group());
			if (mCardNo.find()) {
				strCard = mCardNo.group();
				Log.v(TAG, "card:"+strCard);
			}
		}
		
		// time
		Pattern pTime = Pattern.compile(ICBC_REG_TIME);
		Matcher mTime = pTime.matcher(str);
		if (mTime.find()) {
			Log.v(TAG, "time:"+mTime.group());
			// date
			String date = "";
			Matcher mDate = Pattern.compile("[0-9]{1,2}").matcher(mTime.group());
			if (mDate.find()) {
				date = mDate.group();
			}
			
			// time
			String time = "";
			Matcher mcTime = Pattern.compile("[0-9]{1,2}:[0-9]{1,2}").matcher(mTime.group());
			if (mcTime.find()) {
				time = mcTime.group();
			}
			Time cur = new Time();
			cur.setToNow();
			strTime = cur.format("%Y-%m"); // ��ȡ����ǰ����-��
			strTime = strTime+"-"+date+" "+time;
			Log.v(TAG, "time:"+strTime);
		}
		
		// expend
		Pattern pAmountE = Pattern.compile(ICBC_REG_EXPEND);
		Matcher mAmountE = pAmountE.matcher(str);
		if (mAmountE.find()) {
			type = TallyRaw.TALLY_MODE_EXPEND;
			// ֻ�������ֺ�С����
			String amount = Pattern.compile("[^[0-9|\\.]]{1,2}").matcher(mAmountE.group()).replaceAll("");
			Log.v(TAG, "amount:"+amount);
			try {
				dAmount = Double.parseDouble(amount);
			}
			catch (NumberFormatException e) {
				dAmount = 0;
			}
		}
		
		// income
		Pattern pAmountI = Pattern.compile(ICBC_REG_INCOME);
		Matcher mAmountI = pAmountI.matcher(str);
		if (mAmountI.find()) {
			type = TallyRaw.TALLY_MODE_INCOME;
			// ֻ�������ֺ�С����
			String amount = Pattern.compile("[^[0-9|\\.]]{1,2}").matcher(mAmountI.group()).replaceAll("");
			Log.v(TAG, "amount:"+amount);
			try {
				dAmount = Double.parseDouble(amount);
			}
			catch (NumberFormatException e) {
				dAmount = 0;
			}
		}
		
		if (0 == dAmount) {
			return null;
		}
		
		TallyRaw tally = new TallyRaw();
		tally.mTallyMode = type;
		tally.mTime = strTime;
		tally.mAmount = dAmount;
		
		return tally;
	}
}
