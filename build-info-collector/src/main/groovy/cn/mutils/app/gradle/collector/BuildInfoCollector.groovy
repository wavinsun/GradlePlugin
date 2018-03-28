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
        println("BuildInoCollector: ${project.name}");
        project.afterEvaluate {
            println("BuildInoCollector: project.afterEvaluate");
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
                def allMap = jsonFile.exists() ? new JsonSlurper().
                        parseText(jsonFile.getText()) : new LinkedHashMap();
                allMap.put(project.name, map);
                jsonFile.setText(new JsonBuilder(allMap).toPrettyString());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
