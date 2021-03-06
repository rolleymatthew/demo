package com.wy.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author congli216488
 */
public class ResultVO<T> implements Serializable {
	public static final Integer RESULT_CODE_SUCCESS = 200;
	public static final Integer RESULT_CODE_FAILURE = -1;

	public static final ResultVO ERROR = ResultVO.build(RESULT_CODE_FAILURE, "error,出现错误 请刷新页面");
	public static final ResultVO AUDIT_ERROR = ResultVO.build(RESULT_CODE_FAILURE, "error,出现错误 请刷新重新整理发言");
	public static final ResultVO ERROR_NO_AUTHORITY = ResultVO.build(RESULT_CODE_FAILURE, "没有权限，如果需要，可以联系开发，开通权限");
	public static final ResultVO ERROR_NO_DATA = ResultVO.build(RESULT_CODE_FAILURE, "数据不存在");
	public static final ResultVO OK = ResultVO.build(RESULT_CODE_SUCCESS, "执行成功");
	public static final ResultVO ADD_OK = ResultVO.build(RESULT_CODE_SUCCESS, "添加成功");
	public static final ResultVO EDIT_OK = ResultVO.build(RESULT_CODE_SUCCESS, "编辑成功");
	public static final ResultVO UPDATE_OK = ResultVO.build(RESULT_CODE_SUCCESS, "更新成功");
	public static final ResultVO SAVE_OK = ResultVO.build(RESULT_CODE_SUCCESS, "保存成功");

	public static final ResultVO NO_PAGE = new ResultVO().setCode(RESULT_CODE_SUCCESS).setCount(0L).setData(null);
	public static final ResultVO DELETE_OK = ResultVO.build(RESULT_CODE_SUCCESS, "删除成功");
	public static final ResultVO PUB_OK = ResultVO.build(RESULT_CODE_SUCCESS, "发布成功");
	/** 状态 */
	private Integer code;
	/** 消息 */
	private String msg;
	/** 数据 分页统计结果 */
	private Object data;
	/** 分页统计总数 */
	private Long count;

	private List<Integer> ids;

	public ResultVO() {
	}

	public ResultVO(Integer code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	/**
	 * 构造方法
	 *
	 * @param data
	 */
	public ResultVO(Object data) {
		this.code = RESULT_CODE_SUCCESS;
		this.msg = "成功";
		this.data = data;
	}

	public boolean success() {
		return RESULT_CODE_SUCCESS.equals(code);
	}

	public static Object toResultVOData(ResultVO resultVO) {
		if (resultVO == null || resultVO.getData() == null) {
			return null;
		}
		return resultVO.getData();
	}

	public static ResultVO build(Integer status, String msg, Object data) {
		return new ResultVO(status, msg, data);
	}

	public static ResultVO ok(Object data) {
		return new ResultVO(data);
	}

	public static ResultVO ok() {
		return new ResultVO(null);
	}

	public static ResultVO build(Integer status, String msg) {
		return new ResultVO(status, msg, null);
	}

	public ResultVO success(Object data) {
		this.code = RESULT_CODE_SUCCESS;
		this.msg = "成功";
		this.data = data;
		return this;
	}

	public Integer getCode() {
		return code;
	}

	public ResultVO setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public ResultVO setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Object getData() {
		return data;
	}

	public ResultVO setData(Object data) {
		this.data = data;
		return this;
	}

	public Long getCount() {
		return count;
	}

	public ResultVO setCount(Long count) {
		this.count = count;
		return this;
	}

	@Override
	public String toString() {
		return "ResultVO{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + ", count=" + count + '}';
	}

	public List<Integer> getIds() {
		return ids;
	}

	public ResultVO<T> setIds(List<Integer> ids) {
		this.ids = ids;
		return this;
	}
}
