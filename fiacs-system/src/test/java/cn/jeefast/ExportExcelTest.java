package cn.jeefast;

import cn.jeefast.modules.fiacs.dao.EquipEntityDao;
import com.alibaba.druid.util.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.jeefast.common.excel.ExcelUtil;
import cn.jeefast.system.entity.SysUser;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel工具类测试
 * 
 * @author theodo
 * @email 36780272@qq.com
 * @date 2017年11月26日 上午10:40:10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ExportExcelTest {
    @Resource
    private EquipEntityDao equipEntityDao;
   // @Test
    public void test() throws Exception {
        List<SysUser> list = new ArrayList<SysUser>();
        SysUser sysUser = new SysUser();
        sysUser.setUsername("test1");
        sysUser.setEmail("test1@qq.com");
        sysUser.setMobile("13111111111");
        sysUser.setDeptName("技术开发部");
        sysUser.setCreateTime(new Date());
        list.add(sysUser);

        Map<String, String> map = new HashMap<String, String>();
        map.put("title", "用户信息表");
        map.put("total", list.size()+" 条");
        map.put("date", getDate());

        ExcelUtil.getInstance().exportObj2ExcelByTemplate(map, "web-info-template.xls", new FileOutputStream("D:/temp/out.xls"),
                list, SysUser.class, true);
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new Date());
    }

    @Test
    public void tes1t(){
        System.out.println(equipEntityDao.selectConfig("system_name"));
        System.out.println(equipEntityDao.selectConfig("nSuperGoNum"));
        System.out.println(equipEntityDao.selectConfig("nInterNum"));
        String qq = equipEntityDao.selectConfig("nInt1erNum");
        if(StringUtils.isEmpty(qq)){
            System.out.println(equipEntityDao.selectConfig("system_name"));
        }else{
            System.out.println(equipEntityDao.selectConfig("nInterNum"));
        }
    }
}
