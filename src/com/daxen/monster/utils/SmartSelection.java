/**
 * 
 */
package com.daxen.monster.utils;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

import com.daxen.monster.PreferActivity;

/**
 * @author 圣阳
 * SmartSelection根据具体的时间段，判断出应该选择的记账分类，减少用户记账的操作成本
 */
public class SmartSelection {
	// 支出分类ID，这些都是默认值，ID写死不会变
	private static final int EXPEND_TYPE_ID_BREAKFAST = 1;   // 早餐
	private static final int EXPEND_TYPE_ID_LUNCH     = 2;   // 午餐
	private static final int EXPEND_TYPE_ID_DINNER    = 3;   // 晚餐
	private static final int EXPEND_TYPE_ID_SNACKS    = 4;   // 零食
	private static final int EXPEND_TYPE_ID_FRUIT     = 5;   // 水果
	private static final int EXPEND_TYPE_ID_DRINK     = 6;   // 饮料
	private static final int EXPEND_TYPE_ID_POST      = 7;   // 邮政
	private static final int EXPEND_TYPE_ID_TRANSP    = 8;   // 交通
	private static final int EXPEND_TYPE_ID_BOOKS     = 9;   // 图书
	private static final int EXPEND_TYPE_ID_MIDSNACKS = 10;  // 夜宵
	private static final int EXPEND_TYPE_ID_SHOPPING  = 11;  // 购物
	private static final int EXPEND_TYPE_ID_ENTERTAIN = 12;  // 娱乐
	private static final int EXPEND_TYPE_ID_MEDICAL   = 13;  // 医药
	
	// 收入分类ID
	private static final int INCOME_TYPE_ID_SALARY   = 1;    // 工资
	private static final int INCOME_TYPE_ID_BONUS    = 2;    // 奖金
	private static final int INCOME_TYPE_ID_INTEREST = 3;    // 分红
	private static final int INCOME_TYPE_ID_STOCK    = 4;    // 股票
	private static final int INCOME_TYPE_ID_GIFT     = 5;    // 礼金
	private static final int INCOME_TYPE_ID_SALES    = 6;    // 销售收入
	private static final int INCOME_TYPE_ID_REIMBURS = 7;    // 报销
	
	// 账户分类ID
	private static final int ACCOUNT_ID_CASH     = 1;        // 现金  
	private static final int ACCOUNT_ID_DEPOSIT  = 2;        // 储蓄卡
	private static final int ACCOUNT_ID_CREDIT   = 3;        // 信用卡
	private static final int ACCOUNT_ID_CATERING = 4;        // 饭卡
	private static final int ACCOUNT_ID_TRANSIT  = 5;        // 公交卡
	
	private static SmartSelection _instance;
	
	SmartSelection() {
		// do nothing
	}
	
	public static SmartSelection Instance(Context ctx) {
		// 先查询配置使能状态，不使能返回空
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
		// 获取当前系统时间
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		
		// 获取的当前小时
		int curHour = cal.get(Calendar.HOUR_OF_DAY);
		
		// 根据小时数划分时间段
		// 0.0点到4点59 宵夜
		if (0 <= curHour && curHour < 5) {
			return EXPEND_TYPE_ID_MIDSNACKS;
		}
		// 1.5点到9点59 早餐
		if (4 < curHour && curHour < 10) {
			return EXPEND_TYPE_ID_BREAKFAST;
		}
		// 2.10点到10点59 水果
		else if (9 < curHour && curHour < 11) {
			return EXPEND_TYPE_ID_FRUIT;
		}
		// 3.11点到13点59 午餐
		else if (10 < curHour && curHour < 14) {
			return EXPEND_TYPE_ID_LUNCH;
		}
		// 4.14点到16点59 购物
		else if (13 < curHour && curHour < 17) {
			return EXPEND_TYPE_ID_SHOPPING;
		}
		// 5.17点到19点59 晚餐
		else if (16 < curHour && curHour < 20) {
			return EXPEND_TYPE_ID_DINNER;
		}
		// 6.20点到21点59 购物
		else if (19 < curHour && curHour < 22) {
			return EXPEND_TYPE_ID_SHOPPING;
		}
		// 7.22点到24点 宵夜
		else if (21 < curHour && curHour <= 24) {
			return EXPEND_TYPE_ID_MIDSNACKS;
		}
		
		return 0;
	}
}
