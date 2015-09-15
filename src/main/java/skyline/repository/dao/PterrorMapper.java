package skyline.repository.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import skyline.repository.model.Pterror;
import skyline.repository.model.PterrorExample;

import java.util.List;

@Component

public interface PterrorMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int countByExample(PterrorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int deleteByExample(PterrorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int insert(Pterror record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int insertSelective(Pterror record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    List<Pterror> selectByExample(PterrorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int updateByExampleSelective(@Param("record") Pterror record, @Param("example") PterrorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Sat Mar 19 22:33:44 CST 2011
     */
    int updateByExample(@Param("record") Pterror record, @Param("example") PterrorExample example);
}