package skyline.repository.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import skyline.repository.model.Ptoper;
import skyline.repository.model.PtoperExample;

import java.util.List;

@Component
public interface PtoperMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int countByExample(PtoperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int deleteByExample(PtoperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int deleteByPrimaryKey(String operid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int insert(Ptoper record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int insertSelective(Ptoper record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    List<Ptoper> selectByExample(PtoperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    Ptoper selectByPrimaryKey(String operid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int updateByExampleSelective(@Param("record") Ptoper record, @Param("example") PtoperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int updateByExample(@Param("record") Ptoper record, @Param("example") PtoperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int updateByPrimaryKeySelective(Ptoper record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int updateByPrimaryKey(Ptoper record);
}