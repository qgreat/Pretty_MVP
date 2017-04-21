package com.codingqi.pretty.mvp.model.entity;

import java.io.Serializable;


public class GirlJson<T> implements Serializable{
    private T results;
    private boolean error;

    public T getData() {
        return results;
    }


    public boolean getError() {
        return error;
    }

    /**
     * 请求是否成功
     * @return
     */
    public boolean isSuccess() {
        if (error==false) {
            return true;
        } else {
            return false;
        }
    }
}
