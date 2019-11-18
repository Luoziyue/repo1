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

	// 登录请求
	@RequestMapping("/getInfo")
	@ResponseBody
	public Map getInfo(HttpServletRequest req, HttpSession session, @RequestBody Map data) {
		String code = (String) data.get("code");
		HashMap info = (HashMap) data.get("userInfo");

		String appId = "wx758812168dbe49aa"; // 自己的appid和appsecret
		String appSecret = "71898dfd3385290ea1faa780505e7e38";
		String openId = ""; // 用于保存用户的openid

		String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appSecret
				+ "&js_code=" + code + "&grant_type=authorization_code";

		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的链接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 打开实际的链接
			connection.connect();

			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有响应头字段
			// for(String key : map.keySet()){
			// System.out.println(key+"---->"+map.get(key));
			// }
			// 定义 BufferedReader输入流来读取URL的响应
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
		// 保存用户信息
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
		//判断用户是否存在于user表，不存在就加入
		int flag = userService.findByOpenid(user.getOpenid());
		if(flag==0){//不存在
			userService.addUser(user);
		}
		System.out.println(user);
		// 将用户对象放入session
		session.setAttribute("user", user);
		// 返回sessionid使程序session状态不变
		String sessionid = req.getSession().getId();
		Map<String, String> loginData = new HashMap<String, String>();
		loginData.put("sessionid", sessionid);

		System.out.println(result);
		System.out.println(info);
		System.out.println(openId);
		return loginData;
	}

	// 读取该用户所有的问卷
	@RequestMapping("/readQuestionnaire")
	@ResponseBody
	public Map readQuestionnaire(HttpSession session) {
		User user = (User) session.getAttribute("user");
		// 得到用户的openid
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

	// 添加问卷，更新问卷
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

	// 删除问卷，问卷下的问题，选项也应删除
	@RequestMapping("/delQuestionNaire")
	@ResponseBody
	public String delQuestionNaire(HttpSession session, @RequestBody Map<String, String> data) {
		Long quesNaireId = Long.parseLong(data.get("quesNaireId"));
		this.quesNaireService.delQuesNaireById(quesNaireId);
		List<Question> list = questionService.findQuestionByQid(quesNaireId);// 查看该问卷的题目
		if (list != null) {
			questionService.delQuestionByQid(quesNaireId);// 根据quesNaireId删除question表中对应的题目
			optionService.delOptionsWithNotQues();// 删除tid不在question表中的选项，因为上一步删了题目
		}
		return "";
	}

	// 添加问题，和更新问题
	@RequestMapping("/savaQuestion")
	@ResponseBody
	public String savaQuestion(HttpServletRequest req, HttpSession session, @RequestBody Map data) {
		// -------------拆分数据
		boolean must_do = (boolean) data.get("must_do"); // 必做标志
		int quesNaireId = Integer.parseInt((String) data.get("quesNaireId")); // 问题所属问卷的id
		int type = Integer.parseInt((String) data.get("type")); // 问题类型，1为单选，2为多选，3为填空
		String quesname = (String) data.get("quesname"); // 问题名
		String tid = (String) data.get("tid");
		List<String> tnameList = (ArrayList<String>) data.get("optionList"); // 选项列表
		System.out.println("quesNaireId:" + quesNaireId + "\nmust:" + must_do + "\nquesname:" + quesname + "\ntype:"
				+ type + "\ntid" + tid);

		// ---------------保存题目
		Question question = new Question();
		question.setMust_do(must_do);
		question.setQid((long) quesNaireId);
		question.setTname(quesname);
		question.setType(type);
		// questionService.addQuestion(question);

		// 根据tid是否为空判断是更新操作还是新建操作
		if (tid == null || "".equals(tid)) {
			tid = UUID.randomUUID().toString().replaceAll("-", ""); // 利用UUID生成唯一的题目id
			question.setTid(tid);
			questionService.addQuestion(question);

			System.out.println("新建操作");
		} else {
			System.out.println("更新操作");
			question.setTid(tid);
			questionService.updateQuestion(question);
			// 删除原来的选项，重新加入新的选项，实现更新选项的效果
			// 根据tid删除选项
			optionService.delOptionByTid(tid);
		}

		// ---------------保存选项
		if (type == 1 || type == 2) {
			// 选择题
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
			// 填空题
		}
		// ---------------利用for循环遍历保存选项数据,如果type=3为简答题，次步骤忽略
		// ---------------保存题目数据
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

	// 读取问题以及选项
	@RequestMapping("readQuestions")
	@ResponseBody
	public Map readQuestions(HttpSession session, @RequestBody Map<String, String> data) {
		//返回的结果
		Map<String,Object> result = new HashMap<String,Object>();
		//从session中获取user，判断用户否登录
		User user = (User) session.getAttribute("user");
		if(user==null){
			//用户未登录返回为登录标志
			result.put("loginflag", "notlogin");
			return result;
		}
		Long quesNaireId = Long.parseLong(data.get("quesNaireId"));
		System.out.println(quesNaireId);
		// 查找问题
		List<Question> questions = questionService.findQuestionByQid(quesNaireId);
		// 查找选项
		for (Question q : questions) {
			if(q.getType()==1||q.getType()==2){   //选择题，设置其选项列表
				List<Option> options = optionService.findOptionByTid(q.getTid());
				q.setOptions(options);
			}else{     //简答题，设置所有回答者的答案
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
		optionService.delOptionsWithNotQues();// 删除tid不在question表中的选项，因为上一步删了题目
	}

	@RequestMapping("quesNaireSubmit")
	@ResponseBody
	public void quesNaireSubmit(@RequestBody Map data) {
		List<HashMap> answers = (List) data.get("quesData"); // 前台传过来一个JSON数组,用List接收
		quesNaireService.answersAdd(answers);  // 并且泛型为HashMap
		for (HashMap map : answers) {
			System.out.println("--------");
			System.out.println(map.get("tid"));
			System.out.println(map.get("type"));
			System.out.println(map.get("value"));
		}
//		boolean flag = true; // 问卷回答次数加一标志
//		for (HashMap map : answers) { // 遍历每个map，判断type是什么题型
//			System.out.println("--------");
//			System.out.println(map.get("tid"));
//			System.out.println(map.get("type"));
//			System.out.println(map.get("value"));
//			
//			Integer type = (Integer) map.get("type");
//			String tid = (String) map.get("tid");
//			if(flag){
//				//将相应的问卷的回答人数加一，随后flag=false  所以每次提交只加一次
//				quesNaireService.answersAdd(tid);
//				flag = false;
//			}
//			//根据tid将该问题的回答人数加一
//			questionService.answersAdd(tid);
//			
//			//选项回答人数加一，，或保存简答题答案
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
//				System.out.println("问答：" + value);
//			}
//
//		}

		// for (Entry entry : answers.entrySet()) {
		// // 问卷的提交人数+1
		// if (flag) {
		// // 通过tid获得oid，并对oid相应的问卷提交数+1 每次请求只会做一次
		// //quesNaireService.answersAdd((String)entry.getKey());
		// flag = false;
		// }
		// // 问题的回答人数+1
		// //questionService.answersAdd((String)entry.getKey());
		// // 选项的选择人数+1
		// System.out.println("key:" + entry.getKey() + "-----value:" +
		// entry.getValue());
		// }

	}
}
