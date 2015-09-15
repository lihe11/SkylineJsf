/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.spark.view.input;

import org.primefaces.spark.domain.Theme;
import org.primefaces.spark.service.ThemeService;
import skyline.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
public class SelectOneMenuView {

    private String console;

    private String car;
    private List<SelectItem> cars;

    private String city;
    private Map<String,String> cities = new HashMap<String, String>();

    private Theme theme;
    private List<Theme> themes;

    @ManagedProperty("#{themeService}")
    private ThemeService service;

    @PostConstruct
    public void init() {
        //cars
        SelectItemGroup g1 = new SelectItemGroup("住房类");
        g1.setSelectItems(new SelectItem[] {new SelectItem("个人住房", "个人住房"), new SelectItem("个人再交易", "个人再交易"), new SelectItem("个人商业用房", "个人商业用房")});

        SelectItemGroup g2 = new SelectItemGroup("消费类");
        g2.setSelectItems(new SelectItem[] {new SelectItem("额度消费", "额度消费"), new SelectItem("个人助业", "个人助业"), new SelectItem("个人汽车", "个人汽车")});

        cars = new ArrayList<SelectItem>();
        cars.add(g1);
        cars.add(g2);

        //cities
        cities = new HashMap<String, String>();
        cities.put("个贷中心", "个贷");
        cities.put("开发区中心","开发区");
        cities.put("城阳支行","城阳");
        cities.put("平度支行","平度支行");
        cities.put("莱西支行","莱西支行");
        cities.put("胶州支行","胶州支行");

        //themes
        themes = service.getThemes();
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public List<SelectItem> getCars() {
        return cars;
    }

    public Map<String, String> getCities() {
        return cities;
    }

    public List<Theme> getThemes() {
        return themes;
    }
    public void ceshi(){
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("parentmenuid", this.getTheme());
            paramMap.put("menulabel", this.getTheme().getDisplayName());

           String sql1 = "INSERT INTO ptmenu t (t.menuid,t.menulevel,t.isleaf,t.parentmenuid,t.menulabel," +
                    "t.menuaction,t.levelidx,t.menudesc,t.targetmachine)" +
                    "VALUES(ptmenusequences.nextval,2,1,:parentmenuid,:menulabel,:menuaction,:levelidx,:menudesc,:targetmachine)";

        } catch (Exception e) {
            MessageUtil.addWarn("编辑数据时出现错误。" + e.getMessage());
        }
    }

    public void setService(ThemeService service) {
        this.service = service;
    }
}
