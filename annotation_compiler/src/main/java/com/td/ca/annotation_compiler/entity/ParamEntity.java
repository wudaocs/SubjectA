package com.td.ca.annotation_compiler.entity;

/**
 * Description : 自动生成数据类型
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
public class ParamEntity {

    private String classpath;

    private String methodName;

    private String paramsType;

    private String[] params;

    public ParamEntity(String classpath, String methodName, String paramsType, String params) {
        this.classpath = classpath;
        this.methodName = methodName;
        this.paramsType = paramsType;
        if (params != null) {
            String[] paramsName = params.split(",");
            int paramSize = paramsName.length;
            this.params = new String[paramSize];
            System.arraycopy(paramsName, 0, this.params, 0, paramSize);
        }

    }

    public String getClasspath() {
        return classpath;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParamsType() {
        Class[] types = null;
        if (paramsType != null) {
            String[] paramsTypes = paramsType.split(",");
            int paramTypeSize = paramsTypes.length;
            types = new Class[paramTypeSize];
            try {
                for (int i = 0; i < paramTypeSize; i++) {
                    String index = paramsTypes[i];
                    if ("long".equals(index)){
                        types[i] = long.class;
                    } else if ("int".equals(index)){
                        types[i] = int.class;
                    } else if ("double".equals(index)){
                        types[i] = double.class;
                    } else if("float".equals(index)){
                        types[i] = float.class;
                    } else if("char".equals(index)){
                        types[i] = char.class;
                    } else if("short".equals(index)){
                        types[i] = short.class;
                    } else if("int[]".equals(index)){
                        types[i] = int[].class;
                    } else if("float[]".equals(index)){
                        types[i] = float[].class;
                    } else if("double[]".equals(index)){
                        types[i] = double[].class;
                    } else if("long[]".equals(index)){
                        types[i] = long[].class;
                    } else if("byte".equals(index)){
                        types[i] = byte.class;
                    } else if("byte[]".equals(index)){
                        types[i] = byte[].class;
                    } else if("boolean".equals(index)){
                        types[i] = boolean.class;
                    }  else {
                        types[i] = Class.forName(index);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.getStackTrace();
            }
        }
        return types;
    }

    public void setParamsType(String paramsType) {
        this.paramsType = paramsType;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }
}
