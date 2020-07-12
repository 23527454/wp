package cn.jeefast.rest.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import cn.jeefast.common.validator.group.AddGroup;
import cn.jeefast.common.validator.group.UpdateGroup;

@TableName("sys_equipment")
public class Equipment extends Model<Equipment> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId(value="id",type=IdType.AUTO)
	@JSONField(ordinal=1)
	private Long id;
	
	@TableField("dept_id")
	@NotNull(message="所属机构不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@JSONField(ordinal=2)
	private Integer deptId;
	
	@TableField(exist=false)
	@JSONField(ordinal=3)
	private String deptName;
	
	@TableField("branches_type")
	@JSONField(ordinal=4)
	private Integer branchesType;
	
	@TableField("ip")
	@JSONField(ordinal=5)
	private String ip;
	
	@TableField("port")
	@JSONField(ordinal=6)
	private Integer port;
	
	@TableField("gateway")
	@JSONField(ordinal=7)
	private String gateWay;
	
	@TableField("ip_center")
	@JSONField(ordinal=8)
	private String ipCenter;
	
	@TableField("port_center")
	@JSONField(ordinal=9)
	private Integer portCenter;
	
	@TableField(exist=false)
	@JSONField(ordinal=10)
	private String branchesName;
	
	@TableField("port_print")
	@JSONField(ordinal=11)
	private String portPrint;
	
	@TableField("equip_sn")
	@JSONField(ordinal=12)
	private String equipSn;
	
	//设备序列号
	@TableField("equip_req")
	@JSONField(ordinal=13)
	private String equipReq;
	
	
	//控制器地址
	@TableField("address")
	@JSONField(ordinal=14)
	private String address;
	
	//子网掩码
	@TableField("subnet_mask")
	@JSONField(ordinal=15)
	private String subnetMask;
	
	//版本号
	@JSONField(ordinal=16)
	private String version;
	//闸机类型
	@JSONField(ordinal=17)
	private String zjType;
	
	//电机类型
	@JSONField(ordinal=18)
	private String djType;
	
	//闸机模式
	@JSONField(ordinal=19)
	private String zjModel;
	
	//左通行模式
	@JSONField(ordinal=20)
	private String leftCrossModel;
	
	//右通行模式
	@JSONField(ordinal=21)
	private String rightCrossModel;
	
	//记忆功能
	@JSONField(ordinal=22)
	private String remember;
	
	//反向物品穿行
	@JSONField(ordinal=23)
	private String fxwpCross;
	
	//儿童刷卡通行
	@JSONField(ordinal=24)
	private String babyCross;
	
	//闸机运行模式
	@JSONField(ordinal=25)
	private String zjWorkModel;
	
	//翼闸尾随关闸
	@JSONField(ordinal=26)
	private String yzwsgzModel;
	
	//主马达运行速度
	@JSONField(ordinal=27)
	private String zmdWorkSpeed;
	
	//副马达运行速度
	@JSONField(ordinal=28)
	private String fmdWorkSpeed;
	
	//马达最大运行时间
	@JSONField(ordinal=29)
	private String mdMaxWorkTime;
	
	//红外检测时间
	@JSONField(ordinal=30)
	private String hwjcTime;
	
	//通行间隔时间
	@JSONField(ordinal=31)
	private String txjgTime;
	
	//等待人员进入时间
	@JSONField(ordinal=32)
	private String ddryjrTime;
	
	//人员滞留时间
	@JSONField(ordinal=33)
	private String ryzlTime;
	
	//延时关闸时间
	private String ysgzTime;
	
	//自由通行间隔时间
	@JSONField(ordinal=34)
	private String zytxjgTime;
	
	//闯入报警
	@JSONField(ordinal=35)
	private String crAlarm;
	
	//尾随报警
	@JSONField(ordinal=36)
	private String wsAlarm;
	
	//滞留报警
	@JSONField(ordinal=37)
	private String zlAlarm;
	
	//自检报警
	@JSONField(ordinal=38)
	private String zjAlarm;
	
	//潜回报警
	@JSONField(ordinal=39)
	private String qhAlarm;
	
	//时钟
	@JSONField(ordinal=40)
	private String clock;
	
	//有效期
	@JSONField(ordinal=41)
	private String indate;
	
	//挡板材料
	@JSONField(ordinal=42)
	private String dbcl;
	
	//主开闸速度
	@JSONField(ordinal=43)
	private String zkzSpeed;
	
	//从开闸速度
	@JSONField(ordinal=44)
	private String ckzSpeed;
	
	//主关闸速度
	@JSONField(ordinal=45)
	private String zgzSpeed;
	
	//从关闸速度
	@JSONField(ordinal=46)
	private String cgzSpeed;
	
	//主阻挡电流
	@JSONField(ordinal=47)
	private String zzdElectric;
	
	//从阻挡电流
	@JSONField(ordinal=48)
	private String czdElectric;
	
	//主开闸时间
	@JSONField(ordinal=49)
	private String zkzTime;
	
	@JSONField(ordinal=50)
	private String ckzTime;
	
	//从关闸时间
	@JSONField(ordinal=51)
	private String zgzTime;
	
	@JSONField(ordinal=52)
	private String cgzTime;
	
	//主开闸角度
	@JSONField(ordinal=53)
	private String zkzAngle;
	
	//从开闸角度
	@JSONField(ordinal=54)
	private String ckzAngle;
	
	//强推脉冲数
	@JSONField(ordinal=55)
	private String qtmcs;
	
	//强退恢复时间
	@JSONField(ordinal=56)
	private String qthfTime;
	
	//阻挡反弹角度
	@JSONField(ordinal=57)
	private String zdftAngle;
	
	//阻挡模式
	@JSONField(ordinal=58)
	private String zdModel;
	
	//开闸间隔时间  
	@JSONField(ordinal=59)
	private String kzjgTime;
	
	@JSONField(ordinal=60)
	private String gzjgTime;
	
	/**
	 * 时间
	 */
	@TableField(exist=false)
	private Integer year;
	@TableField(exist=false)
	private Integer month;
	@TableField(exist=false)
	private Integer day;
	@TableField(exist=false)
	private Integer week;
	@TableField(exist=false)
	private Integer hours;
	@TableField(exist=false)
	private Integer minute;
	@TableField(exist=false)
	private Integer second;
	
	/**
	 * 有效期
	 */
	@TableField(exist=false)
	private Integer validYear;
	@TableField(exist=false)
	private Integer validMonth;
	@TableField(exist=false)
	private Integer validDay;
	
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Integer getHours() {
		return hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	public Integer getSecond() {
		return second;
	}

	public void setSecond(Integer second) {
		this.second = second;
	}

	public Integer getValidYear() {
		return validYear;
	}

	public void setValidYear(Integer validYear) {
		this.validYear = validYear;
	}

	public Integer getValidMonth() {
		return validMonth;
	}

	public void setValidMonth(Integer validMonth) {
		this.validMonth = validMonth;
	}

	public Integer getValidDay() {
		return validDay;
	}

	public void setValidDay(Integer validDay) {
		this.validDay = validDay;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getGzjgTime() {
		return gzjgTime;
	}

	public void setGzjgTime(String gzjgTime) {
		this.gzjgTime = gzjgTime;
	}

	public String getZjType() {
		return zjType;
	}

	public void setZjType(String zjType) {
		this.zjType = zjType;
	}

	public String getDjType() {
		return djType;
	}

	public void setDjType(String djType) {
		this.djType = djType;
	}

	public String getZjModel() {
		return zjModel;
	}

	public void setZjModel(String zjModel) {
		this.zjModel = zjModel;
	}

	public String getLeftCrossModel() {
		return leftCrossModel;
	}

	public void setLeftCrossModel(String leftCrossModel) {
		this.leftCrossModel = leftCrossModel;
	}

	public String getRightCrossModel() {
		return rightCrossModel;
	}

	public void setRightCrossModel(String rightCrossModel) {
		this.rightCrossModel = rightCrossModel;
	}

	public String getRemember() {
		return remember;
	}

	public void setRemember(String remember) {
		this.remember = remember;
	}

	public String getFxwpCross() {
		return fxwpCross;
	}

	public void setFxwpCross(String fxwpCross) {
		this.fxwpCross = fxwpCross;
	}

	public String getBabyCross() {
		return babyCross;
	}

	public void setBabyCross(String babyCross) {
		this.babyCross = babyCross;
	}

	public String getZjWorkModel() {
		return zjWorkModel;
	}

	public void setZjWorkModel(String zjWorkModel) {
		this.zjWorkModel = zjWorkModel;
	}

	public String getYzwsgzModel() {
		return yzwsgzModel;
	}

	public void setYzwsgzModel(String yzwsgzModel) {
		this.yzwsgzModel = yzwsgzModel;
	}

	public String getZmdWorkSpeed() {
		return zmdWorkSpeed;
	}

	public void setZmdWorkSpeed(String zmdWorkSpeed) {
		this.zmdWorkSpeed = zmdWorkSpeed;
	}

	public String getFmdWorkSpeed() {
		return fmdWorkSpeed;
	}

	public void setFmdWorkSpeed(String fmdWorkSpeed) {
		this.fmdWorkSpeed = fmdWorkSpeed;
	}

	public String getMdMaxWorkTime() {
		return mdMaxWorkTime;
	}

	public void setMdMaxWorkTime(String mdMaxWorkTime) {
		this.mdMaxWorkTime = mdMaxWorkTime;
	}

	public String getHwjcTime() {
		return hwjcTime;
	}

	public void setHwjcTime(String hwjcTime) {
		this.hwjcTime = hwjcTime;
	}

	public String getTxjgTime() {
		return txjgTime;
	}

	public void setTxjgTime(String txjgTime) {
		this.txjgTime = txjgTime;
	}

	public String getDdryjrTime() {
		return ddryjrTime;
	}

	public void setDdryjrTime(String ddryjrTime) {
		this.ddryjrTime = ddryjrTime;
	}

	public String getRyzlTime() {
		return ryzlTime;
	}

	public void setRyzlTime(String ryzlTime) {
		this.ryzlTime = ryzlTime;
	}

	public String getYsgzTime() {
		return ysgzTime;
	}

	public void setYsgzTime(String ysgzTime) {
		this.ysgzTime = ysgzTime;
	}

	public String getZytxjgTime() {
		return zytxjgTime;
	}

	public void setZytxjgTime(String zytxjgTime) {
		this.zytxjgTime = zytxjgTime;
	}

	public String getCrAlarm() {
		return crAlarm;
	}

	public void setCrAlarm(String crAlarm) {
		this.crAlarm = crAlarm;
	}

	public String getWsAlarm() {
		return wsAlarm;
	}

	public void setWsAlarm(String wsAlarm) {
		this.wsAlarm = wsAlarm;
	}

	public String getZlAlarm() {
		return zlAlarm;
	}

	public void setZlAlarm(String zlAlarm) {
		this.zlAlarm = zlAlarm;
	}

	public String getZjAlarm() {
		return zjAlarm;
	}

	public void setZjAlarm(String zjAlarm) {
		this.zjAlarm = zjAlarm;
	}

	public String getQhAlarm() {
		return qhAlarm;
	}

	public void setQhAlarm(String qhAlarm) {
		this.qhAlarm = qhAlarm;
	}

	public String getClock() {
		return clock;
	}

	public void setClock(String clock) {
		this.clock = clock;
	}

	public String getIndate() {
		return indate;
	}

	public void setIndate(String indate) {
		this.indate = indate;
	}

	public String getDbcl() {
		return dbcl;
	}

	public void setDbcl(String dbcl) {
		this.dbcl = dbcl;
	}

	public String getZkzSpeed() {
		return zkzSpeed;
	}

	public void setZkzSpeed(String zkzSpeed) {
		this.zkzSpeed = zkzSpeed;
	}

	public String getCkzSpeed() {
		return ckzSpeed;
	}

	public void setCkzSpeed(String ckzSpeed) {
		this.ckzSpeed = ckzSpeed;
	}

	public String getZgzSpeed() {
		return zgzSpeed;
	}

	public void setZgzSpeed(String zgzSpeed) {
		this.zgzSpeed = zgzSpeed;
	}

	public String getCgzSpeed() {
		return cgzSpeed;
	}

	public void setCgzSpeed(String cgzSpeed) {
		this.cgzSpeed = cgzSpeed;
	}

	public String getZzdElectric() {
		return zzdElectric;
	}

	public void setZzdElectric(String zzdElectric) {
		this.zzdElectric = zzdElectric;
	}

	public String getCzdElectric() {
		return czdElectric;
	}

	public void setCzdElectric(String czdElectric) {
		this.czdElectric = czdElectric;
	}

	public String getZkzTime() {
		return zkzTime;
	}

	public void setZkzTime(String zkzTime) {
		this.zkzTime = zkzTime;
	}

	public String getCkzTime() {
		return ckzTime;
	}

	public void setCkzTime(String ckzTime) {
		this.ckzTime = ckzTime;
	}

	public String getZgzTime() {
		return zgzTime;
	}

	public void setZgzTime(String zgzTime) {
		this.zgzTime = zgzTime;
	}

	public String getCgzTime() {
		return cgzTime;
	}

	public void setCgzTime(String cgzTime) {
		this.cgzTime = cgzTime;
	}

	public String getZkzAngle() {
		return zkzAngle;
	}

	public void setZkzAngle(String zkzAngle) {
		this.zkzAngle = zkzAngle;
	}

	public String getCkzAngle() {
		return ckzAngle;
	}

	public void setCkzAngle(String ckzAngle) {
		this.ckzAngle = ckzAngle;
	}

	public String getQtmcs() {
		return qtmcs;
	}

	public void setQtmcs(String qtmcs) {
		this.qtmcs = qtmcs;
	}

	public String getQthfTime() {
		return qthfTime;
	}

	public void setQthfTime(String qthfTime) {
		this.qthfTime = qthfTime;
	}

	public String getZdftAngle() {
		return zdftAngle;
	}

	public void setZdftAngle(String zdftAngle) {
		this.zdftAngle = zdftAngle;
	}

	public String getZdModel() {
		return zdModel;
	}

	public void setZdModel(String zdModel) {
		this.zdModel = zdModel;
	}

	
	public String getKzjgTime() {
		return kzjgTime;
	}

	public void setKzjgTime(String kzjgTime) {
		this.kzjgTime = kzjgTime;
	}


	public String getSubnetMask() {
		return subnetMask;
	}

	public void setSubnetMask(String subnetMask) {
		this.subnetMask = subnetMask;
	}

	public String getEquipReq() {
		return equipReq;
	}

	public void setEquipReq(String equipReq) {
		this.equipReq = equipReq;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return this.id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/*public Integer getDeptParentId() {
		return deptParentId;
	}

	public void setDeptParentId(Integer deptParentId) {
		this.deptParentId = deptParentId;
	}

	public String getDeptParentName() {
		return deptParentName;
	}

	public void setDeptParentName(String deptParentName) {
		this.deptParentName = deptParentName;
	}*/

	public String getIp() {
		return ip;
	}

	public Integer getBranchesType() {
		return branchesType;
	}

	public void setBranchesType(Integer branchesType) {
		this.branchesType = branchesType;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getGateWay() {
		return gateWay;
	}

	public void setGateWay(String gateWay) {
		this.gateWay = gateWay;
	}

	public String getIpCenter() {
		return ipCenter;
	}

	public void setIpCenter(String ipCenter) {
		this.ipCenter = ipCenter;
	}

	public Integer getPortCenter() {
		return portCenter;
	}

	public void setPortCenter(Integer portCenter) {
		this.portCenter = portCenter;
	}

	public String getBranchesName() {
		return branchesName;
	}

	public void setBranchesName(String branchesName) {
		this.branchesName = branchesName;
	}

	public String getPortPrint() {
		return portPrint;
	}

	public void setPortPrint(String portPrint) {
		this.portPrint = portPrint;
	}

	public String getEquipSn() {
		return equipSn;
	}

	public void setEquipSn(String equipSn) {
		this.equipSn = equipSn;
	}

}
