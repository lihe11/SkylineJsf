package skyline.repository.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import skyline.repository.model.Ptoperrole;
import skyline.repository.model.PtoperroleExample;

import java.util.List;

@Component
public interface PtoperroleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPERROLE
     *
     * @mbggenerated Sat Mar 19 22:33:45 CST 2011
     */
    int countByExample(PtoperroleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPERROLE
     *
     * @mbggenerated Sat Mar 19 22:33:45 CST 2011
     */
    int deleteByExample(PtoperroleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPERROLE
     *
     * @mbggenerated Sat Mar 19 22:33:45 CST 2011
     */
    int insert(Ptoperrole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPERROLE
     *
     * @mbggenerated Sat Mar 19 22:33:45 CST 2011
     */
    int insertSelective(Ptoperrole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPERROLE
     *
     * @mbggenerated Sat Mar 19 22:33:45 CST 2011
     */
    List<Ptoperrole> selectByExample(PtoperroleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPERROLE
     *
     * @mbggenerated Sat Mar 19 22:33:45 CST 2011
     */
    int updateByExampleSelective(@Param("record") Ptoperrole record, @Param("example") PtoperroleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPERROLE
     *
     * @mbggenerated Sat Mar 19 22:33:45 CST 2011
     */
    int updateByExample(@Param("record") Ptoperrole record, @Param("example") PtoperroleExample example);
}