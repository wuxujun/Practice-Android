package com.xujun.util;

import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/***
 * 接口 urls
* @ClassName: URLs
* @Description: TODO(这里用一句话描述这个类的作用)
* @author xujunwu
* @date 2013-5-11 下午2:39:25
*
 */
public class URLs implements Serializable {
	
	public final static String HOST = "sx.asiainstitute.cn";
	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";
	
	private final static String URL_SPLITTER = "/";
	private final static String URL_UNDERLINE = "_";
	
	private final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER;

    public final static String IMAGE_URL="http://sx.asiainstitute.cn/images/";
	
	public final static String LOGIN_VALIDATE_HTTP = HTTP + HOST + URL_SPLITTER + "index.php?/ums/login";
    public final static String REGISTER_USER=HTTP + HOST + URL_SPLITTER + "index.php?/ums/register";
    public final static String LOGIN_VALIDATE_HTTPS = HTTPS + HOST + URL_SPLITTER + "action/api/login_validate";

    public final static String INIT_CONFIG_URL = HTTP + HOST + URL_SPLITTER + "index.php?/ums/getConfig";
    public final static String INFO_GET_URL= HTTP + HOST + URL_SPLITTER + "index.php?/ums/getInfo";

    public final static String CATEGORY_LIST_URL=URL_API_HOST+"index.php?/ums/category";
    public final static String CITY_LIST_URL = URL_API_HOST+"index.php?/ums/city";
	public final static String PARAM_LIST_URL = URL_API_HOST+"index.php?/ums/params";
	public final static String OFFICES_LIST_URL = URL_API_HOST+"index.php?/ums/offices";
	public final static String OFFICE_ACTION_URL = URL_API_HOST+"index.php?/ums/officeAction";
	public final static String MY_OFFICE_REQ_URL = URL_API_HOST+"index.php?/ums/myOfficeReq";
	public final static String MY_COLLECT_URL = URL_API_HOST+"index.php?/ums/myCollect";
	public final static String COMPANY_LIST_URL=URL_API_HOST+"index.php?/ums/company";
	public final static String SEARCH_LIST_URL=URL_API_HOST+"index.php?/ums/search";

	public final static String RESUME_INFO_ADD = URL_API_HOST+"index.php?/ums/addResume";
	public final static String RESUME_WORK_ADD = URL_API_HOST+"index.php?/ums/addMemberWork";
	public final static String RESUME_LIFE_ADD = URL_API_HOST+"index.php?/ums/addMemberLife";
	public final static String RESUME_HONOR_ADD = URL_API_HOST+"index.php?/ums/addMemberHonor";

	public final static String RESUME_INFO_QUERY = URL_API_HOST+"index.php?/ums/resumeInfo";
	public final static String RESUME_WORK_LIST = URL_API_HOST+"index.php?/ums/resumeWork";
	public final static String RESUME_LIFE_LIST = URL_API_HOST+"index.php?/ums/resumeLife";
	public final static String RESUME_HONOR_LIST=URL_API_HOST+"index.php?/ums/resumeHonor";

	public final static String UPLOAD_IMAGE=URL_API_HOST+"index.php?/ums/uploadImage";
	public final static String MEMBER_PHOTO_ADD=URL_API_HOST+"index.php?/ums/addPhoto";

	public final static String MESSAGES_LIST = URL_API_HOST+"index.php/ums/messages";

	public final static String RATING = URL_API_HOST+"index.php/ums/rating";

    public final static String ADD_NOTIFY_SET=URL_API_HOST+"index.php/ums/addNotify";


	public final static String SELLERS_LIST = URL_API_HOST+"index.php/ums/store";

    public final static String PDFS_LIST = URL_API_HOST+"index.php/ums/pdfs";

    public final static String EDAIJIA_URL = "http://open.d.api.edaijia.cn";


    public final static String VERSION = URL_API_HOST+"index.php/ums/version";

	public final static String FEEDBACK = URL_API_HOST+"index.php/ums/feedback";

	public final static String APPINFO = URL_API_HOST+"index.php/ums/getAppInfo";
	public final static String TOP_APPINFO=URL_API_HOST+"index.php/ums/getTopApps";

	public final static String DICTS_LIST = URL_API_HOST+"index.php/ums/getDicts";
	public final static String COUPON_LIST = URL_API_HOST+"index.php/ums/coupons";

	public final static String UPDATE_VERSION = URL_API_HOST+"MobileAppVersion.json";

}
