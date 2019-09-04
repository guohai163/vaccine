package org.guohai.vaccine.utilities;

/**
 * 工具类，验证输入参数是否正确
 */
public final class VerificationUtilities {
    /**
     * 验证是否为整形
     * @param param
     * @return
     */
    public static boolean validateInteger(String param) {
        return param.matches("[0-9]");
    }

    /**
     * 检查批号
     * @param param
     * @return
     */
    public static boolean validateBatchNo(String param) {
        return param.matches("[0-9a-zA-Z() \\-（）、～/\",~，\\.－\\第亚批+]");
    }

    /**
     * 检查格式是否符合中国日期
     * @param param
     * @return
     */
    public static boolean validateChineseDate(String param) {
        return param.matches("^[0-9]{4}年[0-9]{1,2}月[0-9]{0,2}[日]{0,1}.*");
    }
}
