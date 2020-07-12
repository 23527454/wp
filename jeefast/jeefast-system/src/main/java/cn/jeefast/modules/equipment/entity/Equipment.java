package cn.jeefast.modules.equipment.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

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
	private Long id;
	
	@TableField("dept_id")
	@NotNull(message="所属机构不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private Integer deptId;
	
	@TableField(exist=false)
	private String deptName;
	
/*	private Integer deptParentId;
	
	private String deptParentName;*/
	
	@TableField("branches_type")
	private Integer branchesType;
	
	@TableField("ip")
	@NotNull(message="ip不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Pattern(message="ip格式不对",regexp="([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
	private String ip;
	
	@TableField("port")
	@NotNull(message="端口不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Range(min=0,max=65535,message="端口号范围0~65535",groups = {AddGroup.class, UpdateGroup.class})
	private int port;
	@TableField(exist=false)
	private String oldIp;
	@TableField(exist=false)
	private int oldPort;
	
	@TableField("gateway")
	@NotNull(message="网关不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String gateWay;
	
	@TableField("ip_center")
	@NotNull(message="上传地址不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String ipCenter;
	
	@TableField("port_center")
	@NotNull(message="上传端口不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Range(min=0,max=65535,message="端口号范围0~65535",groups = {AddGroup.class, UpdateGroup.class})
	private int portCenter;
	
	@TableField(exist=false)
	private String branchesName;
	
	@TableField("port_print")
	private String portPrint;
	
	@TableField("equip_sn")
	//@NotNull(message="设备sn码不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String equipSn;
	
	@TableField("create_user")
	private String createUser;
	
	@TableField("create_time")
	private Date createTime;
	
	//设备序列号
	@TableField("equip_req")
	@NotNull(message="设备序列号不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String equipReq;
	
	@TableField("del_flag")
	private Integer delFlag;
	
	//控制器地址
	@TableField("address")
	private String address;
	
	//子网掩码
	@TableField("subnet_mask")
	@NotNull(message="子网掩码不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String subnetMask;
	
	private String modifyUser;
	
	private Timestamp modifyTime;
	
	@TableField(exist=false)
	private String password;
	
	@TableField("other_name")
	@NotNull(message="设备名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String otherName;
	
	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	
	public Timestamp getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getOldIp() {
		return oldIp;
	}

	public void setOldIp(String oldIp) {
		this.oldIp = oldIp;
	}

	public int getOldPort() {
		return oldPort;
	}

	public void setOldPort(int oldPort) {
		this.oldPort = oldPort;
	}

	//版本号
	@TableField(exist=false)
	private String version;
	//闸机类型
	@TableField(exist=false)
	private String zjType;
	
	//电机类型
	@TableField(exist=false)
	private String djType;
	
	//闸机模式
	@TableField(exist=false)
	private String zjModel;
	
	//左通行模式
	@TableField(exist=false)
	private String leftCrossModel;
	
	//右通行模式
	@TableField(exist=false)
	private String rightCrossModel;
	
	//记忆功能
	@TableField(exist=false)
	private String remember;
	
	//反向物品穿行
	@TableField(exist=false)
	private String fxwpCross;
	
	//儿童刷卡通行
	@TableField(exist=false)
	private String babyCross;
	
	//闸机运行模式
	@TableField(exist=false)
	private String zjWorkModel;
	
	//翼闸尾随关闸
	@TableField(exist=false)
	private String yzwsgzModel;
	
	//主马达运行速度
	@TableField(exist=false)
	private String zmdWorkSpeed;
	
	//副马达运行速度
	@TableField(exist=false)
	private String fmdWorkSpeed;
	
	//马达最大运行时间
	@TableField(exist=false)
	private String mdMaxWorkTime;
	
	//红外检测时间
	@TableField(exist=false)
	private String hwjcTime;
	
	//通行间隔时间
	@TableField(exist=false)
	private String txjgTime;
	
	//等待人员进入时间
	@TableField(exist=false)
	private String ddryjrTime;
	
	//人员滞留时间
	@TableField(exist=false)
	private String ryzlTime;
	
	//延时关闸时间
	@TableField(exist=false)
	private String ysgzTime;
	
	//自由通行间隔时间
	@TableField(exist=false)
	private String zytxjgTime;
	
	//闯入报警
	@TableField(exist=false)
	private String crAlarm;
	
	//尾随报警
	@TableField(exist=false)
	private String wsAlarm;
	
	//滞留报警
	@TableField(exist=false)
	private String zlAlarm;
	
	//自检报警
	@TableField(exist=false)
	private String zjAlarm;
	
	//潜回报警
	@TableField(exist=false)
	private String qhAlarm;
	
	//时钟
	@TableField(exist=false)
	private String clock;
	
	//有效期
	@TableField(exist=false)
	private String indate;
	
	//挡板材料
	@TableField(exist=false)
	private String dbcl;
	
	//主开闸速度
	@TableField(exist=false)
	private String zkzSpeed;
	
	//从开闸速度
	@TableField(exist=false)
	private String ckzSpeed;
	
	//主关闸速度
	@TableField(exist=false)
	private String zgzSpeed;
	
	//从关闸速度
	@TableField(exist=false)
	private String cgzSpeed;
	
	//主阻挡电流
	@TableField(exist=false)
	private String zzdElectric;
	
	//从阻挡电流
	@TableField(exist=false)
	private String czdElectric;
	
	//主开闸时间
	@TableField(exist=false)
	private String zkzTime;
	
	@TableField(exist=false)
	private String ckzTime;
	
	//从关闸时间
	@TableField(exist=false)
	private String zgzTime;
	
	@TableField(exist=false)
	private String cgzTime;
	
	//主开闸角度
	@TableField(exist=false)
	private String zkzAngle;
	
	//从开闸角度
	@TableField(exist=false)
	private String ckzAngle;
	
	//强推脉冲数
	@TableField(exist=false)
	private String qtmcs;
	
	//强退恢复时间
	@TableField(exist=false)
	private String qthfTime;
	
	//阻挡反弹角度
	@TableField(exist=false)
	private String zdftAngle;
	
	//阻挡模式
	@TableField(exist=false)
	private String zdModel;
	
	//开闸间隔时间  
	@TableField(exist=false)
	private String kzjgTime;
	
	@TableField(exist=false)
	private String gzjgTime;
	
	@TableField(exist=false)
	private ReadRecord readRecord;
	@TableField(exist=false)
	private ReadStatusEntity readStatus;
	
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
	
	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

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

	public ReadStatusEntity getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(ReadStatusEntity readStatus) {
		this.readStatus = readStatus;
	}

	public ReadRecord getReadRecord() {
		return readRecord;
	}

	public void setReadRecord(ReadRecord readRecord) {
		this.readRecord = readRecord;
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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
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

	public int getPortCenter() {
		return portCenter;
	}

	public void setPortCenter(int portCenter) {
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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
}
