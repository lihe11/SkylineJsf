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
package org.primefaces.spark.service;

import org.primefaces.spark.domain.Car;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ManagedBean(name = "carService")
@ApplicationScoped
public class CarService {
    
    private final static String[] colors;

	private final static String[] brands;

    static {
		colors = new String[10];
		colors[0] = "Black";
		colors[1] = "White";
		colors[2] = "Green";
		colors[3] = "Red";
		colors[4] = "Blue";
		colors[5] = "Orange";
		colors[6] = "Silver";
		colors[7] = "Yellow";
		colors[8] = "Brown";
		colors[9] = "Maroon";

		brands = new String[10];
		brands[0] = "���и�������";
		brands[1] = "����������������";
		brands[2] = "���ϵ�һ֧��";
		brands[3] = "���ϵڶ�֧��";
		brands[4] = "���ϵ���֧��";
		brands[5] = "�б�֧��";
		brands[6] = "ƽ��֧��";
		brands[7] = "����֧��";
		brands[8] = "�ķ�֧��";
		brands[9] = "����֧��";
	}
    
    public List<Car> 	createCars(int size) {
        List<Car> list = new ArrayList<Car>();
		for(int i = 0 ; i < size ; i++) {
			list.add(new Car(getRandomId(), getRandomBrand(), getRandomYear(), getRandomColor(), getRandomPrice(), getRandomSoldState()));
        }
        
        return list;
    }
    
    private String getRandomId() {
		return  "371980120" + (int) (Math.random() * 10);
	}
    
    private int getRandomYear() {
		return (int) (Math.random() * 50 + 1960);
	}
	
	private String getRandomColor() {
		return "" + (int) (Math.random() * 100);
	}
	
	private String getRandomBrand() {
		return brands[(int) (Math.random() * 10)];
	}
    
    private int getRandomPrice() {
		return (int) (Math.random() * 10000);
	}
    
    private boolean getRandomSoldState() {
		return (Math.random() > 0.5) ? true: false;
	}
    
    public List<String> getColors() {
        return Arrays.asList(colors);
    }
    
    public List<String> getBrands() {
        return Arrays.asList(brands);
    }
}
