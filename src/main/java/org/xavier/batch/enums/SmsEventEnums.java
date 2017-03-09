/* 
 * All rights Reserved, Designed By 农金圈
 * 2017年3月9日 下午1:41:59
 */
package org.xavier.batch.enums;


/**
 * 短信事件
 * 
 * @author: XavierZz
 */
public enum SmsEventEnums {

    CREADIT_PASS("放款通过"),
    PAY_PASS("放款成功"),
    LAST_REPAYMENT_SEVEN("最晚还款日7天"),
    LAST_REPAYMENT_ONE("最晚还款日1天");

    private String desc;

    private SmsEventEnums(String desc) {
        this.desc = desc;
    }


    /**
     * @return: String <br>
     */
    public String getDesc() {
        return desc;
    }


}
