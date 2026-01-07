package org.example.common;

import lombok.Data;

@Data
public final class BusinessException extends RuntimeException {

    /**
     * 常用异常
     * BE1001:查询数据为空
     */
    public static final BusinessException BE1001, BE1002, BE1003, BE1004, BE1005, BE1006, BE1007, BE1008, BE1009, BE1010,BE1011;
    private static final long serialVersionUID = 1L;
    /**
     * 业务异常
     **/
    public static final Integer CODE1000 = 1000;
    /**
     * 业务异常--查询数据为空
     **/
    private static final Integer CODE1001 = 1001;
    /**
     * 业务异常--获取用户信息失败
     **/
    private static final Integer CODE1002 = 1002;
    /**
     * 业务异常--没有操作权限
     */
    private static final Integer CODE1003 = 1003;
    /**
     * 业务异常--打印模板填充数据失败
     */
    private static final Integer CODE1004 = 1004;
    /**
     * 业务异常--打印模板生成pdf文件失败
     */
    private static final Integer CODE1005 = 1005;
    /**
     * 业务异常--查询子表单统计列数据异常
     */
    private static final Integer CODE1006 = 1006;
    /**
     * 业务异常--表单不存在或已删除，前端需要根据code区分此类异常
     */
    private static final Integer CODE1007 = 1007;
    /**
     * 业务异常--表单扩容异常，已达到扩容上限
     */
    private static final Integer CODE1008 = 1008;
    /**
     * 文件分片已结束,请重新开始！
     */
    private static final Integer CODE1009 = 1009;
    /**
     * 业务异常--获取应用令牌信息失败
     */
    private static final Integer CODE1010 = 1010;
    /**
     * 业务异常--加密文件请下载后再预览
     */
    private static final Integer CODE1011 = 1011;

    static {
        BE1001 = new BusinessException(CODE1001, "查询数据为空！");
        BE1002 = new BusinessException(CODE1002, "获取用户信息失败！");
        BE1003 = new BusinessException(CODE1003, "用户没有此表单操作权限！");
        BE1004 = new BusinessException(CODE1004, "获取模板填充数据失败！");
        BE1005 = new BusinessException(CODE1005, "生成pdf预览文件失败！");
        BE1006 = new BusinessException(CODE1006, "该表单没有设置子表单统计列！");
        BE1007 = new BusinessException(CODE1007, "查询异常，表单未创建或已删除！");
        BE1008 = new BusinessException(CODE1008, "表单字段已达上限！");
        BE1009 = new BusinessException(CODE1009, "文件分片已结束,请重新开始！");
        BE1010 = new BusinessException(CODE1010, "获取应用令牌信息失败！");
        BE1011 = new BusinessException(CODE1011, "加密或未知格式文件请下载后再预览！");
    }

    private Integer code;
    private String msg;
    private Object data;


    public BusinessException(Integer code, String msg,Object data) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String msg,Object data) {
        this(CODE1000, msg,data);
    }
    public BusinessException(String msg) {
        this(CODE1000, msg);
    }

    public void destroy() {
        this.data = null;
        this.code = null;
        this.msg = null;
    }
}