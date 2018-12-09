package egovframework.standalone;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.SampleDefaultVO;

/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**  
 * @Class Name : SampleMain.java
 * @Description : SampleMain Class
 * @Modification Information  
 * @
 * @   수정일		 수정자             수정내용
 * @ -----------   ----------   -------------------------------
 * @ 2014.04.17     유지보수			최초생성
 * 
 * @author 표준프레임워크센터 유지보수팀
 * @since 2014. 04.17
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by MOPAS All right reserved.
 */
public class SampleMain {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "classpath:/egovframework/spring/context-*.xml" });

		EgovSampleService service = (EgovSampleService) context.getBean("sampleService");

		SampleDefaultVO searchVO = new SampleDefaultVO();

		try {
			List list = service.selectSampleList(searchVO);

			System.out.println("selectSampleList : " + list.size());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
