package skyline.repository.model;

public class PtenudetailKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PTENUDETAIL.ENUITEMVALUE
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    private String enuitemvalue;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PTENUDETAIL.ENUTYPE
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    private String enutype;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PTENUDETAIL.ENUITEMVALUE
     *
     * @return the value of PTENUDETAIL.ENUITEMVALUE
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    public String getEnuitemvalue() {
        return enuitemvalue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PTENUDETAIL.ENUITEMVALUE
     *
     * @param enuitemvalue the value for PTENUDETAIL.ENUITEMVALUE
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    public void setEnuitemvalue(String enuitemvalue) {
        this.enuitemvalue = enuitemvalue == null ? null : enuitemvalue.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PTENUDETAIL.ENUTYPE
     *
     * @return the value of PTENUDETAIL.ENUTYPE
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    public String getEnutype() {
        return enutype;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PTENUDETAIL.ENUTYPE
     *
     * @param enutype the value for PTENUDETAIL.ENUTYPE
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    public void setEnutype(String enutype) {
        this.enutype = enutype == null ? null : enutype.trim();
    }
}