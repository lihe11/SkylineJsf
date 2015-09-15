package skyline.view;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.common.utils.MessageUtil;
import skyline.repository.model.Ptoper;
import skyline.repository.model.Ptoplog;
import pub.auth.MD5Helper;
import pub.platform.form.config.SystemAttributeNames;
import pub.platform.security.OperatorManager;
import skyline.service.PlatformService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * User: zhanrui
 * Date: 2015-6-11
 */
@ManagedBean
@SessionScoped
public class LoginManager implements Serializable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Ptoper ptoper;
    private boolean isLogin;
    private String deptId;
    private String operId;
    private String operPasswd;
    private boolean agree=true;
    private String mailbox;
    private String loginTime;

    @ManagedProperty(value = "#{platformService}")
    private PlatformService platformService;

    public String onLogin() {
        try {
            if (StringUtils.isBlank(operId)) {
                MessageUtil.addWarn("请输入用户名.");
                return null;
            }
            if (StringUtils.isBlank(operPasswd)) {
                MessageUtil.addWarn("请输入用户密码.");
                return null;
            }

            ptoper = platformService.selectOper(operId);

            if (ptoper == null) {
                MessageUtil.addWarn("该用户不存在或密码错误.");
                return null;
            } else {
                if (!MD5Helper.getMD5String(operPasswd).equals(ptoper.getOperpasswd())) {
                    //TODO 检查错误累计次数及时间
                    //ptoper.setFillint6(ptoper.getFillint6() + 1);
                    MessageUtil.addWarn("该用户不存在或密码错误...");
                    return null;
                }

                //验证通过
                this.loginTime = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
                //ptoper.setOnlineSituation(loginTime);
                ptoper.setFillint6(0);
                ptoper.setFilldate1(new DateTime().toDate());
                platformService.updateOperLoginInfo(ptoper);

//                ptoper.setFillint6(0);
//                ptoper.setFilldate1(new DateTime().toDate());
//                platformService.updateOperLoginInfo(ptoper);

                //临时处理，兼容旧平台
//                OperatorManager om = (OperatorManager) getSession().getAttribute(SystemAttributeNames.USER_INFO_NAME);
//                if (om == null) {
//                    om = new OperatorManager();
//                    getSession().setAttribute(SystemAttributeNames.USER_INFO_NAME, om);
//                    om.login(operId, operPasswd);
//                }

                OperatorManager om = new OperatorManager();
                getSession().setAttribute(SystemAttributeNames.USER_INFO_NAME, om);
                om.login(operId, operPasswd);

                Ptoplog oplog = new Ptoplog();
                oplog.setActionId("LoginAction_onLogin");
                oplog.setActionName("登录成功");
                platformService.insertNewOperationLog(oplog);

                return "dashboard";
            }

        } catch (Exception e) {
            logger.error("查询数据时出现错误。", e);
            MessageUtil.addWarn("查询数据时出现错误。" + e.getMessage());
        }
        return null;
    }

    public String onLogout() {
        HttpSession session = getSession();
        session.invalidate();
        return "/login";
    }

    private HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }
    //=========================

    public Ptoper getPtoper() {
        return ptoper;
    }

    public void setPtoper(Ptoper ptoper) {
        this.ptoper = ptoper;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public String getDeptId() {
        return deptId;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getOperPasswd() {
        return operPasswd;
    }

    public void setOperPasswd(String operPasswd) {
        this.operPasswd = operPasswd;
    }

    public PlatformService getPlatformService() {
        return platformService;
    }

    public void setPlatformService(PlatformService platformService) {
        this.platformService = platformService;
    }

    public boolean isAgree() {
        return agree;
    }

    public String getMailbox() {
        return mailbox;
    }

    public String getLoginTime() {
        return loginTime;
    }
}
