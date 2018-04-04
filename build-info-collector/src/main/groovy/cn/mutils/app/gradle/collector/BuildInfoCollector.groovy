package cn.mutils.app.gradle.collector

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by wenhua.ywh on 2018/3/28.
 */
class BuildInfoCollector implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("BuildInoCollector:${project.name}");
        project.afterEvaluate {
            try {
                def map = new LinkedHashMap();
                project.configurations.each {
                    def list = [];
                    it.resolve().each {
                        if (it.isFile()) {
                            list.add(it.canonicalPath);
                        }
                    }
                    map.put(it.name, list);
                }
                File jsonFile = project.rootProject.file("build/bulid.info.collector.json");
                String jsonText = jsonFile.exists() ? jsonFile.getText() : null;
                def allMap = jsonText != null ? new JsonSlurper().
                        parseText(jsonText) : new LinkedHashMap();
                allMap.put(project.name, map);
                String newJsonText = new JsonBuilder(allMap).toPrettyString();
                if (newJsonText == jsonText) {
                    println("BuildInfoCollector:build.info.collector.json UP-TO-DATE");
                } else {
                    println("BuildInfoCollector:build.info.collector.json");
                    jsonFile.withPrintWriter {
                        it.write(newJsonText);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
