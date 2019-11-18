package cn.wjdc.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wjdc.entity.Option;
import cn.wjdc.entity.Question;
import cn.wjdc.entity.QuestionNaire;
import cn.wjdc.entity.User;
import cn.wjdc.service.OptionService;
import cn.wjdc.service.QuesNaireService;
import cn.wjdc.service.QuestionService;
import cn.wjdc.service.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	QuesNaireService quesNaireService;
	@Autowired
	QuestionService questionService;
	@Autowired
	OptionService optionService;

	// ��¼����
	@RequestMapping("/getInfo")
	@ResponseBody
	public Map getInfo(HttpServletRequest req, HttpSession session, @RequestBody Map data) {
		String code = (String) data.get("code");
		HashMap info = (HashMap) data.get("userInfo");

		String appId = "wx758812168dbe49aa"; // �Լ���appid��appsecret
		String appSecret = "71898dfd3385290ea1faa780505e7e38";
		String openId = ""; // ���ڱ����û���openid

		String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appSecret
				+ "&js_code=" + code + "&grant_type=authorization_code";

		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// �򿪺�URL֮�������
			URLConnection connection = realUrl.openConnection();
			// ����ͨ�õ���������
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ��ʵ�ʵ�����
			connection.connect();

			// ��ȡ������Ӧͷ�ֶ�
			Map<String, List<String>> map = connection.getHeaderFields();
			// ����������Ӧͷ�ֶ�
			// for(String key : map.keySet()){
			// System.out.println(key+"---->"+map.get(key));
			// }
			// ���� BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		// �����û���Ϣ
		User user = new User();
		try {
			BeanUtils.populate(user, info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] codeSplit = result.split("\"");
		for (int i = 0; i < codeSplit.length; i++) {
			if ("openid".equals(codeSplit[i])) {
				openId = codeSplit[i + 2];
				user.setOpenid(openId);
			}
		}
		//�ж��û��Ƿ������user�������ھͼ���
		int flag = userService.findByOpenid(user.getOpenid());
		if(flag==0){//������
			userService.addUser(user);
		}
		System.out.println(user);
		// ���û��������session
		session.setAttribute("user", user);
		// ����sessionidʹ����session״̬����
		String sessionid = req.getSession().getId();
		Map<String, String> loginData = new HashMap<String, String>();
		loginData.put("sessionid", sessionid);

		System.out.println(result);
		System.out.println(info);
		System.out.println(openId);
		return loginData;
	}

	// ��ȡ���û����е��ʾ�
	@RequestMapping("/readQuestionnaire")
	@ResponseBody
	public Map readQuestionnaire(HttpSession session) {
		User user = (User) session.getAttribute("user");
		// �õ��û���openid
		String openid = user.getOpenid();
		List<QuestionNaire> quesNaires = quesNaireService.findQuesNaireByOpenid(openid);
		List<Map> quesNaireMap = new ArrayList<Map>();
		for (QuestionNaire quesNaire : quesNaires) {
			Map<String, String> temp = new HashMap<String, String>();
			temp.put("qnname", quesNaire.getQnname());
			temp.put("answers", quesNaire.getAnwers().toString());
			temp.put("createtime", quesNaire.getCreatetime());
			temp.put("explains", quesNaire.getExplains());
			temp.put("id", quesNaire.getId().toString());
			quesNaireMap.add(temp);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("quesNaires", quesNaireMap);
		return result;
	}

	// ����ʾ������ʾ�
	@RequestMapping("/addQuestionNaire")
	@ResponseBody
	public Map addQuestionNaire(HttpSession session, @RequestBody Map<String, String> Items) {
		String qnname = Items.get("qnName");
		String explains = Items.get("explains");
		String idtemp = Items.get("quesNaireId");

		String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
		User user = (User) session.getAttribute("user");
		String openid = user.getOpenid();
		QuestionNaire quesNaire = new QuestionNaire();
		quesNaire.setQnname(qnname);
		quesNaire.setExplains(explains);
		quesNaire.setUseropenid(openid);
		quesNaire.setCreatetime(createTime);
		if (idtemp != null) {
			Long id = Long.parseLong(idtemp);
			quesNaire.setId(id);
			this.quesNaireService.updateQuesNaire(quesNaire);
		} else {
			this.quesNaireService.addQuesNaire(quesNaire);
		}
		return null;
	}

	// ɾ���ʾ��ʾ��µ����⣬ѡ��ҲӦɾ��
	@RequestMapping("/delQuestionNaire")
	@ResponseBody
	public String delQuestionNaire(HttpSession session, @RequestBody Map<String, String> data) {
		Long quesNaireId = Long.parseLong(data.get("quesNaireId"));
		this.quesNaireService.delQuesNaireById(quesNaireId);
		List<Question> list = questionService.findQuestionByQid(quesNaireId);// �鿴���ʾ����Ŀ
		if (list != null) {
			questionService.delQuestionByQid(quesNaireId);// ����quesNaireIdɾ��question���ж�Ӧ����Ŀ
			optionService.delOptionsWithNotQues();// ɾ��tid����question���е�ѡ���Ϊ��һ��ɾ����Ŀ
		}
		return "";
	}

	// ������⣬�͸�������
	@RequestMapping("/savaQuestion")
	@ResponseBody
	public String savaQuestion(HttpServletRequest req, HttpSession session, @RequestBody Map data) {
		// -------------�������
		boolean must_do = (boolean) data.get("must_do"); // ������־
		int quesNaireId = Integer.parseInt((String) data.get("quesNaireId")); // ���������ʾ��id
		int type = Integer.parseInt((String) data.get("type")); // �������ͣ�1Ϊ��ѡ��2Ϊ��ѡ��3Ϊ���
		String quesname = (String) data.get("quesname"); // ������
		String tid = (String) data.get("tid");
		List<String> tnameList = (ArrayList<String>) data.get("optionList"); // ѡ���б�
		System.out.println("quesNaireId:" + quesNaireId + "\nmust:" + must_do + "\nquesname:" + quesname + "\ntype:"
				+ type + "\ntid" + tid);

		// ---------------������Ŀ
		Question question = new Question();
		question.setMust_do(must_do);
		question.setQid((long) quesNaireId);
		question.setTname(quesname);
		question.setType(type);
		// questionService.addQuestion(question);

		// ����tid�Ƿ�Ϊ���ж��Ǹ��²��������½�����
		if (tid == null || "".equals(tid)) {
			tid = UUID.randomUUID().toString().replaceAll("-", ""); // ����UUID����Ψһ����Ŀid
			question.setTid(tid);
			questionService.addQuestion(question);

			System.out.println("�½�����");
		} else {
			System.out.println("���²���");
			question.setTid(tid);
			questionService.updateQuestion(question);
			// ɾ��ԭ����ѡ����¼����µ�ѡ�ʵ�ָ���ѡ���Ч��
			// ����tidɾ��ѡ��
			optionService.delOptionByTid(tid);
		}

		// ---------------����ѡ��
		if (type == 1 || type == 2) {
			// ѡ����
			char tnumber = 'A';
			List<Option> optionList = new ArrayList<Option>();
			for (String tname : tnameList) {
				Option option = new Option();
				option.setOptionname(tname);
				option.setTid(tid);
				option.setTnumber(tnumber);
				optionList.add(option);
				tnumber += 1;
			}
			optionService.addOptions(optionList);
		} else {
			// �����
		}
		// ---------------����forѭ����������ѡ������,���type=3Ϊ����⣬�β������
		// ---------------������Ŀ����
		// for(Object option : optionList){
		// //Map map = org.apache.commons.beanutils.BeanMap(option);
		// Map map= new org.apache.commons.beanutils.BeanMap(option);
		// for(Object ob : map.keySet()){
		// Object value = map.get(ob);
		// System.out.println(String.valueOf(value));
		// }
		// }
		//

		return null;
	}

	// ��ȡ�����Լ�ѡ��
	@RequestMapping("readQuestions")
	@ResponseBody
	public Map readQuestions(HttpSession session, @RequestBody Map<String, String> data) {
		//���صĽ��
		Map<String,Object> result = new HashMap<String,Object>();
		//��session�л�ȡuser���ж��û����¼
		User user = (User) session.getAttribute("user");
		if(user==null){
			//�û�δ��¼����Ϊ��¼��־
			result.put("loginflag", "notlogin");
			return result;
		}
		Long quesNaireId = Long.parseLong(data.get("quesNaireId"));
		System.out.println(quesNaireId);
		// ��������
		List<Question> questions = questionService.findQuestionByQid(quesNaireId);
		// ����ѡ��
		for (Question q : questions) {
			if(q.getType()==1||q.getType()==2){   //ѡ���⣬������ѡ���б�
				List<Option> options = optionService.findOptionByTid(q.getTid());
				q.setOptions(options);
			}else{     //����⣬�������лش��ߵĴ�
				List<String> values = optionService.finValueByTid(q.getTid());
				q.setValues(values);
				for (String str : values) {
					System.out.println(q.getTname()+"-------"+str);
				}
			}
			
		}
		for (Question q1 : questions) {
			System.out.println(q1.toString());
		}
		
		result.put("questions", questions);
		return result;
	}

	@RequestMapping("delQuestion")
	@ResponseBody
	public void delQuestion(@RequestBody Map<String, String> data) {
		String tid = (String) data.get("tid");
		questionService.delQuestionByTid(tid);
		optionService.delOptionsWithNotQues();// ɾ��tid����question���е�ѡ���Ϊ��һ��ɾ����Ŀ
	}

	@RequestMapping("quesNaireSubmit")
	@ResponseBody
	public void quesNaireSubmit(@RequestBody Map data) {
		List<HashMap> answers = (List) data.get("quesData"); // ǰ̨������һ��JSON����,��List����
		quesNaireService.answersAdd(answers);  // ���ҷ���ΪHashMap
		for (HashMap map : answers) {
			System.out.println("--------");
			System.out.println(map.get("tid"));
			System.out.println(map.get("type"));
			System.out.println(map.get("value"));
		}
//		boolean flag = true; // �ʾ�ش������һ��־
//		for (HashMap map : answers) { // ����ÿ��map���ж�type��ʲô����
//			System.out.println("--------");
//			System.out.println(map.get("tid"));
//			System.out.println(map.get("type"));
//			System.out.println(map.get("value"));
//			
//			Integer type = (Integer) map.get("type");
//			String tid = (String) map.get("tid");
//			if(flag){
//				//����Ӧ���ʾ�Ļش�������һ�����flag=false  ����ÿ���ύֻ��һ��
//				quesNaireService.answersAdd(tid);
//				flag = false;
//			}
//			//����tid��������Ļش�������һ
//			questionService.answersAdd(tid);
//			
//			//ѡ��ش�������һ�����򱣴������
//			if (type.intValue() == 1) {
//				Long value = Long.parseLong((String) map.get("value"));
//				optionService.answerAdd(value);
//			} else if (type.intValue() == 2) {
//				List<String> values = (List<String>) map.get("value");
//				for (String str : values) {
//					optionService.answerAdd(Long.parseLong(str));
//				}
//				
//			} else {
//				String value = (String) map.get("value");
//				System.out.println("�ʴ�" + value);
//			}
//
//		}

		// for (Entry entry : answers.entrySet()) {
		// // �ʾ���ύ����+1
		// if (flag) {
		// // ͨ��tid���oid������oid��Ӧ���ʾ��ύ��+1 ÿ������ֻ����һ��
		// //quesNaireService.answersAdd((String)entry.getKey());
		// flag = false;
		// }
		// // ����Ļش�����+1
		// //questionService.answersAdd((String)entry.getKey());
		// // ѡ���ѡ������+1
		// System.out.println("key:" + entry.getKey() + "-----value:" +
		// entry.getValue());
		// }

	}
}
