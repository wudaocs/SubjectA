package com.td.ca.annotation_compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.td.ca.annotation_compiler.annotations.TAction;
import com.td.ca.annotation_compiler.annotations.TSubject;
import com.td.ca.annotation_compiler.entity.ParamEntity;
import com.td.ca.annotation_compiler.utils.StringUtil;
import com.td.ca.annotation_compiler.utils.TSubjectConfigs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Description :
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
// 支持的注解类型, 此处要填写全类名
@SupportedAnnotationTypes({"TSubject", "TAction"})
// JDK版本, 我用的是java7
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class CustomProcessor extends AbstractProcessor {

    /**
     * 文件相关的辅助类
     */
    private Filer mFiler;
    /**
     * 元素相关的辅助类
     */
    private Elements mElementUtils;
    /**
     * 日志相关的辅助类
     */
    private Messager mMessager;

    private HashMap<String, HashMap<String, Object>> typeSpecCache = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        File file = new File(TSubjectConfigs.CACHE_DIR_NAME);
        if (!file.exists()) {
            boolean exist = file.mkdirs();
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        processSubject(roundEnvironment);
        // 根据汇总循环生成Java文件
        if (typeSpecCache != null && !typeSpecCache.isEmpty()) {
            JavaFile javaFile;
            try {
                for (Map.Entry<String, HashMap<String, Object>> entry : typeSpecCache.entrySet()) {
                    String subjectName = entry.getKey();
                    // 子项循环查找
                    HashMap<String, Object> map = typeSpecCache.get(subjectName);
                    for (Map.Entry<String, Object> m : map.entrySet()) {
                        JavaSubjectEntity javaSubjectEntity = (JavaSubjectEntity) m.getValue();
                        javaFile = JavaFile.builder(TSubjectConfigs.PACKAGE_NAME + "." + subjectName, javaSubjectEntity.getTypeSpec()).build();
                        javaFile.writeTo(mFiler);
                    }
                }
            } catch (IOException e) {
                e.getStackTrace();
            }
            typeSpecCache.clear();
        }
        return true;
    }

    // 订阅对象
    class JavaSubjectEntity {

        String packageName;

        String className;

        TypeSpec.Builder builder;

        private MethodSpec methodSpec;

        HashMap<String, MethodSpec.Builder> methods = new HashMap<>();

        JavaSubjectEntity(String subjectName, String packageName) {
            this.packageName = packageName;
            this.className = subjectName;
            this.builder = TypeSpec.classBuilder(subjectName).addModifiers(Modifier.PUBLIC)
                    .superclass(ClassName.bestGuess("InvokeBase"));
        }

        // 生成反射方法
        MethodSpec generatedInvoke() {
            // add invoke
            MethodSpec.Builder invokeMethod = MethodSpec.methodBuilder("invoke")
                    .addParameter(TypeName.get(String.class), "action")
                    .addParameter(TypeName.get(HashMap.class), "params")
                    .returns(Object.class)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addStatement("return super.invoke($N(action), action, params)", generatedGetMethod());
            return invokeMethod.build();
        }

        // 生成反射方法中包含的逻辑比较方法
        MethodSpec generatedGetMethod() {
            MethodSpec.Builder invokeMethod = MethodSpec.methodBuilder("getMethods");
            if (methodSpec == null) {
                StringBuilder content = new StringBuilder();
                if (methods != null && !methods.isEmpty()) {
                    int index = 1;
                    for (Map.Entry<String, MethodSpec.Builder> entry : methods.entrySet()) {
                        if (index == 1) {
                            // 只有一个元素
                            content.append("if($T.equals(\"").append(entry.getKey()).append("\",action)) {\n")
                                    .append("\tmap = ").append(entry.getKey()).append("();\n }");
                        } else {
                            // 中间条件
                            content.append(" else if(StringUtil.equals(\"").append(entry.getKey())
                                    .append("\",action)) {\n").append("\tmap = ").append(entry.getKey()).append("();\n }");
                        }
                        index++;
                    }
                    content.append("\n");
                }
                invokeMethod.addParameter(TypeName.get(String.class), "action")
                        .returns(HashMap.class)
                        .addStatement("$T map = new $T()", HashMap.class, HashMap.class)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

                if (content.length() > 0) {
                    // 包含方法添加返回
                    invokeMethod.addCode(content.toString(), StringUtil.class);
                } else {
                    // 无方法添加无返回
                    invokeMethod.addStatement("return null");
                }
                invokeMethod.addStatement("return map");
            }
            return methodSpec != null ? methodSpec : (methodSpec = invokeMethod.build());
        }

        TypeSpec getTypeSpec() {
            // generated params map to invoke
            if (methods != null && !methods.isEmpty()) {
                for (Map.Entry<String, MethodSpec.Builder> entry : methods.entrySet()) {
                    entry.getValue().addStatement("return map");
                    builder.addMethod(entry.getValue().build());
                }
            }
            builder.addMethod(generatedGetMethod());
            builder.addMethod(generatedInvoke());
            return builder.build();
        }

        void addMethod(String methodName, MethodSpec.Builder builder) {
            methods.put(methodName, builder);
        }

        // 返回是否存在方法
        boolean isNotExistMethod(String action) {
            return methods.isEmpty() || methods.get(action) == null;
        }

        public String getClassName() {
            return className;
        }
    }

    private void processSubject(RoundEnvironment roundEnvironment) {
        // 找到使用注解 TSubject 相关类
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(TSubject.class);
        // 记录所有不同的 action+type ,通过 action+type 创建方法
        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {
                TSubject tSubject = element.getAnnotation(TSubject.class);
                String bs = tSubject.target();
                JavaSubjectEntity javaSubjectEntity;
                TypeElement enclosingElement = (TypeElement) element;
                String classpath = enclosingElement.getQualifiedName().toString();
                // 查询已经存在公共订阅类数量
                String subjectName = enclosingElement.getSimpleName().toString();
                // 取出缓存中的数据
                HashMap<String, Object> map = typeSpecCache.get(bs);
                if (map == null) {
                    map = new HashMap<>();
                }
                if (map.get(subjectName) != null) {
                    // 已经存在相同类型的订阅关系
                    javaSubjectEntity = ((JavaSubjectEntity) map.get(subjectName));
                } else {
                    String packageName = mElementUtils.getPackageOf(element).getQualifiedName().toString();
                    String className = bs + "_" + getFileSize(bs, classpath + "." + subjectName);
//                    String className = subjectName + "_" + getFileSize(target, classpath + "." + subjectName);
                    javaSubjectEntity = new JavaSubjectEntity(className, packageName);
                    generatedFileCache(bs, classpath + "." + subjectName);
                }
                createMethodSpec(element, classpath, javaSubjectEntity);
                // 刷新缓存中的数据
                map.put(subjectName, javaSubjectEntity);
                typeSpecCache.put(bs, map);
            }
        }
    }

    /**
     * 生成本地缓存文件用于区分同一订阅存在多少类相关
     */
    private int getFileSize(String bs, String filename) {
        File file = new File(TSubjectConfigs.CACHE_DIR_NAME + bs + "/");
        if (!file.exists()) {
            boolean exist = file.mkdirs();
        } else {
            File parent = new File(TSubjectConfigs.CACHE_DIR_NAME + bs + "/" + filename);
            if (parent.exists()) {
                File[] child = parent.listFiles();
                if (child != null && child.length > 0) {
                    // 曾经创建过，使用之前的id
                    return Integer.valueOf(child[0].getName());
                }
            } else {
                File[] files = file.listFiles();
                return files != null ? files.length : 0;
            }
        }
        return 0;
    }

    private void generatedFileCache(String bs, String filename) {
        File file = new File(TSubjectConfigs.CACHE_DIR_NAME + bs + "/" + filename);
        if (!file.exists()) {
            int size = 0;
            File parent = new File(TSubjectConfigs.CACHE_DIR_NAME + bs);
            File[] files = parent.listFiles();
            if (files != null) {
                size = files.length;
            }
            boolean exist = file.mkdirs();
            File cache = new File(file.getAbsoluteFile() + "/" + size);
            try {
                cache.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createMethodSpec(Element element, String classpath, JavaSubjectEntity javaSubjectEntity) {
        List<? extends Element> elements = element.getEnclosedElements();
        if (elements != null && !elements.isEmpty()) {
            for (Element e : elements) {
                if (e.getKind() == ElementKind.METHOD) {
                    // 循环获取方法中添加的标识
                    TAction tAction = e.getAnnotation(TAction.class);
                    if (tAction != null) {
                        ExecutableElement executableElement = (ExecutableElement) e;
                        List<? extends VariableElement> parameters = executableElement.getParameters();
                        int size = parameters.size();
                        StringBuilder types = new StringBuilder();
                        StringBuilder names = new StringBuilder();
                        for (int i = 0; i < size; i++) {
                            types.append(parameters.get(i).asType().toString());
                            names.append(parameters.get(i).toString());
                            if (i < size - 1) {
                                types.append(",");
                                names.append(",");
                            }
                        }
                        // key 是 action + type ，value 是 classpath，method，参数类型
                        String key = StringUtil.generatedKey(tAction.value(), String.valueOf(tAction.type()));
                        MethodSpec.Builder methodSpec;
                        if (javaSubjectEntity.methods != null && javaSubjectEntity.methods.get(key) != null) {
                            methodSpec = javaSubjectEntity.methods.get(key);
                        } else {
                            methodSpec = MethodSpec.methodBuilder(key).addModifiers(Modifier.PUBLIC).returns(HashMap.class);
                        }
                        if (javaSubjectEntity.isNotExistMethod(key)) {
                            // 不存在方法则添加第一条语句
                            methodSpec.addStatement("$T map = new $T()", HashMap.class, HashMap.class);
                        }
                        if (types.length() > 0) {
                            methodSpec.addStatement("map.put($S,new $T($S,$S,$S,$S))", StringUtil.generatedKey(classpath, tAction.value()),
                                    ParamEntity.class, classpath, executableElement.getSimpleName(), types, names);
                        } else {
                            methodSpec.addStatement("map.put($S,new $T($S,$S,$S,$S))", StringUtil.generatedKey(classpath, tAction.value()),
                                    ParamEntity.class, classpath, executableElement.getSimpleName(), null, null);
                        }
                        javaSubjectEntity.addMethod(key, methodSpec);
                    }
                }
            }
        }
    }

    /**
     * 这个方法必须重写，否则无法生成Java文件
     * 这里必须指定，这个注解处理器是注册给哪个注解的。
     * 注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称。
     * 换句话说，在这里定义你的注解处理器注册到哪些注解上。
     *
     * @return 支持类型
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(TSubject.class.getCanonicalName());
        types.add(TAction.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    // 输出日志
    public void println(String message) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, message);
    }
}
