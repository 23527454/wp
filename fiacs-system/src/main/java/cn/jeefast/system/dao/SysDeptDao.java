package cn.jeefast.system.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.jeefast.system.entity.SysDept;

/**
 * <p>
  * 部门管理 Mapper 接口
 * </p>
 *
 * @author theodo
 * @since 2017-10-28
 */
public interface SysDeptDao extends BaseMapper<SysDept> {
	
	List<SysDept> queryList(Map<String, Object> map);
	
	/**
     * 查询子部门ID列表
     * @param parentId  上级部门ID
     */
    List<Long> queryDetpIdList(Long parentId);
    
    
    /**
     * 查询子孙部门ID列表
     * @param parentId  上级部门ID
     */
    List<Long> queryAllDetpIdList(Long parentId);
    
    /**
     * 根据dept查询机构信息
     * @param deptId
     * @return
     */
    SysDept findByDeptId(Long deptId);
    
    /**
     * 根据角色设备权限查询对赢组织树
     * @param userId
     * @return
     */
    List<SysDept> queryListByUserId(String userId);
}