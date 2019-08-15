package com.xyzq.webapp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xyzq.webapp.entity.DataTableParameter;

/**
 * Package: com.xyzq.webapp.utils
 * Description： 分页数据工具类
 * Author: linkan
 * Date: Created in 2019/8/1 13:52
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
public class DataTableUtils {

	private static Map<String,Object> covertJsonStringToHashMap(String jsonParam){
        JSONArray jsonArray = JSONArray.parseArray(jsonParam);
        Map<String,Object> map = new HashMap<>();
        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            map.put(jsonObj.getString("name"), jsonObj.get("value"));
        }
        return map;
    }

	
	public static DataTableParameter getDataTableParameterByJsonParam(String jsonParam){
        Map<String,Object> map = covertJsonStringToHashMap(jsonParam);
        int sEcho = (int) map.get("sEcho"); 
        int iDisplayStart = (int) map.get("iDisplayStart");
        int iDisplayLength = (int) map.get("iDisplayLength");
        int iColumns = (int)map.get("iColumns");
        int iSortingCols = (int)map.get("iSortingCols");

        List<String> mDataProps = new ArrayList<>();
        List<Boolean> bSortables = new ArrayList<>();
        for(int i=0;i<iColumns;i++){
            String dataProp = String.valueOf(map.get("mDataProp_"+i));
            Boolean sortable = (Boolean) map.get("bSortable_"+i);
            mDataProps.add(dataProp);
            bSortables.add(sortable);
        }

        List<Integer> iSortCols = new ArrayList<>();
        List<String> sSortDirs = new ArrayList<>();
        List<String> iSortColsName = new ArrayList<>();
        for(int i=0;i<iSortingCols;i++){
            Integer sortCol = (Integer) map.get("iSortCol_"+i);
            String sortColName = mDataProps.get(sortCol);
            String sortDir = (String) map.get("sSortDir_"+i);
            iSortCols.add(sortCol);
            sSortDirs.add(sortDir);
            iSortColsName.add(sortColName);
        }

        return new DataTableParameter(sEcho, 
        		iDisplayStart, iDisplayLength, 
        		iColumns, mDataProps, bSortables, 
        		iSortingCols, iSortCols, sSortDirs,iSortColsName);
    }
}
