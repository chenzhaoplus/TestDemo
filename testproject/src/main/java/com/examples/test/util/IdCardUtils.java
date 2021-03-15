package com.examples.test.util;

import cn.hutool.core.util.IdcardUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @ClassName: IdUtils
 * @Description:
 * @Author: zp
 * @Date: Created in 2020/3/31 14:25
 * @Version V1.0
 **/
public class IdCardUtils {

    private static final int CHINA_ID_MIN_LENGTH = 15;

    public static boolean isIDNumber(String IDNumber) {

        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        //$结尾

        //假设15位身份证号码:410001910101123  410001 910101 123
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        //$结尾


        boolean matches = IDNumber.matches(regularExpression);

        //判断第18位校验值
        if (matches) {

            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    return idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase());

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

        }
        return matches;
    }

    public static String[] getBirthAndSexByIdNo(String identifyNumber) {

        String dateOfBirth = null;

        String gender = null;

        //通过身份证获取性别和生日
        if (identifyNumber != null && !"".equals(identifyNumber)) {

            if (identifyNumber.length() == 15) {

                dateOfBirth = "19" + identifyNumber.substring(6, 8) + "-" + identifyNumber.substring(8, 10) + "-" + identifyNumber.substring(10, 12);

                gender = identifyNumber.substring(14, 15);

                /*基数为男 偶数为女*/
                if (Integer.parseInt(gender) % 2 == 0) {
                    gender = "2";
                } else {
                    gender = "1";
                }
            } else if (identifyNumber.length() == 18) {
                dateOfBirth = identifyNumber.substring(6, 10) + "-" + identifyNumber.substring(10, 12) + "-" + identifyNumber.substring(12, 14);

                gender = identifyNumber.substring(16, 17);

                /*基数为男 偶数为女*/
                if (Integer.parseInt(gender) % 2 == 0) {
                    gender = "2";
                } else {
                    gender = "1";
                }
            }
        }

        String[] strings = new String[]{dateOfBirth, gender};

        return strings;
    }

    public static Date getBirthByIdcard(String idCard) {
        if (StringUtils.isBlank(idCard) || idCard.length() < CHINA_ID_MIN_LENGTH) {
            return null;
        }

        String birth = IdcardUtil.getBirth(idCard);
        return DateUtils.parseDate(birth, "yyyyMMdd");
    }

    public static String getSexByIdCard(String idCard){
        if (StringUtils.isBlank(idCard) || idCard.length() < CHINA_ID_MIN_LENGTH) {
            return null;
        }
        int gender = IdcardUtil.getGenderByIdCard(idCard);
        return String.valueOf(gender);
    }

    public static String getNativeProvinceByIdCard(String idCard){
        if (StringUtils.isBlank(idCard) || idCard.length() < CHINA_ID_MIN_LENGTH) {
            return null;
        }
        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = IdcardUtil.convert15To18(idCard);
        }
        return idCard.substring(0, 2) + "0000";
    }

    public static String getNativeCityByIdCard(String idCard){
        if (StringUtils.isBlank(idCard) || idCard.length() < CHINA_ID_MIN_LENGTH) {
            return null;
        }
        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = IdcardUtil.convert15To18(idCard);
        }
        return idCard.substring(0, 4) + "00";
    }

    public static String getNativeDistrictByIdCard(String idCard){
        if (StringUtils.isBlank(idCard) || idCard.length() < CHINA_ID_MIN_LENGTH) {
            return null;
        }
        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = IdcardUtil.convert15To18(idCard);
        }
        return idCard.substring(0, 6);
    }

    public static void main(String[] args) {
        boolean idNumber = isIDNumber("420222199401015472");
        String[] birthAndSexByIdNo = getBirthAndSexByIdNo("420222199401015472");
        System.out.println(idNumber);
    }

}
