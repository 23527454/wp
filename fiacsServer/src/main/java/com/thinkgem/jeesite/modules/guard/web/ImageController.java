/**
 * Copyright &copy; 2012-2017 <a href="https://www.jumbo-soft.com/">Jumbo Soft</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.guard.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.JsonDateValueProcessor;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.guard.dao.CompanyDao;
import com.thinkgem.jeesite.modules.guard.dao.CompanyExDao;
import com.thinkgem.jeesite.modules.guard.dao.StaffImageDao;
import com.thinkgem.jeesite.modules.guard.entity.Car;
import com.thinkgem.jeesite.modules.guard.entity.CarImage;
import com.thinkgem.jeesite.modules.guard.entity.CompanyEx;
import com.thinkgem.jeesite.modules.guard.entity.EventDetail;
import com.thinkgem.jeesite.modules.guard.entity.Staff;
import com.thinkgem.jeesite.modules.guard.entity.StaffImage;
import com.thinkgem.jeesite.modules.guard.service.CarImageService;
import com.thinkgem.jeesite.modules.guard.service.CarService;
import com.thinkgem.jeesite.modules.guard.service.EventDetailService;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 事件详情Controller
 * @author Jumbo
 * @version 2017-07-14
 */
@Controller
@RequestMapping(value = "${adminPath}/guard/image")
public class ImageController extends BaseController {

	@Autowired
	private EventDetailService eventDetailService;
	@Autowired
	private CarService carService;
	@Autowired
	private CarImageService carImageService;
	@Autowired
	private StaffImageDao staffImageDao;
	@Autowired
	private CompanyExDao companyExDao;
	@Autowired
	private UserDao userDao;
	
	private static final String NO_PIC  = "89504E470D0A1A0A0000000D494844520000016A0000010008060000009AB73CDF0000200049444154785EED9D7BBC244575C7CFE93BEB82F8621376A7AAE6820BF20A12301F31065F8080EC121E1A429010E48391242A0A041F8418411335411405A348084224688C1F5F1182B2A0ACA0468D112109AEBAECDEE9EAB92B0B4840DCDD3B7DF22932972C70E776CF54774FF5CCAFFFD93FA6EAD4E96F9DF96EDD9A7E30E1000110000110089A00079D1D9203011000011020881A450002200002811380A8039F20A4070220000210356A000440000402270051073E41480F04400004206AD40008800008044E00A20E7C82901E0880000840D4A80110000110089C00441DF804213D1000011080A8510320000220103801883AF009427A200002200051A306400004402070021075E01384F44000044000A2460D8000088040E00420EAC02708E98100088000448D1A0001100081C00940D4814F10D20301100001881A350002200002811380A8039F20A4070220000210356A000440000402270051073E41480F04400004206AD40008800008044E00A20E7C82901E0880000840D4A80110000110089C00441DF804213D1000011080A8510320000220103801883AF009427A200002200051A306400004402070021075E01384F44000044000A2460D8000088040E00420EAC02708E98100088000448D1A0001100081C00940D4814F10D20301100001881A350002200002811380A8039F20A4070220000210356A000440000402270051073E41480F04400004206AD40008800008044E00A20E7C82901E0880000840D4193560ADBD85880E41A980000894434044DE658C796739D1C7232A440D518F4725E32C6A4B00A2CE9E3A881AA2CEAE12B40081120940D4D970216A883ABB4AD002044A24005167C385A821EAEC2A410B10289100449D0D17A286A8B3AB042D40A044021075365C881AA2CEAE12B40081120940D4D97021EA0C46711CAF61E6C31669F6F56CCC680102134DE0594474403F022272A131E68289269471F210B5E78A5A29D560E62E8A0C04406061029D4EE7B0344DD72C226A5C470D51FB7D7DB26E7881A8FDF8A2F7F81380A8FDE7182B6AACA8FDAB08114060110210B57F7940D410B57F1521020840D4A5D600440D51975A60080E025851FBD700440D51FB571122800056D4A5D600440D51975A60080E025851FBD700440D51FB571122800056D4A5D600440D51975A60080E025851FBD700440D51FB571122800056D4A5D600440D51975A60080E025851FBD700440D51FB571122800056D4A5D600440D51975A60080E025851FBD700440D51FB571122800056D4A5D600440D51975A60080E025851FBD700440D51FB571122800056D4A5D600440D51975A60080E025851FBD700440D51FB571122800056D4A5D600440D51975A60080E025851FBD700440D51FB571122800056D4A5D600440D51975A60080E025851FBD700440D51FB571122800056D4A5D600449D81378EE335CC7C58BF665A6B302CB544C73BF8A64D9B9ED6ED760F4AD3F4D9CCAC88E85E22DAD0ED76BF333D3D7DDF389C7D9224878AC8CDFDCE45442E34C65C300EE75AD6394032107559B585B88B1088E3F824223A8B997F731181FD308AA2CB95521FA9334C88DA7FF6206A88DABF8A10213781D9D9D93DBADDEE75447450DE4ECCFC63663EA5D96C7E3B6F9F90DA41D4FEB30151678BFA66663E145B1FFEC536E911E2383E96993F45443B0EC9E26CADF52543F61D59B71C3F2662EB23637620EA0C40D6DA5B88E8907ECD94520D66EE8EEC5B80816B41204992D522F205226AF8242C22671A632EF3895175DF1CA27E9731E69D55E755A7F120EAEC15357E4CAC534507986BBBDDDE2B8AA2BB0B4CED28ADF58D05C62B3514B63EFCF142D45851FB5711222C4AC05AFB65225A5D1426B767DD6C36F7A9CB5F725851FBCF3C449DBDA2C61EB57F9D4D6C84388E5FCCCC6B8B062022AF33C6FC5DD171CB889743D4D8A3C61EB55FE9618FDA8FDFA4F7B6D65E4B442717CD4144BE658CF9ADA2E396112F87A8B1470D51FB951E6E78F1E337C9BD676666769C9A9A7237ADEC500607665EA994BAA78CD845C6C41EB53F4D6C7D608FDABF8A106141029D4E67FF344DEF280B8F881C6F8C715792047D6045ED3F3D103544ED5F4588B02081388E8F64E6D2AECE60E637D6E1AE4588DAFF0B025167308CE3183F26FAD7D9444688E3F8F77A37B89472FE22528BBDDD1CA2C68F89D8A3F6FB8EE0C7443F7E93DC3B8EE32398F92B2532384B6BFDA112E317123A87A86BF11F4E2130860C821535B63E862C1D74CB2250F61E35339FA894FA4C561EA3FE1CA2F69F01881AA2F6AF22445890C09D77DEF99465CB96DD4F444F2D03515DAEFA80A8FD671FA286A8FDAB0811FA12B0D6BA15EF09452362E63B945207141DB78C7810B53F55883A8321AEA3F62FB2498ED07B1893BB85BCE8E3CD5AEB0F171DB48C78B88EDA9F2A448D15B57F1521C2A204ACB5B711D1C10562DAA094DA9399B71518B3B4505851FBA385A8B1A2F6AF22445894409224FB89C8778BBA43318AA297379BCDBEAFB60A6D3AB0A2F69F11881A2B6AFF2A42844C02BD97067C9E88BCBE73781E7526EAB16CE055346349E4092785EBA8276196AB39476BEDAB88C83DA46998677F7445E4ACBABD34C091C5D6877F7D41D4D8FA788CC0FAF5EB9FB572E5CA07FCCB0A11FA114892E40522F271221AE48A8D1F89C81B8C3137D5912CB63EFC670DA2CE16F544DC421EC7F16B99F90322728831E6FBFEA585088B11B0D6FE0E119DBED80B0598798D885CADB5FE873AD3CCB1A2C62DE419130C51678B7AEC5FC565AD3D83882EEFA17840445E6E8CF9F73ACBA12EB96FDEBCF9195BB76E7D519AA686999713D17DCCBC716E6EEE5BD3D3D3EE11A9B53FB0A2F69F42883A83E1B8EF515B6B5F4744EE4FF1ED8F07D3347D59ABD5FA0FFF12438449279063458D677D6045EDF735196751C771FC4666BEB40F21C8DAAF74D0BB4700A2F62F05ACA82774459D21E9792A0F32F3E14AA9EFF8971A224C2A0188DA7FE621EA0914759224E78AC84539CBE721663E0CB2CE490BCD9E4400A2F62F0A887AC2449D24C93BDC03E7072C1DC87A406068FEFF04206AFF6A80A82748D4711C5FC4CCE70E593690F590E026BD1B44ED5F0110F58488DA53D2F394206BFFEFDCC44580A8FDA71CA29E0051274972A988BCD1BF5C1E8DF09088BCC218737B41F11066CC0940D4FE130C518FB9A80B96F43CAD4744E470C8DAFF0B380911206AFF5986A8C758D4D65A77238BBBA1A58C03B22E83EA18C684A8FD2715A21E43518B082749E26E092F4BD25859FB7FF726260244ED3FD510F59889BA27E94F10D1A9FEE5912B0256D6B9304D6E2388DA7FEE21EA3112F50824FDD8CA9A99572BA5BEE65F9288306E04206AFF1985A8C744D42232D5E9743E292227F997C55011B630F35190F550ECC6BA1344ED3FBD10F51888DA493A4992CF10D12BFD4BC22B0264ED856F3C3B43D4FEF30A51D75CD422D24892E49F0290F43C49C8DAFF7B395611206AFFE984A86B2CEA9EA4DD0B538FF62F8542236C1191638C315F2D342A82D5920044ED3F6D10754D452D224B9224F95C80929E27BA4D448E86ACFDBFA4758F0051FBCF20445D4351F724FD65223AC2BF044A8D0059978AB71EC1216AFF7982A86B26EA75EBD62DDD69A79DBE5403496365EDFFFD1C8B0810B5FF3442D43512754FD2FF4A4487F84F7DA511B0B2AE1477588341D4FEF30151D744D43333333B4E4D4D5D5F43493FB6B266E6572AA5DC960D8E09220051FB4F36445D0351F7247D13111DEC3FE5238D30C7CCC743D6239D83CA0787A8FD9143D4818B7AD3A64D4F9B9B9BBB710C243D4F1AB2F6FFDED62A0244ED3F5D1075C0A2DEBC79F333B66CD9E256D207F94F75501120EBA0A6A3DC64206A7FBE1075A0A2EE49FAEB4474A0FF34071901B20E725A8A4F0AA2F6670A510728EA0D1B36ECBC64C9929BC758D28F6D8310D1895A6B77E30E8E31250051FB4F2C441D98A87B925E4B44FBF94F6F2D227489E87721EB5ACCD5504942D443617B5C27883A20515B6B7F9588DC339D2745D2F3F4216BFFEF72B011206AFFA981A80311754FD2DF20A2BDFDA7B5961120EB5A4E5B76D2107536A3AC16107500A29E9D9D5DD1ED76DD0F87932A69ACACB3BEA935FE1CA2F69F3C887AC4A2EE49FA7622DADD7F3AC7224257444E31C67C6A2CCE06274110B57F1140D42314F5CCCC8C999A9ABA15927ED22488889C0C59FB7FC143880051FBCF02443D22515B6B772322B7DDE1FEC5F1640290F598540544ED3F9110F50844DD93F46D4464FCA770AC2340D66330BD10B5FF2442D4158BBAD3E9EC9EA6A9DBEE80A4F3D5AF30F3694AA96BF23547ABD00840D4FE3302515728EA9EA4DD0F872BFCA76EA22240D6359E6E88DA7FF220EA8A441DC7F1DECCECF6A421E9E1EA76A2659D24C9B345E45A1179B53166E3700847D30BA2F6E70E515720EA9EA4DDCD2CEECE431CC313984859F724ED1E2BD06266CBCC2F6E369BEB87C7586D4F88DA9F37445DB2A89324D94F44DC6DE190B47FBDBA081325EB76BBDD8AA2E89B4ED2DBE1DBC4CC872AA5FEB318A4E54681A8FDF942D4258ABA2769B712DAD97FAA10613B0242447FA4B5BE629CA9F424EDEAE7D90B9CE703DD6EF765D3D3D37784CE00A2F69F2188BA2451B7DBED03A328728F2A85A4FDEBB45F8433C655D619929EE7F120111DA6B5FE5E7988FD2343D4FE0C21EA12449D24C94122E2DECCF20CFF2942840C026327EB9C929EC7F270144587379BCD6F855A2910B5FFCC40D4058BBA2769B7927E9AFFF420424E026323EB01253D8FE711227A85D6DA6D93047740D4FE5302511728EA388E0F6666F7225A48DABF36078D507B590F29E9794E5BD2343DBAD56AAD19145CD9ED216A7FC2107541A2EE49DA6D77ECE83F2D88300C011139D31873D9307D47DDC753D2F3E96F63E6E39452378CFA7CB61F1FA2F69F0D88BA0051773A9D9788C8F590B47F41FA46A8A3AC0B92F43CBA391139C118F3055F9645F587A8FD4942D49EA276D7B38AC82DFE5381084511A893AC0B96F43CC294884ED55A5F5B14539F3810B50FBDFFEB0B5167308CE3780D331FB648B35F12D10EFE538108451260E6739552171719B3E8589B366D6ACECDCDB93B56F7283A762F5E10FBF64992B8C58CFB817DC143442E34C65C501283B1080B517BAEA8C7A20AC6F424425E593B4977BBDDB522F29C32F133F3EB95521F2D738CACD858516711CAFE1CA286A8B3ABA4C62D98F92D4AA9F787740A55497ABB733E5B6B7DC9A81840D4FEE4216A88DABF8A028F1092AC4720E947676794DB0B10B5FF1704A286A8FDABA806114290F5A824BDDDF45CA2B53EBBEAE982A8FD8943D4190C73FC98E83F0B88501581F3B5D6EFA96AB0EDC7E9FD70E8DEECB3E728C69F1F93992F554ABDA9E81C9224F935227A6E9AA6FB455174A0883489E899BDC728B8E7DDF4FDC19D993F323737F7DEE9E9E9B8E8BCC6251E448D15F5B8D472AEF318C5CA3A8095F4E3D830F3DF379BCD3F6466F714C2818F9999991D99D9DD85FB52227A0933BFB0A07B08121171B7C1AF75AFAB6BB55A3F1C36C7814F2AF00E1035441D7889169F5E95FBB5A1497A3B9AD72AA54E656677CD75E6212251A7D3392A4DD3D398F958225A9AD9C9B3817B4982885C23229F30C6DCED19AED6DD21EA8CE9C3D647ADEB7BB1E4DFA3B53EBFCCB3EB6D77B89746EC5DE638C3C61691CF18634E5CACFFC68D1BF592254BDE2422A713D12EC38EE5DB4F44BE4D44571863AEF48D55C7FE103556D475ACDB42721691F71B63DE5248B027044992C449EDF6B2AF932E20F71B9452C731F3B6ED63F55E1F771E119D4C444B0A18A7A81031335F323535F5B1E5CB973F5454D0D0E340D45851875EA365E7F73EADB51352618793B488B81719EF5B58D07203DDA0B55EED86702BE846A3F121223AA1DC21BDA3FF0F33BF5B29759177A41A0480A8B1A2AE4199969B62912BEB9EA4DDD51DFB949B75E1D1D732F39744E42FEAF4985E11713F389EAEB5FE6EE144020A085143D40195E3E85261E6CB945267FA64506349FB9CF6F67D1F20A29F88C84F98F9C744B49999EF4BD3F47E1179686A6A6A67115926223B33F30A22DA8B88DC2DF445ECE18FE41AF1A2C065C581A821EAAC1A9998CF7D643DA192DE42446E8BE7FA344D6F68B55A3F1AA65844A4D1E9745E4444ABD2345DCDCCFB0F19E787535353C7359BCDF5C3F40FB90F449D313B4992DC24222F0F7912915B710444E46263CCB98344EC49DA5DDDE16EFA18F72321A22F8BC8BF30F357B5D6BF28FA846766664CA3D1384E448E21A24307BC14D0ED5D9FAE94FAE7A2F31A653C883A833E2ECF1B65798E6CECDC7F46F724ED9E47BEDFC8B2AD66E05844DEA1B5BE3AEFB5D745A4D56EB77F8599DFC5CC671051236F4C1179B731C6EDB78FC5015163EB632C0AB9E893C8B30D3221DB1D6EC5FCDE6EB77BF1F4F4B47B89EE488E76BBBD17335FCCCCBF9D370166BE4A29E5AEFFAEFD015143D4B52FE2124FE00AADB55BC93DE99810497F72C992256FDD65975DDC764710471CC787BB6783F47E88CC93D357BADDEEF1A3FC4F264F92596D206A883AAB4626FDF327C97A666666D9D4D4D46D35BC042FD75CBA2B36BADDEEA9AD56EB9BB93A8CA091B5D65DFB9EF7015B376AAD8F1A419A850D095143D48515D318077A4CD64ED251147D6DD82B13426724221FDDBA75EB392B57AE74AF980BFA68B7DB074451F4E99C97F77D5629756295FBEB45C283A821EA22EB699C635DD1ED76DF3EC6927EF4DD9FA378BAA04FD1AC5FBF7E87A54B977E9C88FE20479C8F69ADFF2447BBE09A40D4195382AB3E82ABD95126743F11B9672B8FDBF13F44F4F4ED4EEA2CADB5BB8DBC36479224EF1091776525CCCCAF514A5D93D52EB4CF216AACA843AB49E4532D8159227277093EEE1091371863FEB6DA54FC464B92E435EE91A819511E8AA2E8D7EB76530C440D51FB7D3BD0BBCE049EB8927EDCB94451F4BA66B3F977753A416BED5944F4C18C9CFF5D2975509DF6AB216A88BA4EDF43E45A1C81B93C379030F3694AA9AB8B1BB6FC48D6DA8F12D11F678CF4E75AEBBF2A3F9B624680A821EA622A09516A45208AA2F3D2347D6FCEA47F5F6BFD8F39DB06D12C8EE39B99D9DD7EDEF788A2688F66B3F9D32012CE4802A286A8EB50A7C8B158021FD25A9FD5E9745E9EA6E94D794233F3EFD6E9F919EED6F3288AEE5A68FF7DFE7C45E41663CC6179CE7FD46D206A887AD43588F1AB25708FD67AE5FC90D65A7723C80D79521091E38D315FC8D33684364992BC4C44DCC3B2FA1E22729231C65D8B1DF4015143D4411728922B9480A469BACF131F471AC7F1B1CC9C4BC0CC7CB452EAFA42B32A3198B5F6DD44F4E78B0CB14E6BED9E8B1DF4015143D4411728922B94C0F55AEBA3178A68ADFD1D22CA7C342833CF31F3AA66B3996BCBA4D0EC8708B66EDDBAA53BEDB4D33A229A5EA4FB295AEB6B87085F5917881AA2AEACD830D0E808307377E9D2A5BFB26CD9B29FF7CBA2D3E99C94A6E97539B27CC4C95A29E55E1A10FC11C7B17B79EFE7175B552BA5F6666609F564206A883AD4DA445E05121091AB8C31998FFC6CB7DBA7465194E772BC0745649531E6F602D32C2D94B5D63D33FC90450678A5D67A319997965B9EC01035449DA74ED0A6DE04BA8D46E359CB972F7F28CF69C471FC5A66CE73A3CBBDEEF5597578B16C1CC74730F357FA9D3F337F5129755C1E3EA368035143D4A3A83B8C59210111B9CE1873F220435A6BDD0D23EEC691450F664EA2285AB562C58A1F64B51DF5E74992FC40447EBD4F1EDD6DDBB6EDB2DB6EBBB9E7B9047740D41075704589840A2590361A8D67E65D4D6F3FB2B5F64D4494E7E14C1B7B7BD6FF5968E605078BE3F8D5CCDCF7C61D667EA352CABD9420B803A286A8832B4A24542801F702DA23878D9824C99F8AC8FBB3FABB970DCCCDCDAD9E9E9E765758047B586BDD8AF9597D12BC4D6BFDE2109387A821EA10EB12391547E0D95AEB0D3EE1E2387E3B33E7B9DDFCBF7A7BD65EE3F9E49AD537E3392043FFF59135AEEFE7103544ED5B43E81F2E811F69ADF72E22BDBCCF7B16913BBADDEEAA5D77DDD516316ED13192247981887C7B91B8EEC7D17F2D7A5CDF78103544ED5B43E81F2E813FD55A7FA0A8F472DCE5373FD4F77A7BD63F2B6AEC22E3586B3713D1B2856232F35F2BA5DE5EE47845C482A821EA22EA0831C224B0AFD6FABF8B4C2D4992F789C8DBB26232F337972E5DBA6AB11B6CB26294F5B9B5D6DD81E9EEC47CD22122DF32C6FC5659630F1B17A286A887AD1DF40B9BC0CFB4D6CBCB483149928B45E49C1CB16FEDED59FF2247DBCA9A2449F20611B9ACCF805BB4D63B54964CCE81206A883A67A9A0599D0888C8678C31279695731CC797BACBD9B2E28BC89A2449563DFFF9CFDF96D5B6AACFDBEDF68151147DBFDF78699A4EB75AAD7655F9E41907A286A8F3D409DAD48C807BD1AB31E69D65A66DADFD1811FD518E316ED05AAFCED1AE9226BD0735B9B7AE2F7888C891C698AF56924CCE41206A883A67A9A0599D0888C8AB8D319F2A3BE72449AE1491CC678884768BB6B5D65D42B8EB427C44E44DC6984BCB6637487C881AA21EA45ED0B62604D2343DB0D56A55725BB7B5F61F88E8941C683EABB53E2147BBD29B586B6F24A27E3702BD4F6B7D5EE9490C3000449D012B8EE335CC5C8BD7F50C30EF683AE6049879A552EA9EAA4E338EE34F31F3EFE518EF3AADF540CF1DC91173E026711C5FE55EDCDBA7E3E55AEBAC97E30E3CA64F07881A2B6A9FFA41DF400974BBDDD6F4F4745C657AD6DACF12D1AB728C798DD6FA3539DA95D6248EE38B98F9DC850660E64F2BA54E2A6DF0210243D410F51065832EA11388A26845B3D9DC54759E49927C51448EC91A5744AE34C6FC6156BBB23E8FE3F86DCCFCBE3EF16FD45ABB7749067340D4107530C588440A25B08BD6DA3D2FBAD24344384912F7B2DC57640DCCCC1F554ABD3EAB5D199F5B6BCF20A2CBFBC40EEEE14C1035445DC6F70031474FC0FB614CC39E82883CA5D3E9DC2022797EDBF9B0D6FACDC38E356CBF8C47B8DEA4B53E62D8D865F483A821EA32EA0A31474C80999FAB94BA6B5469743A9D9DD234752BEB9764E52022171B6316DC2FCEEA3BECE7D6DAF389E82FFBF4FF9CD63ACF5EFBB0C30FDC0FA286A8072E1A74089F4014452F6C369B8B3D25AEF493D8B061C3CE8D46E30666FECD1C83BD576BFD6739DA15D224E3992523FFB1F38927095143D485143E8284458099572BA5DC8A76A447A7D359DE5B59FF46562255DC4D399F83B5D6BD13F2B50BE5C4CC9729A5CECCCAB7CACF216A88BACA7AC358D51138476BFDC1EA86EB3FD2BDF7DE6BB66DDBE6F6ACF7CF91CF3BB4D6FDB6247274CFD7C45A7B1B111DBC506B1139CF18D3EF8A907C0314DC0AA286A80B2E29840B84C0C7B5D6799EC35149BAF7DC73CFCA254B96B86D90CC171930F3DB94527F536662D65AF746F69DFA8CF14AADF5E7CB1C7FD0D81035443D68CDA07D3D08DCAAB57E5948A9B6DBEDBDA22872DB31BBE7C8ABB4BF086667675774BBDD4EBF1CD234DDBBD56AFD28478E953581A821EACA8A0D03554AE0974AA9A733F35CA5A3660C9624C97E22E2643D9D9597889C698CE9F7DCE8ACEE7D3FB7D6BA9706B897072C74A44AA925CC9C0E3D40091D216A88BA84B242C84008BC546BBD36905C1E4B238EE3E731B393F58AACDCD234FDE356ABD5EFC694ACEE0B7E1EC7F14798B9DF8D36FFA1B57EDE50814BEC045143D4259617428F9200335FA094BA709439F41BBBF7925927EB05DF5DB87D3F663E5D29755551E761AD75AF27EBB7577E89D6FAECA2C62A2A0E440D5117554B88131801F7DE42A5D482573684906A1CC72FEAADAC9F9E239F3FD05A7F3247BB459B743A9DDDD334FD49BF462272AC31E64BBEE314DD1FA286A88BAE29C40B88401445BB379BCDF501A5F4B854DAEDF6A151145D4F4499EF291491938C319FF639978C37A94B14454F6F369B0FFB8C51465F881AA22EA3AE10331C0295DEF137CC69C7717C646F651DE5E8FF2AADF5E772B45BB089B5D6BD0BD12CF4A1887CDB18F3C2616397D90FA286A8CBAC2FC41E310166B64AA905C534E2D41E377C9224478BC8BFE4C9294DD3635AAD56AEB6DBC7B3D6BA47972E76B7E69BB5D61FCE9343D56D206A88BAEA9AC3781513706F32514A5D5DF1B0030F373B3B7B7CB7DBCDB35A16225AA5B576AFD3CA7DC4717C33331FDAA743CACC4DA5D4CF7207ACB021440D5157586E186A4404EE564AEDCBCC4E70411FED76FBC4288AF2EC436F61E6554AA95BF29C501CC7BFC1CCDFEBD7D6ADE68D31992F3CC83356196D206A88BA8CBA42CCC00888C871C6982F0696D682E9586B7F9F88F25CE1F19088AC32C67C23EBBCACB56EA57EFC22ED82BB6D7CFB5C216A883AABC6F1F97810F8A9526A1F66DE5687D34992E43411C9BC765A44EE8FA2E828A5D4BFF53BAF24490E15919BFB7DCECC3F6E369B7B85FC1707440D51D7E17B8B1C8B2170BED6FA3DC5842A3F4AC6EBB21E4B809967D334752BEBEF3F312B1159922489BBC1A5EFF34544E46463CC75E59FD1F02340D410F5F0D5839E7523F00811EDABB5DE5097C4E3387E837B3E748E7CDB5114AD6A369B776EDF36E34D2EAEE9DD5AEB7D72C41F6913881AA21E690162F0CA09FC9BD63ACF1B572A4FACDF80D6DAB38828CFB3B5D7F7F6ACEF76B13A9DCE0BD334FD66C6899CA0B5FE6C3027DB2711881AA20EBD46915FF1043EA8B53EA7F8B0E5458CE3F8ADCCFCD73946B8DBADACE7E6E61E8CA2E807FD6E6EE9C5B9516BEDAEAD0EFE80A821EAE08B1409964220E8AB1C163AE31CDB18F3DDDCF6C7FD192FD6FDA588EC6D8CD9580ADD828342D41075C12585703521F08B344D0F6EB55A6ED5599B2349920B44E49DBE0933F3B94AA98B7DE354D51FA2CE20DD6EB70F989A9ADAB9AA09C13820500481344D778FA2E863EEAA8745E2751A8DC6F3962F5FDEF76D2745E452740C6BED5F1191CF1BCB6F554A1D12F2E5784F640651175D458807028110B0D6BAE72A7F20239DBB1A8DC6E17593751CC717B955F110A81F48D374FF56ABE51ECE549B03A2AECD54215110189C80B5D63D0FE3C8AC3F1C99D9DD3472D7E0238CAE87B5F612227AF3201930F36F2BA5BE3C489F10DA42D421CC027200819208743A9DE5BD4BD4B25E28FB20339FA4945AECE9722565397CD84EA773799AA667E4892022EF37C6BC254FDBD0DA40D4A1CD08F201818209C4713CCDCCDF25A2E539427FF8E1871F7EEB9E7BEEB92547DB9136E9743AFBA769FAF93C6F3517911F18630E1C69C21E8343D41EF0D01504EA42C0BDFD3B4DD3DB98F999593933F31D447472C85B21D6DAF38828EFEDF07769AD9F9B75DE217F0E51873C3BC80D040A24D06EB70F8CA2E8AB44F4AB79C232F35F28A5DE9DA76D556DE238762FA5BD9A9973DD5DE9FED3514A1D50557E658D03519745167141204002D6DADD88680D11ED9133BDEFF5AE39FE5ACEF6A534BBEFBEFB507F136B0000033E494441549EF9C8238F9CCDCC835C43BD566BFDD25212AA3828445D31700C0702A3269024C92E22F215221A64CFF676667E4FD5574CF47275B7BBBF9E889E3100BBCF69AD5F3540FBA09B42D4414F0F9203817208CCCCCCECD868342E1391D30719C13DBB59443E2122D718636606E99BB7AD884CCDCECEAE4AD3F43544742C113D256F5F77EB78EF6DE5EE3FA2B13920EAB1994A9C08080C4EC05A7B0A115D41443B0CDE9BDC9B55BE2E22DF11916FB45AADCD43C478B44B92240711D10B44C4BD05FC08225A3144ACB573737327EDBAEBAE7688BE417781A8839E1E240702E513B0D6EE4B44D710D1F33D47DB2C223F66E69F10D14F89683333DF9FA6E90322F2D0D4D4D43211594644EE5FC3CCCF61E63D44E4399EE392885C688CB9C0374EA8FD21EA506706798140850444244A92E44C22FA4B227A5A8543FB0E75BBDBBE31C63CFA0CEA713D20EA719D599C17080C41A0DD6EB7A228726F54396E88EE5576D924227F668CB9B2CA41473516443D2AF21817040226E0AE5766E6F389E8D544D40828D575EE41530F3FFCF05575B87BB2286E10755124110704C690C0C68D1BF5D4D4D439CC7C2A11ED32AA53646677EDF7C79552FF34AA1C46392E443D4AFA181B046A42C05D3267AD5DCDCCA711D13144B4D873AE8B3A2BB77ABEBAD1685C59B7C7B01605603E0E445D3451C40381312760AD7DAA88BC9899DD5D7F2F2122773BF7D2024EDB5D29B25644D6A6697AEBF4F4B413350E2282A85106200002DE04DC9B9098F9D798797F667EAE88347B7712BA8740B93B0A9F4A440F10D1CF99F9E7699ABA4BF7D689C89D5114DDB175EBD63B76DB6D37F79E431C0B1080A8511620000220103801883AF009427A200002200051A306400004402070021075E01384F44000044000A2460D8000088040E00420EAC02708E98100088000448D1A0001100081C00940D4814F10D20301100001881A350002200002811380A8039F20A4070220000210356A000440000402270051073E41480F04400004206AD40008800008044E00A20E7C82901E0880000840D4A80110000110089C00441DF804213D1000011080A8510320000220103801883AF009427A200002200051A306400004402070021075E01384F44000044000A2460D8000088040E004FE1752426EF129A362B00000000049454E44AE426082";
	@ModelAttribute
	public EventDetail get(@RequestParam(required=false) String id) {
		EventDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eventDetailService.get(id);
		}
		if (entity == null){
			entity = new EventDetail();
		}
		return entity;
	}
	
	@RequiresPermissions("guard:eventDetail:view")
	@RequestMapping(value = {"list"})
	public String list(EventDetail eventDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EventDetail> page = eventDetailService.findPage(new Page<EventDetail>(request, response), eventDetail); 
		model.addAttribute("page", page);
		return "modules/guard/eventDetailList";
	}

	@RequestMapping(value = "")
	public void image(EventDetail eventDetail, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException{
		System.out.println(eventDetail.getBigDataFlag());
		if(StringUtils.isNotBlank(eventDetail.getId())){
			EventDetail r = get(eventDetail.getId());
			if(r!=null && r.getImageData()!=null && r.getImageData().length > 0){
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(r.getImageData()));
			    response.setContentType("image/jpg");  
			    OutputStream os = response.getOutputStream();  
			    ImageIO.write(image, "jpg", os);
			    return;
			}else{
				if(r!=null&&r.getImageData()==null){
					if(StringUtils.isNotEmpty(eventDetail.getBigDataFlag())){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						int i=0;
						while(i<5){
							r = get(eventDetail.getId());
							if(r.getImageData()==null){
								i++;
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}else{
								break;
							}
						}
						if(r!=null && r.getImageData()!=null && r.getImageData().length > 0){
							BufferedImage image = ImageIO.read(new ByteArrayInputStream(r.getImageData()));
							response.setContentType("image/jpg");
							OutputStream os = response.getOutputStream();
							ImageIO.write(image, "jpg", os);
						return;}
					}
				}
			}
		}
		responseNoPic(response);
	}
	
	@RequestMapping(value = {"car"})
	public void imageCar(Car car, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException{
		if(StringUtils.isNotBlank(car.getId())){
			CarImage r = carImageService.getByCarId(car.getId());
			if(r!=null && r.getImgData()!=null && r.getImgData().length > 0){
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(r.getImgData()));  
			    response.setContentType("image/jpg");  
			    OutputStream os = response.getOutputStream();  
			    ImageIO.write(image, "jpg", os);
			    return;
			}
		}
		responseNoPic(response);
	}
	
	@RequestMapping(value = {"staff"})
	public void imageStaff(StaffImage staffImage, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException{
		if(StringUtils.isNotBlank(staffImage.getId())){
			StaffImage r = staffImageDao.get(staffImage.getId());
			if(r!=null && r.getImgData()!=null && r.getImgData().length > 0){
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(r.getImgData()));
				response.setContentType("image/jpg");
			    OutputStream os = response.getOutputStream();
			    ImageIO.write(image, "jpg", os);
			    return;
			}
		}
		responseNoPic(response);
	}

	@RequestMapping(value = {"webCamStaff"})
	public void webCamStaff(String filePath,HttpServletRequest request, HttpServletResponse response, Model model) throws IOException{
		File file = new File(getJarPath()+"/"+filePath);
		if(!file.exists()){
			return;
		}
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(FileUtils.readFileToByteArray(file)));
		response.setContentType("image/jpg");
		OutputStream os = response.getOutputStream();
		ImageIO.write(image, "jpg", os);
	}
	
	private void responseNoPic(HttpServletResponse response) throws IOException{
//		byte[] b = hexStrToByteArray(NO_PIC);
//		BufferedImage image = ImageIO.read(new ByteArrayInputStream(b));
//	    response.setContentType("image/jpg");  
//	    OutputStream os = response.getOutputStream();  
//	    ImageIO.write(image, "jpg", os);
	}
	
	public static byte[] hexStrToByteArray(String str)
	{
	    if (str == null) {
	        return null;
	    }
	    if (str.length() == 0) {
	        return new byte[0];
	    }
	    byte[] byteArray = new byte[str.length() / 2];
	    for (int i = 0; i < byteArray.length; i++){
	        String subStr = str.substring(2 * i, 2 * i + 2);
	        byteArray[i] = ((byte)Integer.parseInt(subStr, 16));
	    }
	    return byteArray;
	}
	
	@RequestMapping(value = {"company"})
	public void imageCompany(CompanyEx companyEx, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException{
		if(StringUtils.isNotBlank(companyEx.getId())){
			CompanyEx r = companyExDao.get(companyEx.getId());
			if(r!=null && r.getImgData()!=null && r.getImgData().length > 0){
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(r.getImgData()));  
			    response.setContentType("image/jpg");  
			    OutputStream os = response.getOutputStream();  
			    ImageIO.write(image, "jpg", os);
			    return;
			}
		}
		responseNoPic(response);
	}
	
	@RequestMapping(value = {"user"})
	public void imageUser(User user, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException{
		if(StringUtils.isNotBlank(user.getId())){
			User r = userDao.get(user.getId());
			if(r!=null && r.getImgData()!=null && r.getImgData().length > 0){
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(r.getImgData()));  
			    response.setContentType("image/jpg");  
			    OutputStream os = response.getOutputStream();  
			    ImageIO.write(image, "jpg", os);
			    return;
			}
		}
		responseNoPic(response);
	}
	
	
	/**
	 * 获取列表数据（JSON）
	 * @param eventDetail
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("guard:eventDetail:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public String listData(EventDetail eventDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			eventDetail.setCreateBy(user);
		}
		
		String filterRules = request.getParameter("filterRules");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		if(StringUtils.isNoneBlank(filterRules)){
				JSONArray filter = JSONArray.fromObject(filterRules, jsonConfig);
				List<Field> fields = new ArrayList();
				Reflections.getAllFields(fields, EventDetail.class);
				if(filter!=null&& filter.size()>0){
					for (Object o : filter) {
						JSONObject oo = (JSONObject) o;
						for(int i=0;i<fields.size();i++){  
							if(fields.get(i).getName().equals(oo.get("field"))){
								Reflections.invokeSetter(eventDetail, fields.get(i).getName(), oo.get("value"));
							}
						}
					}
				}
				
		}
		
        Page<EventDetail> page = eventDetailService.findPage(new Page<EventDetail>(request, response), eventDetail); 
        JSONObject jo = JSONObject.fromObject(JsonMapper.toJsonString(page));
        JSONArray ja = jo.getJSONArray("list");
        for(int i = 0;ja!=null&&i<ja.size();i++){
        	JSONObject e = (JSONObject) ja.get(i);
        }
        return jo.toString();
	}

	@RequiresPermissions("guard:eventDetail:view")
	@RequestMapping(value = "form")
	public String form(EventDetail eventDetail, Model model) {
		model.addAttribute("eventDetail", eventDetail);
		return "modules/guard/eventDetailForm";
	}

	@RequiresPermissions("guard:eventDetail:edit")
	@RequestMapping(value = "save")
	public String save(EventDetail eventDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, eventDetail)){
			return form(eventDetail, model);
		}
		eventDetailService.save(eventDetail);
		addMessage(redirectAttributes, "保存事件详情成功");
		return "redirect:"+Global.getAdminPath()+"/guard/eventDetail/?repage";
	}
	
	@RequiresPermissions("guard:eventDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(EventDetail eventDetail, RedirectAttributes redirectAttributes) {
		eventDetailService.delete(eventDetail);
		addMessage(redirectAttributes, "删除事件详情成功");
		return "redirect:"+Global.getAdminPath()+"/guard/eventDetail/?repage";
	}

	private String getJarPath() {
		ApplicationHome h = new ApplicationHome(getClass());
		File jarF = h.getSource();
		return jarF.getParentFile().toString();
	}
}